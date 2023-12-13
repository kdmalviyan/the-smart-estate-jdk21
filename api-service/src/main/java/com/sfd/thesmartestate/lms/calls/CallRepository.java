package com.sfd.thesmartestate.lms.calls;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface CallRepository extends JpaRepository<Call, Long> {
    Call findByPhoneAndStartTime(String phone, LocalDateTime startTime);
}
