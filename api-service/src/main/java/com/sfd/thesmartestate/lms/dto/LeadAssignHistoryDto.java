package com.sfd.thesmartestate.lms.dto;

import com.sfd.thesmartestate.users.dtos.UserResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class LeadAssignHistoryDto {
    private Long id;
    private UserResponse assignedBy;
    private UserResponse assignedTo;
    private UserResponse assignedFrom;
    private LocalDateTime assignmentTime;
}
