package com.sfd.thesmartestate.lms.calls;

import java.util.Set;

public interface CallService {
    Call create(Call call, Long leadId);

    Call findById(Long id);

    Set<Call> getAllByLeadId(Long leadId);
}
