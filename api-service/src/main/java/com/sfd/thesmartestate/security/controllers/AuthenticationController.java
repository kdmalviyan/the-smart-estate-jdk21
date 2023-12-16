package com.sfd.thesmartestate.security.controllers;

import com.sfd.thesmartestate.common.entities.Role;
import com.sfd.thesmartestate.common.responsemapper.ProjectResponseMapper;
import com.sfd.thesmartestate.customer.entities.Customer;
import com.sfd.thesmartestate.customer.services.CustomerService;
import com.sfd.thesmartestate.security.entities.RefreshToken;
import com.sfd.thesmartestate.security.services.AuthenticationService;
import com.sfd.thesmartestate.security.services.RefreshTokenService;
import com.sfd.thesmartestate.users.entities.Employee;
import com.sfd.thesmartestate.users.entities.LoginDetails;
import com.sfd.thesmartestate.users.services.LoginDetailsService;
import com.sfd.thesmartestate.users.services.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "auth")
@Slf4j
public record AuthenticationController(AuthenticationService authenticationService,
                                       RefreshTokenService refreshTokenService,
                                       UserService userService,
                                       LoginDetailsService loginDetailsService,
                                       CustomerService customerService) {

    @PostMapping("/token")
    public ResponseEntity<AuthResponse> generateToken(@RequestBody Credentials credentials) throws NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("Token generation starts");

        AuthResponse response = AuthResponse.builder().withToken(authenticationService
                        .generateToken(credentials.getUsername(), credentials.getPassword()))
                .withUsername(credentials.getUsername())
                .withRefreshToken(refreshTokenService.createRefreshToken(credentials.getUsername())).build();

        LoginDetails loginDetails = loginDetailsService.findLoggedInUser();
        if(Objects.nonNull(loginDetails.getEmployeeUniqueId())) {
            Employee employee = userService.findByEmployeeUniqueId(loginDetails.getEmployeeUniqueId());
            response.setAdmin(employee.isAdmin());
            response.setSuperAdmin(employee.isSuperAdmin());
            response.setProfilePath(employee.getProfileImageThumbPath());
            Optional<Role> userRole = employee.getRoles().stream().findFirst();
            userRole.ifPresent(role -> response.setRole(role.getDescription()));
            response.setProject(ProjectResponseMapper.mapToProjectResponse(employee.getProject()));
        } else if(Objects.nonNull(loginDetails.getCustomerUniqueId())) {
            Customer customer = customerService.findByCustomerUniqueId(loginDetails.getCustomerUniqueId());
            response.setAdmin(false);
            response.setSuperAdmin(false);
            response.setProfilePath(customer.getProfileImageThumbPath());
            Optional<Role> userRole = customer.getLoginDetails().getRoles().stream().findFirst();
            userRole.ifPresent(role -> response.setRole(role.getDescription()));
        }

        log.info("Token generated successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<AuthResponse> generateTokenWithRefreshToken(@RequestBody Credentials credentials) {
        log.info("Token generation with refresh token");
        AuthResponse response = AuthResponse.builder()
                .withToken(authenticationService.generateTokenWithRefreshToken(credentials.getUsername(),
                        credentials.getRefreshToken()))
                .withUsername(credentials.getUsername())
                .withRefreshToken(refreshTokenService.createRefreshToken(credentials.getUsername())).build();
        log.info("Token generated successfully");
        return ResponseEntity.ok(response);
    }
}

@Data
class Credentials {
    private RefreshToken refreshToken;
    private String username;
    private String password;
}
