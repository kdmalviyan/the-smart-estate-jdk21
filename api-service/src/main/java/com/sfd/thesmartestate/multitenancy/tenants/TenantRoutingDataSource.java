package com.sfd.thesmartestate.multitenancy.tenants;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

/**
 * @author kuldeep
 */
public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return StringUtils.hasText(TenantContext.getTenantId()) ? TenantContext.getTenantId() : "SFD";
    }
}

