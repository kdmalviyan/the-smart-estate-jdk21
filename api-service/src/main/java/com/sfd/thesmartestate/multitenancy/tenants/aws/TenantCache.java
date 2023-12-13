package com.sfd.thesmartestate.multitenancy.tenants.aws;

import com.sfd.thesmartestate.multitenancy.tenants.Tenant;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kuldeep
 */

public class TenantCache {
    private static final Map<String, Tenant> TENANTS = new HashMap<>();
    public static void cacheTenant(Tenant tenant) {
        TENANTS.put(tenant.getOrganizationId(), tenant);
    }

    public static Tenant getTenant(String orgId) {
        return TENANTS.get(orgId);
    }
    public static Collection<Tenant> list() {
        return TENANTS.values();
    }
}
