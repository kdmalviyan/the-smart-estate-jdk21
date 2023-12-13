package com.sfd.thesmartestate.lms.controllers;

import com.sfd.thesmartestate.lms.entities.DeactivationReason;
import com.sfd.thesmartestate.lms.services.DeactivationReasonService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/deactivationReason")
@SuppressFBWarnings("EI_EXPOSE_REP")
@CrossOrigin("*")
public class DeactivationReasonController {

    private final DeactivationReasonService deactivationReasonService;

    @GetMapping
    public ResponseEntity<List<DeactivationReason>> getAllDeactivationReason() {
        return ResponseEntity.ok(this.deactivationReasonService.findAll());
    }

    @PostMapping
    public ResponseEntity<DeactivationReason> createDeactivationReason(@RequestBody DeactivationReason deactivationReason) {
        return ResponseEntity.ok(this.deactivationReasonService.create(deactivationReason));
    }

    @PutMapping
    public ResponseEntity<DeactivationReason> update(@RequestBody DeactivationReason deactivationReason) {
        return ResponseEntity.ok(this.deactivationReasonService.update(deactivationReason));
    }

    @DeleteMapping
    public ResponseEntity<DeactivationReason> deactivate(@RequestParam("id") Long id) {
        return ResponseEntity.ok(this.deactivationReasonService.delete(id));
    }
}
