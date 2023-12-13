package com.sfd.thesmartestate.users.services;

import com.sfd.thesmartestate.common.Constants;
import com.sfd.thesmartestate.common.entities.Role;
import com.sfd.thesmartestate.common.services.FileService;
import com.sfd.thesmartestate.notifications.NotificationMessage;
import com.sfd.thesmartestate.notifications.entities.OneTimePassword;
import com.sfd.thesmartestate.notifications.enums.NotificationType;
import com.sfd.thesmartestate.notifications.services.NotificationSender;
import com.sfd.thesmartestate.notifications.services.NotificationSenderFactory;
import com.sfd.thesmartestate.notifications.services.OTPService;
import com.sfd.thesmartestate.users.UserHelper;
import com.sfd.thesmartestate.users.dtos.ChangePasswordRequestPayload;
import com.sfd.thesmartestate.users.dtos.ResetPasswordRequestPayload;
import com.sfd.thesmartestate.users.entities.User;
import com.sfd.thesmartestate.users.exceptions.UserManagementException;
import com.sfd.thesmartestate.users.repositories.UserRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserHelper userHelper;
    private final NotificationSenderFactory notificationSenderFactory;
    private final FileService fileService;
    private final OTPService otpService;

    @Value("${user.default.password}")
    private String defaultPassword;

    @Value("${aws.s3.upload-bucket}")
    private String bucketName;
    @Value("${aws.s3.region}")
    private String region;

    @Value("${tenant.default.aws.bucket}")
    private String profileBucketName;

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream()
                .filter(user -> !user.isSuperAdmin()).collect(Collectors.toList());
    }

    @Override
    public List<User> findUsersByProjectId(Long projectId) {
        return userRepository.findByProjectId(projectId);
    }

    @Override
    public User createUser(final User user) {
        assignDefaultPassword(user);
        user.setEnabled(true);
        validateUser(user);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.saveAndFlush(user);
    }

    private void assignDefaultPassword(final User user) {
        user.setPassword(bCryptPasswordEncoder.encode(defaultPassword));
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public User update(User user) {
        User loggedInUser = findLoggedInUser();
        User persistentUser = validateUsernameChangesAndGetPersistentUser(user);
        if (Objects.isNull(loggedInUser)) {
            throw new UserManagementException("Invalid permissions, please login again.");
        }
        validatePasswordChanges(user, persistentUser,
                loggedInUser.isAdmin() || loggedInUser.isSuperAdmin());
        validateAdminUpdates(user);
        user.setAdmin(checkAdminRole(user));
        user.setLastUpdateAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    private boolean checkAdminRole(User user) {
        if (!user.getRoles().isEmpty()) {
            Role role = (Role) user.getRoles().toArray()[0];
            return "ROLE_ADMIN".equalsIgnoreCase(role.getName());
        } else {
            return false;
        }
    }

    private void validatePasswordChanges(User user, User persistentUser, boolean isAdmin) {
        if (Objects.nonNull(user)) {
            if (isAdmin && Objects.nonNull(user.getPassword())) {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            } else {
                user.setPassword(persistentUser.getPassword());
            }
        }
    }

    private User validateUsernameChangesAndGetPersistentUser(User user) {
        User persistentUser = userRepository.findByUsername(user.getUsername());
        if (Objects.isNull(persistentUser)) {
            throw new UserManagementException("User does not exists, please check if you changed username which is not allowed");
        }
        return persistentUser;
    }

    @Override
    public boolean delete(User user) {
        validateAdminUpdates(user);
        userRepository.delete(user);
        return true;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getUserByNameStartsWithAndProjectName(String name, String projectName) {
        return userRepository.getUserByNameStartsWithAndProjectName(name, projectName);
    }
    private void validateAdminUpdates(User user) {
        User persistingUser = this.userRepository.findById(user.getId()).orElse(null);
        assert persistingUser != null;
        if (!persistingUser.getUsername().equals(user.getUsername()) && Objects.nonNull(userRepository.findByUsername(user.getUsername()))) {
            throw new UserManagementException("Username is already taken, Pls try different");
        } else if (!persistingUser.getEmail().equals(user.getEmail()) && Objects.nonNull(userRepository.findByEmail(user.getEmail()))) {
            throw new UserManagementException("This Email address is already associated with another account");
        } else if ((user.isAdmin() || user.isSuperAdmin()) && notActive(user)) {
            throw new UserManagementException("Disabling super admin is not allowed.");
        }

    }

    private void validateUser(User user) {
        if (Objects.nonNull(userRepository.findByUsername(user.getUsername()))) {
            throw new UserManagementException("Username is already taken, Pls try different");
        } else if (Objects.nonNull(userRepository.findByEmail(user.getEmail()))) {
            throw new UserManagementException("This Email address is already associated with another account");
        }
    }

    private boolean notActive(User user) {
        return !user.isEnabled();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    @Override
    public User findLoggedInUser() {
        try {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            log.error("Unable to find loggedin user");
        }
        return null;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserManagementException("User does not exists"));
    }

    @Override
    public User changePassword(ChangePasswordRequestPayload requestPayload) {
        // Only admins have permissions to change password
        User user = getValidUser(requestPayload.getUsername());
        return updatePasswordAndSaveUser(user, requestPayload.getPassword());
    }

    private User updatePasswordAndSaveUser(User user, String password) {
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setLastUpdateAt(LocalDateTime.now());
        user = userRepository.save(user);
        return userRepository.save(user);
    }

    @Override
    public void sendForgotPasswordOTP(String emailUsername) {
        User user = getValidUser(emailUsername);
        NotificationSender notificationSender = notificationSenderFactory.get(NotificationType.EMAIL);
        NotificationMessage notificationMessage = userHelper.createPasswordChangeOTPEmailMessage(user);
        notificationSender.send(notificationMessage, Set.of(user.getUsername()));
    }

    private User getValidUser(String username) {
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            user = userRepository.findByEmail(username);
            if (Objects.isNull(user) || !user.isEnabled()) {
                throw new UserManagementException("Invalid username or user does not exists");
            }
        }
        return user;
    }

    @Override
    public User resetPassword(ResetPasswordRequestPayload requestPayload) {
        userHelper.validateResetRequest(requestPayload);
        User user = getValidUser(requestPayload.getUsername());
        OneTimePassword otp = userHelper.validateOTP(user, requestPayload.getOtp());
        user = updatePasswordAndSaveUser(user, requestPayload.getPassword());
        otpService.markOTPUsed(otp);
        return user;
    }

    @Override
    public User uploadProfilePhoto(MultipartFile photo, Long userId, String vendorId) {
        validatePhoto(photo);
        User user = findById(userId);
        String username = user.getUsername();
        String path = String.format("%s/%s/%s", profileBucketName, Constants.PROFILE_FOLDER_NAME, username);
        String fileName = String.format("%s", photo.getOriginalFilename());
        fileService.uploadFileToS3(path, fileName, photo);
        String pathToSave = String.format("https://s3." + region + ".amazonaws.com/%s/%s/%s/%s", profileBucketName, Constants.PROFILE_FOLDER_NAME, username, fileName);
        user.setProfileImagePath(pathToSave);

        try {
            BufferedImage thumbnail = Thumbnails.of(photo.getInputStream())
                    .size(50, 50)
                    .asBufferedImage();

            String thumbnailPath = String.format("%s/%s/%s/%s", profileBucketName, Constants.PROFILE_FOLDER_NAME, username, "thumbnail");
            String thumbnailFileName = String.format("%s", photo.getOriginalFilename());
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, "png", os);
            InputStream thumbnail_is = new ByteArrayInputStream(os.toByteArray());
            fileService.uploadFileStreamToS3(thumbnailPath, thumbnailFileName, thumbnail_is);
            String thumbnailPathToSave = String.format("https://s3." + region + ".amazonaws.com/%s/%s/%s/thumbnail/%s", profileBucketName, Constants.PROFILE_FOLDER_NAME, username, fileName);

            user.setProfileImageThumbPath(thumbnailPathToSave);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return userRepository.save(user);
    }

    private void validatePhoto(MultipartFile photo) {
        if (photo.isEmpty()) {
            throw new UserManagementException("Can not upload empty photo");
        }
        //file size check
        if (photo.getSize() < 0) {
            throw new UserManagementException("Can not upload empty photo");
        }
    }
}
