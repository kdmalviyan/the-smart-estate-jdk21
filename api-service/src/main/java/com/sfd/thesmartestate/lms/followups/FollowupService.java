package com.sfd.thesmartestate.lms.followups;

import com.sfd.thesmartestate.lms.dto.PageableFilterDto;
import com.sfd.thesmartestate.lms.dto.PageableFollowupResponse;
import com.sfd.thesmartestate.lms.entities.Lead;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface FollowupService {
    List<Followup> findAll();

    Followup create(Followup followup);

    Followup create(String followupTime, Lead lead);

    Followup update(Followup followup);

    Followup findById(Long id);

    void delete(Followup followup);

    Long findAllOpenCount();

    Long findByIsOpenAndFollowupTimeGreaterThan(boolean isOpen, LocalDateTime followupStart, LocalDateTime followupEnd);

    PageableFollowupResponse findAllFollowupPageable(PageableFilterDto filterRequestPayload);

    Followup findOpenFollowupByLead(Lead lead);

    Set<Followup> findFollowupByLead(Lead lead);

    void updateLeadStatus(Followup followup);
}
