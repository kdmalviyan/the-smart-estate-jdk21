package com.sfd.thesmartestate.common.exceptions;

/**
 * @author kuldeep
 */
public class FileException extends RuntimeException {
    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable th) {
        super(message, th);
    }
}
