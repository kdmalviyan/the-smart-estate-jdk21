package com.sfd.thesmartestate.projects.exceptions;

public class FileUploadException extends RuntimeException {
    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String message, Throwable th) {
        super(message, th);
    }
}
