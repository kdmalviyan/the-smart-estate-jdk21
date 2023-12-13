package com.sfd.thesmartestate.lms.exceptions;

public class LeadException  extends RuntimeException {
    public LeadException(String message) {
        super(message);
    }

    public LeadException(String message, Throwable th) {
        super(message, th);
    }
}
