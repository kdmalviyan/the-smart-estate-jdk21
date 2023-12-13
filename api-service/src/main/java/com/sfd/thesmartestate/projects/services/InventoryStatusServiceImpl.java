package com.sfd.thesmartestate.projects.services;

import com.sfd.thesmartestate.projects.entities.InventoryStatus;
import com.sfd.thesmartestate.projects.repositories.InventoryStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InventoryStatusServiceImpl implements InventoryStatusService {

    private final InventoryStatusRepository inventoryStatusRepository;

    @Override
    public void create(InventoryStatus inventory) {
        inventoryStatusRepository.save(inventory);
    }

    @Override
    public InventoryStatus update(InventoryStatus inventory) {
        return inventoryStatusRepository.save(inventory);
    }

    @Override
    public InventoryStatus findById(Long id) {
        return inventoryStatusRepository.findById(id).orElse(null);
    }

    @Override
    public List<InventoryStatus> findAll() {
        return inventoryStatusRepository.findAll();
    }

    @Override
    public void delete(InventoryStatus inventory) {
        inventoryStatusRepository.delete(inventory);
    }

    @Override
    public InventoryStatus findByName(String name) {
        return inventoryStatusRepository.findByName(name);
    }
}
