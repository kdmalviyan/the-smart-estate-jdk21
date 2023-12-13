package com.sfd.thesmartestate.common.responsemapper;

import com.sfd.thesmartestate.lms.dto.CallDto;
import com.sfd.thesmartestate.lms.dto.CommentDto;
import com.sfd.thesmartestate.lms.dto.LeadAssignHistoryDto;
import com.sfd.thesmartestate.lms.dto.LeadResponseDto;
import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.lms.followups.Followup;
import com.sfd.thesmartestate.lms.followups.FollowupDto;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class LeadResponseMapper {

    private LeadResponseMapper() {
        throw new IllegalStateException("Constructor can't be initialize error");
    }

    public static LeadResponseDto mapToLeadResponse(Lead lead, Set<Followup> leadFollowups) {

        Set<LeadAssignHistoryDto> leadAssignHistoryDtos = lead.getLeadAssignHistory().stream()
                .map(LeadAssignHistoryMapper::mapToLeadAssignHistory).collect(Collectors.toSet());

        Set<CommentDto> comments = lead.getComments().stream()
                .map(CommentsMapper::mapToComments)
                .sorted(Comparator.comparingLong(CommentDto::getId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<CallDto> callsDto = lead.getCalls().stream()
                .map(CallsMapper::mapToCall)
                .sorted(Comparator.comparingLong(CallDto::getId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<FollowupDto> followups = new LinkedHashSet<>();
        if (leadFollowups != null) {
            followups = leadFollowups.stream()
                    .map(FollowupMapper::mapToFollowup)
                    .sorted(Comparator.comparingLong(FollowupDto::getId).reversed())
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        return LeadResponseDto.builder()
                .id(lead.getId())
                .customer(lead.getCustomer())
                .project(ProjectResponseMapper.mapToProjectResponse(lead.getProject()))
                .source(MetadataResponseMapper.mapToLeadSource(lead.getSource()))
                .type(MetadataResponseMapper.mapToLeadType(lead.getType()))
                .leadInventorySize(MetadataResponseMapper.mapToLeadInventorySizes(lead.getLeadInventorySize()))
                .status(MetadataResponseMapper.mapToLeadStatus(lead.getStatus()))
                .deactivationReason(MetadataResponseMapper.mapToDeactivationReason(lead.getDeactivationReason()))
                .budget(lead.getBudget())
                .assignedTo(UserResponseMapper.mapToUserResponse(lead.getAssignedTo()))
                .createdBy(UserResponseMapper.mapToUserResponse(lead.getCreatedBy()))
                .updatedBy(UserResponseMapper.mapToUserResponse(lead.getUpdatedBy()))
                ///pending
                .leadAssignHistory(leadAssignHistoryDtos)
                .comments(comments)
                .createdAt(lead.getCreatedAt())
                .lastUpdateAt(lead.getLastUpdateAt())
                .followups(followups)
                .siteVisit(lead.isSiteVisit())
                .calls(callsDto).build();
    }
}
