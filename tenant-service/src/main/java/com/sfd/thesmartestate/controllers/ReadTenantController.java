package com.sfd.thesmartestate.controllers;

import com.sfd.thesmartestate.entities.Tenant;
import com.sfd.thesmartestate.entities.TenantRepository;
import com.sfd.thesmartestate.entities.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author kuldeep
 */

@RestController
@Slf4j
@RequiredArgsConstructor
public class ReadTenantController {
    private final TenantService service;
    @GetMapping
    public ResponseEntity<Iterable<Tenant>> fetchALl() {
        return ResponseEntity.ok(service.fetchALl());
    }
    @GetMapping("{tenantId}")
    public ResponseEntity<Tenant> findByOrgId(@PathVariable String orgId) {
        return ResponseEntity.ok(service.findByOrgId(orgId));
    }
}
