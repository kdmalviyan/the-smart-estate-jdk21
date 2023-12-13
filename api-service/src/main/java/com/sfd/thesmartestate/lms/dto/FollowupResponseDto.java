package com.sfd.thesmartestate.lms.dto;

import com.sfd.thesmartestate.users.dtos.UserResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@SuppressFBWarnings("EI_EXPOSE_REP")

public class FollowupResponseDto {
    private Long id;
    private String followupMessage;
    private LeadResponseDto lead;
    private LocalDateTime followupTime;
    private boolean isOpen;
    private UserResponse createdBy;
    private UserResponse updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdateAt;
}
