package com.sfd.thesmartestate.lms.dto;

import com.sfd.thesmartestate.users.dtos.UserResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class CommentDto {
    private Long id;
    private String message;
    private UserResponse createdBy;
    private LocalDateTime createdAt;
    private String commentType;
}
