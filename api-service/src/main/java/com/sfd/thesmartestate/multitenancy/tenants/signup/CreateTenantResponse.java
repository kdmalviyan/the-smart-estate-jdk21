package com.sfd.thesmartestate.multitenancy.tenants.signup;

import com.sfd.thesmartestate.multitenancy.tenants.Tenant;
import lombok.Builder;

/**
 * @author kuldeep
 */
@Builder(setterPrefix = "with")
public class CreateTenantResponse {
    private String username;
    private String tenantName;
    private String orgId;
    public static CreateTenantResponse buildFromTenant(Tenant tenant) {
        return CreateTenantResponse.builder()
                .build();
    }
}
