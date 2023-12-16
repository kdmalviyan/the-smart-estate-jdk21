package com.sfd.thesmartestate.employee.teams.exceptions;

public class TeamException extends RuntimeException {
    public TeamException(String message) {
        super(message);
    }

    public TeamException(String message, Throwable th) {
        super(message, th);
    }
}
