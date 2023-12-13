package com.sfd.thesmartestate.adhoc.folloups;/*
package com.ccs.realestate.adhoc.folloups;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/adhoc/lead/followup")
@RequiredArgsConstructor
@Transactional
@Slf4j
@CrossOrigin("*")
public class FollowupRemappingController {
    private final FollowupRemappingRepository repository;
    @PutMapping("")
    public ResponseEntity<?> updateInventorySize() {
        new Thread(() -> repository.updateFollowupMappingToLeads(UUID.randomUUID().toString())).start();
        return ResponseEntity.ok("Request submitted: Reference id " + UUID.randomUUID());
    }
}
*/
