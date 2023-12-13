package com.sfd.thesmartestate.multitenancy.tenants;

import com.sfd.thesmartestate.multitenancy.tenants.signup.CreateTenantResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author kuldeep
 */
@RestController
@RequestMapping(value = "tenant")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TenantController {
    private final TenantService tenantService;

    @PostMapping
    public ResponseEntity<CreateTenantResponse> create(@RequestBody Tenant tenant) {
        return ResponseEntity.ok(CreateTenantResponse.buildFromTenant(tenantService.createTenant(tenant)));
    }
}
