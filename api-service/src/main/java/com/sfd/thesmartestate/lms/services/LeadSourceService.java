package com.sfd.thesmartestate.lms.services;

import com.sfd.thesmartestate.lms.entities.LeadSource;

import java.util.List;

public interface LeadSourceService {
    List<LeadSource> findAll();

    LeadSource create(LeadSource leadSource);

    LeadSource update(LeadSource leadSource);

    LeadSource findById(Long id);

    LeadSource deactivateLeadSource(Long id);

    long count();

    LeadSource findByName(String leadSourceName);

    String createLeadSourceName(String description);
}
