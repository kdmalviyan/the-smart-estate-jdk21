package com.sfd.thesmartestate.security.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable th) {
        super(message, th);
    }
}
