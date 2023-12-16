package com.sfd.thesmartestate.users.services;

import com.sfd.thesmartestate.users.entities.LoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author kuldeep
 */
public interface LoginDetailsRepository extends JpaRepository<LoginDetails, Long> {
    Optional<LoginDetails> findByUsername(String username);
}
