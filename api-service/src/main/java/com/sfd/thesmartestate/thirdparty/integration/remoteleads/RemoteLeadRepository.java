package com.sfd.thesmartestate.thirdparty.integration.remoteleads;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RemoteLeadRepository extends JpaRepository<RemoteLead, Long> {
    List<RemoteLead> findByAssigneeId(Long userId);

    RemoteLead findByCustomerPhoneAndProjectName(String phone, String name);
}
