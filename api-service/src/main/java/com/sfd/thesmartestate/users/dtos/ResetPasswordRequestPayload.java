package com.sfd.thesmartestate.users.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ResetPasswordRequestPayload {
    private String username;
    private String otp;
    private String password;
    private String rePassword;
}
