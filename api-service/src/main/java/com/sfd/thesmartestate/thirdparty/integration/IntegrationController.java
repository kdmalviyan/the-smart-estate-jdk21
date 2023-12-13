package com.sfd.thesmartestate.thirdparty.integration;

import com.sfd.thesmartestate.thirdparty.integration.remoteleads.RemoteLead;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("thirdparty/integration")
@RequiredArgsConstructor
@CrossOrigin("*")
public class IntegrationController {
    private final IntegrationService leadService;
    @PostMapping("lead/create")
    public ResponseEntity<?> createRemoteLea(@RequestBody List<RemoteLead> remoteLeads) {
        remoteLeads.forEach(leadService::createRemoteLead);
        return ResponseEntity.ok("Data Successfully pushed");
    }
}
