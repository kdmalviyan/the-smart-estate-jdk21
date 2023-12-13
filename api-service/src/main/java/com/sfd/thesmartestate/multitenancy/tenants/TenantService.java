package com.sfd.thesmartestate.multitenancy.tenants;

import com.sfd.thesmartestate.multitenancy.tenants.aws.TenantCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * @author kuldeep
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TenantService {
private final TenantRepository repository;

    public Iterable<Tenant> createTenant(Collection<Tenant> list) {
        return repository.saveAll(list);
    }
    public Tenant createTenant(final Tenant tenant) {
        Optional<Tenant> tenantExists = repository.findByOrganizationId(tenant.getOrganizationId());
        if(!tenantExists.isPresent()){
            tenant.setOrganizationId(TenantHelper.createTenantOrganizationId(tenant));
            return repository.save(tenant);
        }
        return tenantExists.get();
    }

    public boolean isTenantValid(String tenantId) {
        return Objects.nonNull(TenantCache.getTenant(tenantId));
    }
}
