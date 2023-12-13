package com.sfd.thesmartestate.projects.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvetoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByNameAndTower(String name, String tower);
}
