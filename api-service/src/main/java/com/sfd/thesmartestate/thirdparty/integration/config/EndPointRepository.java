package com.sfd.thesmartestate.thirdparty.integration.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface EndPointRepository extends JpaRepository<EndPoint, Integer> {

    @Query("SELECT q FROM EndPoint q where isActive = true")
    Set<EndPoint> findAllActive();
}
