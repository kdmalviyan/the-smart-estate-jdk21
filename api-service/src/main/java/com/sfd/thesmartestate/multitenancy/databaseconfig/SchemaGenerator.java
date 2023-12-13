package com.sfd.thesmartestate.multitenancy.databaseconfig;

import com.sfd.thesmartestate.multitenancy.tenants.Tenant;

/**
 * @author kuldeep
 */
public interface SchemaGenerator {
    void generate(Tenant tenant);
}
