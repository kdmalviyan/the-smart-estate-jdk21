package com.sfd.thesmartestate.employee.exceptions;

public class UserManagementException extends RuntimeException {
    public UserManagementException(String message) {
        super(message);
    }

    public UserManagementException(String message, Throwable th) {
        super(message, th);
    }
}
