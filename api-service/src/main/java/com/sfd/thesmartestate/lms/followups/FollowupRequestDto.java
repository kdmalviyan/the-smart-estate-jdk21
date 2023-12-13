package com.sfd.thesmartestate.lms.followups;

import lombok.Data;

@Data
public class FollowupRequestDto {
    private Long followUpId;
    private String followupMessage;
}
