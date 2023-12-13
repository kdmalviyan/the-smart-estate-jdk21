package com.sfd.thesmartestate.notifications.exceptions;

public class OneTimePasswordException extends RuntimeException {
    public OneTimePasswordException(String messages, Throwable th) {
        super(messages, th);
    }

    public OneTimePasswordException(String message) {
        super(message);
    }
}
