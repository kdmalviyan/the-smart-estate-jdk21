package com.sfd.thesmartestate.entities;

/**
 * @author kuldeep
 */
public class TenantHelper {
    public static String createTenantOrganizationId(Tenant tenant) {
        if(tenant.getOrganizationName().length() < 8) {
            return tenant.getOrganizationName();
        }
        return tenant.getOrganizationName()
                .replaceAll(" ", "").toUpperCase().substring(0, 8);
    }
}
