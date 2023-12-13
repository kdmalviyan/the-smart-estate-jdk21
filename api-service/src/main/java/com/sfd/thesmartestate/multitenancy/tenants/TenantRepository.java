package com.sfd.thesmartestate.multitenancy.tenants;


import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author kuldeep
 */
public interface TenantRepository extends CrudRepository<Tenant, Long> {
    Optional<Tenant> findByOrganizationId(String orgId);
}
