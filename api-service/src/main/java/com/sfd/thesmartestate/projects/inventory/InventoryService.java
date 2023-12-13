package com.sfd.thesmartestate.projects.inventory;

public interface InventoryService {
    void save(Inventory inventory);
    Inventory findById(Long inventoryId);

    Inventory findByNameAndTower(String name, String tower);
}
