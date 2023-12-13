package com.sfd.thesmartestate.notifications.exceptions;

public class EmailSendException extends RuntimeException {
    public EmailSendException(String messages, Throwable th) {
        super(messages, th);
    }

    public EmailSendException(String message) {
        super(message);
    }
}
