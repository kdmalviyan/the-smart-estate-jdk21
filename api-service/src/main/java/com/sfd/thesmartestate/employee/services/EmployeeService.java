package com.sfd.thesmartestate.employee.services;

import com.sfd.thesmartestate.employee.dtos.ChangePasswordRequestPayload;
import com.sfd.thesmartestate.employee.dtos.ResetPasswordRequestPayload;
import com.sfd.thesmartestate.employee.entities.Employee;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();

    List<Employee> findEmployeesByProjectId(Long projectId);

    Employee createEmployee(Employee employee);

    long count();

    Employee update(Employee employee);

    boolean delete(Employee employee);

    Employee findEmployeeByUsername(String username);

    List<Employee> getEmployeeByNameStartsWithAndProjectName(String name, String projectName);

    Employee findById(Long id);

    Employee changePassword(ChangePasswordRequestPayload requestPayload);

    void sendForgotPasswordOTP(String email);

    Employee resetPassword(ResetPasswordRequestPayload requestPayload);

    Employee uploadProfilePhoto(MultipartFile photo, Long userId, String vendorId);

    Employee findByEmployeeUniqueId(String employeeUniqueId);
    Employee findLoggedInEmployee();

}
