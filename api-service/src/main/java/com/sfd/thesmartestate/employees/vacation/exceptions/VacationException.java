package com.sfd.thesmartestate.employees.vacation.exceptions;

public class VacationException extends RuntimeException {
    public VacationException(String message) {
        super(message);
    }

    public VacationException(Throwable th, String message) {
        super(message, th);
    }
}
