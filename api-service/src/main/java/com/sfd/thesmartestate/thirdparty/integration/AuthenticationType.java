package com.sfd.thesmartestate.thirdparty.integration;

public enum AuthenticationType {
    SECRET_KEY(1, "Secret key based"),

    BASIC(2, "Username password based"),

    TOKEN_BASED(3, "JWT token Based"),

    PUBLIC(4, "No authentication is required");

    AuthenticationType(int id, String name) {
    }
}
