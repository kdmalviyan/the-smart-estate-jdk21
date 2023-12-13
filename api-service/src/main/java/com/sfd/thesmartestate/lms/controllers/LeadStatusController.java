package com.sfd.thesmartestate.lms.controllers;

import com.sfd.thesmartestate.lms.entities.LeadStatus;
import com.sfd.thesmartestate.lms.services.LeadStatusService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "leadstatus")
@AllArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")
@CrossOrigin("*")
public class LeadStatusController {
    private final LeadStatusService leadStatusService;

    @GetMapping
    public ResponseEntity<List<LeadStatus>> findAll() {
        return ResponseEntity.ok(leadStatusService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<LeadStatus> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(leadStatusService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LeadStatus> create(@RequestBody LeadStatus leadSource) {
        return ResponseEntity.ok(leadStatusService.create(leadSource));
    }

    @PutMapping
    public ResponseEntity<LeadStatus> update(@RequestBody LeadStatus leadSource) {
        return ResponseEntity.ok(leadStatusService.update(leadSource));
    }

    @DeleteMapping
    public ResponseEntity<LeadStatus> deactivateLeadStatus(@RequestParam("id") Long id) {
        return ResponseEntity.ok(leadStatusService.deactivateLeadStatus(id));
    }
}
