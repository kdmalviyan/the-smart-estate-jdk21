package com.sfd.thesmartestate.lms.dto;

import com.sfd.thesmartestate.employee.dtos.UserResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@SuppressFBWarnings("EI_EXPOSE_REP")
public class CallDto {
    private Long id;
    private String phone;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double latitude;
    private Double longitude;
    private String comment;
    private UserResponse createdBy;
    private LocalDateTime createdAt;
}
