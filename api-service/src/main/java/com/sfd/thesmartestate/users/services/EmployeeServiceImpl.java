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
import com.sfd.thesmartestate.users.entities.Employee;
import com.sfd.thesmartestate.users.entities.LoginDetails;
import com.sfd.thesmartestate.users.exceptions.UserManagementException;
import com.sfd.thesmartestate.users.repositories.UserRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
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

public class EmployeeServiceImpl implements EmployeeService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserHelper userHelper;
    private final NotificationSenderFactory notificationSenderFactory;
    private final FileService fileService;
    private final OTPService otpService;
    private final LoginDetailsService loginDetailsService;
    @Value("${user.default.password}")
    private String defaultPassword;

    @Value("${aws.s3.upload-bucket}")
    private String bucketName;
    @Value("${aws.s3.region}")
    private String region;

    @Value("${tenant.default.aws.bucket}")
    private String profileBucketName;

    @Override
    public List<Employee> findAll() {
        return userRepository.findAll().stream()
                .filter(user -> !user.isSuperAdmin()).collect(Collectors.toList());
    }

    @Override
    public List<Employee> findUsersByProjectId(Long projectId) {
        return userRepository.findByProjectId(projectId);
    }

    @Override
    public Employee createUser(final Employee employee) {
        assignDefaultPassword(employee);
        employee.setActive(true);
        validateUser(employee);
        employee.setCreatedAt(LocalDateTime.now());
        return userRepository.saveAndFlush(employee);
    }

    private void assignDefaultPassword(final Employee employee) {
        employee.setPassword(bCryptPasswordEncoder.encode(defaultPassword));
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public Employee update(Employee employee) {
        Employee loggedInEmployee = findLoggedInEmployee();
        Employee persistentEmployee = validateUsernameChangesAndGetPersistentUser(employee);
        if (Objects.isNull(loggedInEmployee)) {
            throw new UserManagementException("Invalid permissions, please login again.");
        }
        validatePasswordChanges(employee, persistentEmployee,
                loggedInEmployee.isAdmin() || loggedInEmployee.isSuperAdmin());
        validateAdminUpdates(employee);
        employee.setAdmin(checkAdminRole(employee));
        employee.setLastUpdateAt(LocalDateTime.now());
        return userRepository.save(employee);
    }

    private boolean checkAdminRole(Employee employee) {
        if (!employee.getRoles().isEmpty()) {
            Role role = (Role) employee.getRoles().toArray()[0];
            return "ROLE_ADMIN".equalsIgnoreCase(role.getName());
        } else {
            return false;
        }
    }

    private void validatePasswordChanges(Employee employee, Employee persistentEmployee, boolean isAdmin) {
        if (Objects.nonNull(employee)) {
            if (isAdmin && Objects.nonNull(employee.getPassword())) {
                employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
            } else {
                employee.setPassword(persistentEmployee.getPassword());
            }
        }
    }

    private Employee validateUsernameChangesAndGetPersistentUser(Employee employee) {
        Employee persistentEmployee = userRepository.findByUsername(employee.getUsername());
        if (Objects.isNull(persistentEmployee)) {
            throw new UserManagementException("User does not exists, please check if you changed username which is not allowed");
        }
        return persistentEmployee;
    }

    @Override
    public boolean delete(Employee employee) {
        validateAdminUpdates(employee);
        userRepository.delete(employee);
        return true;
    }

    @Override
    public Employee getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<Employee> getUserByNameStartsWithAndProjectName(String name, String projectName) {
        return userRepository.getUserByNameStartsWithAndProjectName(name, projectName);
    }
    private void validateAdminUpdates(Employee employee) {
        Employee persistingEmployee = this.userRepository.findById(employee.getId()).orElse(null);
        assert persistingEmployee != null;
        if (!persistingEmployee.getUsername().equals(employee.getUsername()) && Objects.nonNull(userRepository.findByUsername(employee.getUsername()))) {
            throw new UserManagementException("Username is already taken, Pls try different");
        } else if (!persistingEmployee.getEmail().equals(employee.getEmail()) && Objects.nonNull(userRepository.findByEmail(employee.getEmail()))) {
            throw new UserManagementException("This Email address is already associated with another account");
        } else if ((employee.isAdmin() || employee.isSuperAdmin()) && notActive(employee)) {
            throw new UserManagementException("Disabling super admin is not allowed.");
        }

    }

    private void validateUser(Employee employee) {
        if (Objects.nonNull(userRepository.findByUsername(employee.getUsername()))) {
            throw new UserManagementException("Username is already taken, Pls try different");
        } else if (Objects.nonNull(userRepository.findByEmail(employee.getEmail()))) {
            throw new UserManagementException("This Email address is already associated with another account");
        }
    }

    private boolean notActive(Employee employee) {
        return !employee.isActive();
    }

    @Override
    public Employee findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserManagementException("User does not exists"));
    }

    @Override
    public Employee changePassword(ChangePasswordRequestPayload requestPayload) {
        // Only admins have permissions to change password
        Employee employee = getValidUser(requestPayload.getUsername());
        return updatePasswordAndSaveUser(employee, requestPayload.getPassword());
    }

    private Employee updatePasswordAndSaveUser(Employee employee, String password) {
        employee.setPassword(bCryptPasswordEncoder.encode(password));
        employee.setLastUpdateAt(LocalDateTime.now());
        employee = userRepository.save(employee);
        return userRepository.save(employee);
    }

    @Override
    public void sendForgotPasswordOTP(String emailUsername) {
        Employee employee = getValidUser(emailUsername);
        NotificationSender notificationSender = notificationSenderFactory.get(NotificationType.EMAIL);
        NotificationMessage notificationMessage = userHelper.createPasswordChangeOTPEmailMessage(employee);
        notificationSender.send(notificationMessage, Set.of(employee.getUsername()));
    }

    private Employee getValidUser(String username) {
        Employee employee = userRepository.findByUsername(username);
        if (Objects.isNull(employee)) {
            employee = userRepository.findByEmail(username);
            if (Objects.isNull(employee) || !employee.isActive()) {
                throw new UserManagementException("Invalid username or user does not exists");
            }
        }
        return employee;
    }

    @Override
    public Employee resetPassword(ResetPasswordRequestPayload requestPayload) {
        userHelper.validateResetRequest(requestPayload);
        Employee employee = getValidUser(requestPayload.getUsername());
        OneTimePassword otp = userHelper.validateOTP(employee, requestPayload.getOtp());
        employee = updatePasswordAndSaveUser(employee, requestPayload.getPassword());
        otpService.markOTPUsed(otp);
        return employee;
    }

    @Override
    public Employee uploadProfilePhoto(MultipartFile photo, Long userId, String vendorId) {
        validatePhoto(photo);
        Employee employee = findById(userId);
        String username = employee.getUsername();
        String path = String.format("%s/%s/%s", profileBucketName, Constants.PROFILE_FOLDER_NAME, username);
        String fileName = String.format("%s", photo.getOriginalFilename());
        fileService.uploadFileToS3(path, fileName, photo);
        String pathToSave = String.format("https://s3." + region + ".amazonaws.com/%s/%s/%s/%s", profileBucketName, Constants.PROFILE_FOLDER_NAME, username, fileName);
        employee.setProfileImagePath(pathToSave);

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

            employee.setProfileImageThumbPath(thumbnailPathToSave);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return userRepository.save(employee);
    }

    @Override
    public Employee findByEmployeeUniqueId(String employeeUniqueId) {
        return userRepository.findByEmployeeUniqueId(employeeUniqueId);
    }

    @Override
    public Employee findLoggedInEmployee() {
        LoginDetails loginDetails = loginDetailsService.findLoggedInUser();
        return findByEmployeeUniqueId(loginDetails.getEmployeeUniqueId());
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
