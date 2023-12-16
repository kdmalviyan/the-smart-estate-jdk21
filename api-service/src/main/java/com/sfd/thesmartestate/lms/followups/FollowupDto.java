package com.sfd.thesmartestate.lms.followups;

import com.sfd.thesmartestate.employee.dtos.UserResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class FollowupDto {
    private Long id;
    private String followupMessage;
    private Long leadId;
    private LocalDateTime followupTime;
    private boolean isOpen = true;
    private UserResponse createdBy;
    private UserResponse updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdateAt;
}
