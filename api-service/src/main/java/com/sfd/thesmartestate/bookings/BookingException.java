package com.sfd.thesmartestate.bookings;

public class BookingException extends RuntimeException {
    public BookingException(String message) {
        super(message);
    }

    public BookingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
