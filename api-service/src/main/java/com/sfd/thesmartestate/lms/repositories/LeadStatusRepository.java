package com.sfd.thesmartestate.lms.repositories;

import com.sfd.thesmartestate.lms.entities.LeadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeadStatusRepository extends JpaRepository<LeadStatus, Long> {
    Optional<LeadStatus> findByName(String leadStatusName);
}
