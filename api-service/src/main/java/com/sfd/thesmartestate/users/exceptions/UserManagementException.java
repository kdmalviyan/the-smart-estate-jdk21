package com.sfd.thesmartestate.users.exceptions;

public class UserManagementException extends RuntimeException {
    public UserManagementException(String message) {
        super(message);
    }

    public UserManagementException(String message, Throwable th) {
        super(message, th);
    }
}
