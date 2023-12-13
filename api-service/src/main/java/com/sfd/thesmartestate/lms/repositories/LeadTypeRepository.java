package com.sfd.thesmartestate.lms.repositories;

import com.sfd.thesmartestate.lms.entities.LeadType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeadTypeRepository extends JpaRepository<LeadType, Long> {
    Optional<LeadType> findByName(String type);
}
