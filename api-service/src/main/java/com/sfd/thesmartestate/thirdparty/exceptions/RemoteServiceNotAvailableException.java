package com.sfd.thesmartestate.thirdparty.exceptions;

public class RemoteServiceNotAvailableException extends RuntimeException {
    public RemoteServiceNotAvailableException(String message) {
        super(message);
    }

    public RemoteServiceNotAvailableException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
