package com.sfd.thesmartestate.lms.controllers;

import com.sfd.thesmartestate.lms.entities.LeadSource;
import com.sfd.thesmartestate.lms.services.LeadSourceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "leadsource")
@AllArgsConstructor
@CrossOrigin("*")
public class LeadSourceController {
    private final LeadSourceService leadSourceService;

    @GetMapping
    public ResponseEntity<List<LeadSource>> findAll() {
        return ResponseEntity.ok(leadSourceService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<LeadSource> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(leadSourceService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LeadSource> create(@RequestBody LeadSource leadSource) {
        return ResponseEntity.ok(leadSourceService.create(leadSource));
    }

    @PutMapping
    public ResponseEntity<LeadSource> update(@RequestBody LeadSource leadSource) {
        return ResponseEntity.ok(leadSourceService.update(leadSource));
    }

    @DeleteMapping
    public ResponseEntity<LeadSource> deactivateLeadSource(@RequestParam("id") Long id) {
        return ResponseEntity.ok(leadSourceService.deactivateLeadSource(id));
    }
}
