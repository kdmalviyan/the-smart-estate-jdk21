package com.sfd.thesmartestate.cloud.aws;

/**
 * @author kuldeep
 */
public class FileException extends RuntimeException {
    public FileException(String message, Throwable th) {
        super(message, th);
    }
}
