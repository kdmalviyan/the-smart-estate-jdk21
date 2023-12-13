package com.sfd.thesmartestate.lms.rawleads;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RawLeadRepository extends JpaRepository<RawLead, Long> {
    List<RawLead> findByAssigneeId(Long userId);

    RawLead findByCustomerPhoneAndProjectName(String phone, String name);

    RawLead findByCustomerPhone(String phone);
}
