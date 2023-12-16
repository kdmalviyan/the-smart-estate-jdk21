package com.sfd.thesmartestate.entities;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author kuldeep
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TenantService {
    private final TenantRepository repository;
    public Tenant createTenant(final Tenant tenant) {
        Optional<Tenant> tenantExists = repository.findByOrganizationId(tenant.getOrganizationId());
        if(tenantExists.isEmpty()){
            tenant.setOrganizationId(TenantHelper.createTenantOrganizationId(tenant));
            return repository.save(tenant);
        }
        return tenantExists.get();
    }

    public Iterable<Tenant> fetchALl() {
        return repository.findAll();
    }

    public Tenant findByOrgId(String orgId) {
        return repository.findByOrganizationId(orgId).orElse(null);
    }
}
