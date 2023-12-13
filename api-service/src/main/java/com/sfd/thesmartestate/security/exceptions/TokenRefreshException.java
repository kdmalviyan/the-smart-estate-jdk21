package com.sfd.thesmartestate.security.exceptions;

public class TokenRefreshException extends RuntimeException {
    public TokenRefreshException(String message) {
        super(message);
    }

    public TokenRefreshException(String message, Throwable th) {
        super(message, th);
    }
}
