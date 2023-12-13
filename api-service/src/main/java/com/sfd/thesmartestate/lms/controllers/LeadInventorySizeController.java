package com.sfd.thesmartestate.lms.controllers;

import com.sfd.thesmartestate.lms.entities.LeadInventorySize;
import com.sfd.thesmartestate.lms.services.LeadInventorySizeService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "leadInventorySize")
@AllArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")
@CrossOrigin("*")
public class LeadInventorySizeController {

    private final LeadInventorySizeService leadInventorySizeService;

    @GetMapping
    public ResponseEntity<List<LeadInventorySize>> findAll() {
        return ResponseEntity.ok(leadInventorySizeService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<LeadInventorySize> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(leadInventorySizeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LeadInventorySize> create(@RequestBody LeadInventorySize leadInventorySize) {
        return ResponseEntity.ok(leadInventorySizeService.create(leadInventorySize));
    }

    @PutMapping
    public ResponseEntity<LeadInventorySize> update(@RequestBody LeadInventorySize leadInventorySize) {
        return ResponseEntity.ok(leadInventorySizeService.create(leadInventorySize));
    }

    @DeleteMapping
    public ResponseEntity<LeadInventorySize> deactivateLeadInventory(@RequestParam("id") Long id){
        return ResponseEntity.ok(leadInventorySizeService.deactivateLeadInventory(id));
    }
}
