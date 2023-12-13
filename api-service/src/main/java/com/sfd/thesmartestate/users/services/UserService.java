package com.sfd.thesmartestate.users.services;

import com.sfd.thesmartestate.users.dtos.ChangePasswordRequestPayload;
import com.sfd.thesmartestate.users.dtos.ResetPasswordRequestPayload;
import com.sfd.thesmartestate.users.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> findAll();

    List<User> findUsersByProjectId(Long projectId);

    User createUser(User user);

    long count();

    User update(User user);

    boolean delete(User user);

    User getUserByUsername(String username);

    List<User> getUserByNameStartsWithAndProjectName(String name, String projectName);

    User findLoggedInUser();

    User findById(Long id);

    User changePassword(ChangePasswordRequestPayload requestPayload);

    void sendForgotPasswordOTP(String email);

    User resetPassword(ResetPasswordRequestPayload requestPayload);

    User uploadProfilePhoto(MultipartFile photo, Long userId, String vendorId);

}
