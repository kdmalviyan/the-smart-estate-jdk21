package com.sfd.thesmartestate.lms.services;

import com.sfd.thesmartestate.lms.entities.LeadType;

import java.util.List;

public interface LeadTypeService {
    List<LeadType> findAll();

    LeadType create(LeadType leadType);

    LeadType update(LeadType leadType);

    LeadType findById(Long id);

    void delete(LeadType leadType);

    long count();

    LeadType findByType(String type);

    LeadType deactivateLeadType(Long id);

    String createLeadType(String leadType);
}
