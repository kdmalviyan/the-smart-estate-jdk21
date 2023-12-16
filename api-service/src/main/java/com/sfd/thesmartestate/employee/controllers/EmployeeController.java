package com.sfd.thesmartestate.employee.controllers;

import com.sfd.thesmartestate.employee.dtos.ChangePasswordRequestPayload;
import com.sfd.thesmartestate.employee.dtos.ResetPasswordRequestPayload;
import com.sfd.thesmartestate.employee.entities.Employee;
import com.sfd.thesmartestate.employee.services.EmployeeService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user")
@Slf4j
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")
@CrossOrigin("*")
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Get all employee and FILTER superadmin
     *
     */
    @GetMapping("")
    public ResponseEntity<List<Employee>> listAllUsers() {
        return ResponseEntity.ok(employeeService.findAll().stream().filter(user -> !user.isSuperAdmin()).collect(Collectors.toList()));
    }

    @PostMapping("")
    public ResponseEntity<Employee> createUser(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.createEmployee(employee));
    }

    @PutMapping("")
    public ResponseEntity<Employee> update(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.update(employee));
    }

    @PutMapping("password/change")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestPayload requestPayload) {
        log.info("Changing password for " + requestPayload.getUsername());
        return ResponseEntity.ok(employeeService.changePassword(requestPayload));
    }

    @GetMapping("password/forgot/{emailUsername}")
    public ResponseEntity<?> forgot(@PathVariable("emailUsername") String emailUsername) {
        log.info("Sending password reset OTP for " + emailUsername);
        employeeService.sendForgotPasswordOTP(emailUsername);
        return ResponseEntity.ok("OTP sent successfully for changing password, Please check your email!");
    }

    @PutMapping("password/reset")
    public ResponseEntity<Employee> resetPassword(@RequestBody ResetPasswordRequestPayload requestPayload) {
        log.info("Resetting password for " + requestPayload.getUsername());
        return ResponseEntity.ok(employeeService.resetPassword(requestPayload));
    }

    @GetMapping(value = "project/{projectId}")
    public ResponseEntity<List<Employee>> findUsersByProject(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(employeeService.findEmployeesByProjectId(projectId));
    }

    @GetMapping(value = "/userName")
    public ResponseEntity<Employee> findUserByUserName(@RequestParam("userName") String userName) {
        return ResponseEntity.ok(employeeService.findEmployeeByUsername(userName));
    }

    @PostMapping(
            value = "/uploadImage/{userId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Employee> uploadUserProfilePhoto(
            @RequestParam("file") MultipartFile photo,
            @PathVariable("userId") Long userId,
            @RequestHeader("X-VendorID") String vendorId
    ) {
        return ResponseEntity.ok(employeeService.uploadProfilePhoto(photo, userId, vendorId));
    }
}
