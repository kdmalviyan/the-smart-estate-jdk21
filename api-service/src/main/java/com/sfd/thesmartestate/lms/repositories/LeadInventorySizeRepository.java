package com.sfd.thesmartestate.lms.repositories;

import com.sfd.thesmartestate.lms.entities.LeadInventorySize;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadInventorySizeRepository extends JpaRepository<LeadInventorySize, Long> {
    LeadInventorySize findBySize(String size);
}
