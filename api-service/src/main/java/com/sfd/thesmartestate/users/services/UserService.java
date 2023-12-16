package com.sfd.thesmartestate.users.services;

import com.sfd.thesmartestate.users.dtos.ChangePasswordRequestPayload;
import com.sfd.thesmartestate.users.dtos.ResetPasswordRequestPayload;
import com.sfd.thesmartestate.users.entities.Employee;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<Employee> findAll();

    List<Employee> findUsersByProjectId(Long projectId);

    Employee createUser(Employee employee);

    long count();

    Employee update(Employee employee);

    boolean delete(Employee employee);

    Employee getUserByUsername(String username);

    List<Employee> getUserByNameStartsWithAndProjectName(String name, String projectName);

    Employee findLoggedInUser();

    Employee findById(Long id);

    Employee changePassword(ChangePasswordRequestPayload requestPayload);

    void sendForgotPasswordOTP(String email);

    Employee resetPassword(ResetPasswordRequestPayload requestPayload);

    Employee uploadProfilePhoto(MultipartFile photo, Long userId, String vendorId);

    Employee findByEmployeeUniqueId(String employeeUniqueId);

}
