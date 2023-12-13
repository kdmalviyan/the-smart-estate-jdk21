package com.sfd.thesmartestate.lms.repositories;

import com.sfd.thesmartestate.lms.entities.LeadSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeadSourceRepository extends JpaRepository<LeadSource, Long> {
    Optional<LeadSource> findByName(String leadSourceName);
}
