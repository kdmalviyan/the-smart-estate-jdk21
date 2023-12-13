package com.sfd.thesmartestate.lms.calls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@Slf4j
@RequestMapping(value = "call")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CallController {
    private final CallService callService;

    @PostMapping("/{leadId}")
    public ResponseEntity<Call> create(@RequestBody Call call, @PathVariable("leadId") Long leadId) {
        return ResponseEntity.ok(callService.create(call, leadId));
    }

    @GetMapping("/{leadId}")
    public ResponseEntity<Set<Call>> getAllByLeadId(@PathVariable("leadId") Long leadId) {
        return ResponseEntity.ok(callService.getAllByLeadId(leadId));
    }

}
