package com.sfd.thesmartestate.common.responsemapper;

import com.sfd.thesmartestate.lms.calls.Call;
import com.sfd.thesmartestate.lms.dto.CallDto;

public class CallsMapper {
    private CallsMapper() {
        throw new IllegalStateException("Constructor can't be initialized");
    }

    public static CallDto mapToCall(Call call) {
        CallDto response = new CallDto();
        if (call != null) {
            response.setId(call.getId());
            response.setPhone(call.getPhone());
            response.setComment(call.getComment());
            response.setLatitude(call.getLatitude());
            response.setLongitude(call.getLongitude());
            response.setStartTime(call.getStartTime());
            response.setEndTime(call.getEndTime());
            response.setCreatedAt(call.getCreatedAt());
            response.setCreatedBy(UserResponseMapper.mapToUserResponse(call.getCreatedBy()));
        }
        return response;

    }
}
