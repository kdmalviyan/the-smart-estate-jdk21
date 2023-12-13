package com.sfd.thesmartestate.security.exceptions;

public class UnauthorizedOperationException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public UnauthorizedOperationException(String msg) {
        super(msg);
    }
}
