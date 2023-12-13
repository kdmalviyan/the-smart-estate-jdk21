package com.sfd.thesmartestate.notifications.repositories;

import com.sfd.thesmartestate.notifications.entities.OneTimePassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPRepository extends JpaRepository<OneTimePassword, Long> {
    OneTimePassword findByUsername(String username);
}
