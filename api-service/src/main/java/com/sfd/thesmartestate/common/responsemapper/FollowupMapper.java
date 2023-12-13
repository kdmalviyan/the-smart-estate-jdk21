package com.sfd.thesmartestate.common.responsemapper;

import com.sfd.thesmartestate.lms.followups.Followup;
import com.sfd.thesmartestate.lms.followups.FollowupDto;

public class FollowupMapper {
    private FollowupMapper() {
        throw new IllegalStateException("Constructor can't be initialize error");
    }
    
    public static FollowupDto mapToFollowup(Followup followup) {
        FollowupDto response = new FollowupDto();
        if (followup != null) {
            response.setId(followup.getId());
            response.setFollowupMessage(followup.getFollowupMessage());
            response.setLastUpdateAt(followup.getLastUpdateAt());
            response.setCreatedAt(followup.getCreatedAt());
            response.setLeadId(followup.getLead().getId());
            response.setOpen(followup.isOpen());
            response.setFollowupTime(followup.getFollowupTime());
            response.setCreatedBy(UserResponseMapper.mapToUserResponse(followup.getCreatedBy()));
            response.setUpdatedBy(UserResponseMapper.mapToUserResponse(followup.getUpdatedBy()));
        }
        return response;

    }
}
