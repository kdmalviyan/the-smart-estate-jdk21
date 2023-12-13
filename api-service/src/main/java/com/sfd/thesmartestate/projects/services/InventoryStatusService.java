package com.sfd.thesmartestate.projects.services;

import com.sfd.thesmartestate.projects.entities.InventoryStatus;

import java.util.List;

public interface InventoryStatusService {
    void create(InventoryStatus inventory);

    InventoryStatus update(InventoryStatus inventory);

    InventoryStatus findById(Long id);

    List<InventoryStatus> findAll();

    void delete(InventoryStatus inventory);

    InventoryStatus findByName(String name);
}
