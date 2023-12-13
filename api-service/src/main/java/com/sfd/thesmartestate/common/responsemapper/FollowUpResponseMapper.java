package com.sfd.thesmartestate.common.responsemapper;

import com.sfd.thesmartestate.lms.dto.FollowupResponseDto;
import com.sfd.thesmartestate.lms.followups.Followup;

public class FollowUpResponseMapper {
    private FollowUpResponseMapper() {
        throw new IllegalStateException("Constructor can't be initialize error");
    }

    public static FollowupResponseDto mapToFollowupResponse(Followup followup) {

        return FollowupResponseDto.builder()
                .followupMessage(followup.getFollowupMessage())
                .id(followup.getId())
                .followupTime(followup.getFollowupTime())
                .isOpen(followup.isOpen())
                .lead(LeadResponseMapper.mapToLeadResponse(followup.getLead(), null))
                .createdAt(followup.getCreatedAt())
                .createdBy(UserResponseMapper.mapToUserResponse(followup.getCreatedBy()))
                .lastUpdateAt(followup.getLastUpdateAt())
                .updatedBy(UserResponseMapper.mapToUserResponse(followup.getUpdatedBy()))
                .build();
    }
}
