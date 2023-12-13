package com.sfd.thesmartestate.common.responsemapper;

import com.sfd.thesmartestate.lms.dto.LeadAssignHistoryDto;
import com.sfd.thesmartestate.lms.entities.LeadAssignHistory;

public class LeadAssignHistoryMapper {
    private LeadAssignHistoryMapper() {
        throw new IllegalStateException("Constructor can't be initialize error");
    }
    public static LeadAssignHistoryDto mapToLeadAssignHistory(LeadAssignHistory leadAssignHistory) {
        LeadAssignHistoryDto response = new LeadAssignHistoryDto();
        response.setId(leadAssignHistory.getId());
        response.setAssignedTo(UserResponseMapper.mapToUserResponse(leadAssignHistory.getAssignedTo()));
        response.setAssignedFrom(UserResponseMapper.mapToUserResponse(leadAssignHistory.getAssignedFrom()));
        response.setAssignedBy(UserResponseMapper.mapToUserResponse(leadAssignHistory.getAssignedBy()));
        response.setAssignmentTime(leadAssignHistory.getAssignmentTime());
        return response;

    }
}
