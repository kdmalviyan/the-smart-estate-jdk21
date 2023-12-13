package com.sfd.thesmartestate.lms.exceptions;

public class TargetException extends RuntimeException {
    public TargetException(String message) {
        super(message);
    }

    public TargetException(String message, Throwable th) {
        super(message, th);
    }
}
