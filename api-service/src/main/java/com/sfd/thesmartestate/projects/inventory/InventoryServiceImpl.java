package com.sfd.thesmartestate.projects.inventory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InvetoryRepository repository;

    @Override
    public void save(Inventory inventory) {
        repository.save(inventory);
    }

    @Override
    public Inventory findById(Long inventoryId) {
        return repository.findById(inventoryId).orElseThrow(() -> new RuntimeException("No inventory found by id" + inventoryId));
    }

    @Override
    public Inventory findByNameAndTower(String name, String tower) {
        return repository.findByNameAndTower(name, tower);
    }
}
