package com.sfd.thesmartestate.users.controllers;

import com.sfd.thesmartestate.users.dtos.ChangePasswordRequestPayload;
import com.sfd.thesmartestate.users.dtos.ResetPasswordRequestPayload;
import com.sfd.thesmartestate.users.entities.Employee;
import com.sfd.thesmartestate.users.services.UserService;
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
public class UserController {

    private final UserService userService;

    /**
     * Get all users and FILTER superadmin
     *
     */
    @GetMapping("")
    public ResponseEntity<List<Employee>> listAllUsers() {
        return ResponseEntity.ok(userService.findAll().stream().filter(user -> !user.isSuperAdmin()).collect(Collectors.toList()));
    }

    @PostMapping("")
    public ResponseEntity<Employee> createUser(@RequestBody Employee employee) {
        return ResponseEntity.ok(userService.createUser(employee));
    }

    @PutMapping("")
    public ResponseEntity<Employee> update(@RequestBody Employee employee) {
        return ResponseEntity.ok(userService.update(employee));
    }

    @PutMapping("password/change")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestPayload requestPayload) {
        log.info("Changing password for " + requestPayload.getUsername());
        return ResponseEntity.ok(userService.changePassword(requestPayload));
    }

    @GetMapping("password/forgot/{emailUsername}")
    public ResponseEntity<?> forgot(@PathVariable("emailUsername") String emailUsername) {
        log.info("Sending password reset OTP for " + emailUsername);
        userService.sendForgotPasswordOTP(emailUsername);
        return ResponseEntity.ok("OTP sent successfully for changing password, Please check your email!");
    }

    @PutMapping("password/reset")
    public ResponseEntity<Employee> resetPassword(@RequestBody ResetPasswordRequestPayload requestPayload) {
        log.info("Resetting password for " + requestPayload.getUsername());
        return ResponseEntity.ok(userService.resetPassword(requestPayload));
    }

    @GetMapping(value = "project/{projectId}")
    public ResponseEntity<List<Employee>> findUsersByProject(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(userService.findUsersByProjectId(projectId));
    }

    @GetMapping(value = "/userName")
    public ResponseEntity<Employee> findUserByUserName(@RequestParam("userName") String userName) {
        return ResponseEntity.ok(userService.getUserByUsername(userName));
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
        return ResponseEntity.ok(userService.uploadProfilePhoto(photo, userId, vendorId));
    }
}
