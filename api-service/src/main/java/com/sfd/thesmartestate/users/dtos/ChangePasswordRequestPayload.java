package com.sfd.thesmartestate.users.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ChangePasswordRequestPayload {
    private String username;
    private String oldPassword;
    private String password;
    private String rePassword;
}
