package com.sfd.thesmartestate.databaseconfig;

import com.sfd.thesmartestate.entities.Tenant;

/**
 * @author kuldeep
 */
public interface SchemaGenerator {
    void generate(Tenant tenant);
}
