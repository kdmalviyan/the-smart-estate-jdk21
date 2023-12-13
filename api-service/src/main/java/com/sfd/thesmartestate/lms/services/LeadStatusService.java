package com.sfd.thesmartestate.lms.services;

import com.sfd.thesmartestate.lms.entities.LeadStatus;

import java.util.List;

public interface LeadStatusService {
    List<LeadStatus> findAll();

    LeadStatus create(LeadStatus leadStatus);

    LeadStatus update(LeadStatus leadStatus);

    LeadStatus findById(Long id);

    void delete(LeadStatus leadStatus);

    long count();

    LeadStatus findByName(String leadStatusName);

    LeadStatus deactivateLeadStatus(Long id);

    String createLeadStatusName(String name);
}
