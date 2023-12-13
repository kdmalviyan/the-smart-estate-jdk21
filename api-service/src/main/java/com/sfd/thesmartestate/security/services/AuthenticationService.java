package com.sfd.thesmartestate.security.services;

import com.sfd.thesmartestate.security.entities.RefreshToken;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface AuthenticationService {
    String generateToken(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException;

    boolean changePassword(String username, String newPassword, String oldPassword);

    boolean resetPassword(String username, String password, String otp);

    String generateTokenWithRefreshToken(String username, RefreshToken refreshToken);
}
