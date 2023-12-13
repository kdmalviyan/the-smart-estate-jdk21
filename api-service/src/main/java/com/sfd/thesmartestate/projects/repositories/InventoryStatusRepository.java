package com.sfd.thesmartestate.projects.repositories;

import com.sfd.thesmartestate.projects.entities.InventoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryStatusRepository extends JpaRepository<InventoryStatus, Long> {
    InventoryStatus findByName(String name);
}
