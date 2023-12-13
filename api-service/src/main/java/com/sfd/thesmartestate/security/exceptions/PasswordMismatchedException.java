package com.sfd.thesmartestate.security.exceptions;

public class PasswordMismatchedException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public PasswordMismatchedException(String message) {
        super(message);
    }

}
