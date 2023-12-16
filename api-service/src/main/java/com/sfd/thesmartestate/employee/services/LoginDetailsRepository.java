package com.sfd.thesmartestate.employee.services;

import com.sfd.thesmartestate.employee.entities.LoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author kuldeep
 */
public interface LoginDetailsRepository extends JpaRepository<LoginDetails, Long> {
    Optional<LoginDetails> findByUsername(String username);
}
