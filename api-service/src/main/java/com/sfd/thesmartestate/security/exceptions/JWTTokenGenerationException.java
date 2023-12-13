package com.sfd.thesmartestate.security.exceptions;

public class JWTTokenGenerationException extends RuntimeException {
    public JWTTokenGenerationException(String message) {
        super(message);
    }

    public JWTTokenGenerationException(String message, Throwable th) {
        super(message, th);
    }
}
