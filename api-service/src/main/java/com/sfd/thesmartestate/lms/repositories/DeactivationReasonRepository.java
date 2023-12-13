package com.sfd.thesmartestate.lms.repositories;

import com.sfd.thesmartestate.lms.entities.DeactivationReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeactivationReasonRepository extends JpaRepository<DeactivationReason, Long> {
    DeactivationReason findByName(String name);

}
