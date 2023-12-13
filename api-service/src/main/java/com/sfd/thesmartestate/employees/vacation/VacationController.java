package com.sfd.thesmartestate.employees.vacation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("vacation")
@RequiredArgsConstructor
@CrossOrigin("*")
public class VacationController {
    private final VacationService service;

    @PostMapping("")
    public ResponseEntity<Vacation> create(@RequestBody Vacation vacation) {
        return ResponseEntity.ok(service.create(vacation));
    }

    @PutMapping("reject")
    public ResponseEntity<?> reject(@RequestBody VacationUpdateDto vacationUpdateDto) {
        return ResponseEntity.ok(service.reject(vacationUpdateDto));
    }

    @PutMapping("approve")
    public ResponseEntity<Vacation> approve(@RequestBody VacationUpdateDto vacationUpdateDto) {
        return ResponseEntity.ok(service.approve(vacationUpdateDto));
    }

    @PutMapping("{vacationId}/changeStatus")
    public ResponseEntity<Vacation> changeStatus(@PathVariable("vacationId") Long vacationId, VacationStatus fromStatus,  VacationStatus toStatus) {
        return ResponseEntity.ok(service.changeStatus(vacationId, fromStatus, toStatus));
    }

    @GetMapping("{status}")
    public ResponseEntity<List<Vacation>> listByStatus(@PathVariable("status") VacationStatus vacationStatus) {
        return ResponseEntity.ok(service.findByStatus(vacationStatus));
    }

    @GetMapping("")
    public ResponseEntity<List<Vacation>> listAll() {
        return ResponseEntity.ok(service.findMyVacations());
    }

    @GetMapping("approval-required")
    public ResponseEntity<List<Vacation>> listApprovalsRequired() {
        return ResponseEntity.ok(service.findVacationForMyApproval());
    }

    @GetMapping("approval-done")
    public ResponseEntity<List<Vacation>> listApprovedVacations() {
        return ResponseEntity.ok(service.findVacationApprovedByMe());
    }

}
