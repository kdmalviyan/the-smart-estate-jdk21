package com.sfd.thesmartestate.adhoc;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/adhoc/lead")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LeadInventoryUpdateController {

    private final LeadInventoryUpdateService leadInventoryUpdateService;

    @PutMapping("inventory")
    @Secured("ROLE_SUPERADMIN")
    public ResponseEntity<?> updateInventorySize(@RequestBody UpdateInventorySizeDTO updateInventorySizeDTO) {
        new Thread(() -> leadInventoryUpdateService.updateLeadInventorySize(updateInventorySizeDTO, UUID.randomUUID().toString())).start();
        return ResponseEntity.ok("Request submitted: Reference id " + UUID.randomUUID());
    }

    @PutMapping("duplicate")
    public ResponseEntity<?> updateDuplicateLeads() {
        System.out.println("IN");
        leadInventoryUpdateService.updateDuplicateLead(UUID.randomUUID().toString());
        //new Thread(() -> ).start();
        return ResponseEntity.ok("Request submitted: Reference id " + UUID.randomUUID());
    }

}

@Data
class UpdateInventorySizeDTO {
    private List<String> excludeProject;
}

