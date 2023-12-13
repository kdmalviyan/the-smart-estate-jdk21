package com.sfd.thesmartestate.common;

public final class Constants {
    private Constants() {
        throw new IllegalStateException("Constructor can't be initialize error");
    }
    public static final String EMPTY = "";
    public static final String USERNAME = "username";
    public static final String ROLE = "role";
    public static final String RSA = "RSA";
    public static final String AUTHENTICATION_HEADER = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String SUPERADMIN = "ROLE_SUPERADMIN";
    public static final String PASSWORD = "password";

    public static final String BOOKING_FOLDER_NAME = "Bookings";
    public static final String APPLICATION_FORM = "Application-Form";
    public static final String BUYER_PAN_CARD = "Buyer-Pan-Card";
    public static final String BUYER_AADHAAR_CARD = "Buyer-Aadhaar-Card";
    public static final String PAYMENT_COPY = "Payment-Copy";
    public static final String CO_BUYER_PAN_CARD = "Co-Buyer-Pan-Card";
    public static final String CO_BUYER_AADHAAR_CARD = "Co-Buyer-Aadhaar-Card";
    public static final String PROFILE_FOLDER_NAME = "Profile-Photos";

    public static final String PUBLIC_KEY_START_TEXT = "-----BEGIN PUBLIC KEY-----";
    public static final String PUBLIC_KEY_END_TEXT = "-----END PUBLIC KEY-----";
    public static final String PRIVATE_KEY_START_TEXT = "-----BEGIN PRIVATE KEY-----";
    public static final String PRIVATE_KEY_END_TEXT = "-----END PRIVATE KEY-----";
    public static final String TOKEN_URL = "/token";

    //-----inventory status-
    public static final String ON_HOLD = "ON_HOLD";
    public static final String AVAILABLE = "AVAILABLE";

    //DEACTIVATION REASON
    public static final String BUDGET = "budget";
    public static final String LOCATION = "location";
    public static final String READY_TO_MOVE = "ready_to_move";
    public static final String RENT = "rent";

    public static final String ACTIVE = "ACTIVE";
    public static final String IN_PROCESS = "IN-PROCESS";
    public static final String BOOKED = "BOOKED";
    public static final String DEACTIVE = "DEACTIVE";
    public static final String FOLLOW_UP = "FOLLOW-UP";
    public static final String FOLLOW_UP_EXPIRE = "FOLLOW-UP-EXPIRE";
    public static final String FOLLOW_UP_COMPLETE = "FOLLOW-UP-COMPLETE";

}
