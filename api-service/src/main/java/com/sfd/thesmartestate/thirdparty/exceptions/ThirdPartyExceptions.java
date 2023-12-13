package com.sfd.thesmartestate.thirdparty.exceptions;

public class ThirdPartyExceptions extends RuntimeException {
    public ThirdPartyExceptions(String message) {
        super(message);
    }

    public ThirdPartyExceptions(String message, Throwable throwable) {
        super(message, throwable);
    }
}
