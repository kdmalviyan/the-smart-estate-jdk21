package com.sfd.thesmartestate.lms.services;

import com.sfd.thesmartestate.lms.entities.LeadInventorySize;

import java.util.List;

public interface LeadInventorySizeService {
    List<LeadInventorySize> findAll();

    LeadInventorySize create(LeadInventorySize leadInventorySize);

    LeadInventorySize update(LeadInventorySize leadInventorySize);

    LeadInventorySize findById(Long id);

    void delete(LeadInventorySize leadInventorySize);

    long count();

    LeadInventorySize findBySize(String size);

    LeadInventorySize deactivateLeadInventory(Long id);

    String createInventorySize(String size);
}
