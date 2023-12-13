package com.sfd.thesmartestate.lms.controllers;

import com.sfd.thesmartestate.lms.entities.LeadType;
import com.sfd.thesmartestate.lms.services.LeadTypeService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "leadtype")
@AllArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")
@CrossOrigin("*")
public class LeadTypeController {

    private final LeadTypeService leadTypeService;

    @GetMapping
    public ResponseEntity<List<LeadType>> findAll() {
        return ResponseEntity.ok(leadTypeService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<LeadType> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(leadTypeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LeadType> create(@RequestBody LeadType leadSource) {
        return ResponseEntity.ok(leadTypeService.create(leadSource));
    }

    @PutMapping
    public ResponseEntity<LeadType> update(@RequestBody LeadType leadSource) {
        return ResponseEntity.ok(leadTypeService.update(leadSource));
    }

    @DeleteMapping
    public ResponseEntity<LeadType> deactivateLeadType(@RequestParam("id") Long id) {
        return ResponseEntity.ok(leadTypeService.deactivateLeadType(id));
    }

}
