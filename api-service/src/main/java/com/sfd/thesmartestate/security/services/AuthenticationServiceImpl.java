package com.sfd.thesmartestate.security.services;

import com.sfd.thesmartestate.security.JwtTokenGenerator;
import com.sfd.thesmartestate.security.entities.RefreshToken;
import com.sfd.thesmartestate.security.exceptions.InvalidCredentialsException;
import com.sfd.thesmartestate.security.exceptions.UserDisableException;
import com.sfd.thesmartestate.users.entities.User;
import com.sfd.thesmartestate.users.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenService refreshTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String generateToken(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        UserDetails userDetails = userService.loadUserByUsername(username);
        log.info("Validating user found in database");
        // check user found or not
        checkUserFoundAndNotDisabled(username, userDetails);
        log.info("User is valid");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        log.info("Authenticating user");
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        log.info("User authenticated");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return (jwtTokenGenerator.generate((User) userDetails, false));
    }

    private void checkUserFoundAndNotDisabled(String username, UserDetails userDetails) {
        if (userDetails == null) {
            throw new UsernameNotFoundException("Username " + username + " not found");
        } else {
            if (!userDetails.isEnabled()) {
                throw new UserDisableException("User account is disabled, please check with your admin team.");
            }
        }
    }

    @Override
    public boolean changePassword(String username, String newPassword, String oldPassword) {
        try {
            User user = (User) userService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, oldPassword);
            authenticationManager.authenticate(authenticationToken);
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userService.update(user);
        } catch (Throwable throwable) {
            throw new InvalidCredentialsException("Invalid credentials!");
        }
        return true;
    }

    @Override
    public boolean resetPassword(String username, String password, String otp) {
        // Validate OTP
        try {
            User user = (User) userService.loadUserByUsername(username);
            // Validate OTP else throw exception
            user.setPassword(bCryptPasswordEncoder.encode(password));
            userService.update(user);
        } catch (Throwable throwable) {
            return false;
        }
        return true;
    }

    @Override
    public String generateTokenWithRefreshToken(String username, RefreshToken refreshToken) {
        refreshTokenService.verifyExpiration(refreshToken);
        UserDetails userDetails = userService.loadUserByUsername(username);
        log.info("Validating user found in database");
        // check user found or not
        checkUserFoundAndNotDisabled(username, userDetails);
        try {
            return jwtTokenGenerator.generate((User) userDetails, false);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new InvalidCredentialsException(e.getMessage());
        }
    }
}