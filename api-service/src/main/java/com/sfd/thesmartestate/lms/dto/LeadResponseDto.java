package com.sfd.thesmartestate.lms.dto;

import com.sfd.thesmartestate.common.dto.MetadataResponse;
import com.sfd.thesmartestate.customer.entities.Customer;
import com.sfd.thesmartestate.lms.entities.Budget;
import com.sfd.thesmartestate.lms.followups.FollowupDto;
import com.sfd.thesmartestate.projects.dto.ProjectDTO;
import com.sfd.thesmartestate.employee.dtos.UserResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class LeadResponseDto {
    //lead detail
    private Long id;
    private Customer customer;
    private ProjectDTO project;

    // metadata
    private MetadataResponse source;
    private MetadataResponse type; //Hot/Warm/Cold;
    private MetadataResponse leadInventorySize;
    private MetadataResponse status; //Visit Done, Followup
    private MetadataResponse deactivationReason;
    private Budget budget;

    //lead user info
    private UserResponse assignedTo;
    private UserResponse updatedBy;
    private UserResponse createdBy;
    private Set<UserResponse> watchers;
    private Set<LeadAssignHistoryDto> leadAssignHistory;


    private Set<CommentDto> comments;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdateAt;

    private Set<FollowupDto> followups;
    private boolean siteVisit;
    private Set<CallDto> calls;
}
