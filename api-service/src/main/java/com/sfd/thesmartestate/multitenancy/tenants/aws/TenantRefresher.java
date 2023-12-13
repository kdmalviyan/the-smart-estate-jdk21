package com.sfd.thesmartestate.multitenancy.tenants.aws;

import com.sfd.thesmartestate.multitenancy.DataSourceConfig;
import com.sfd.thesmartestate.multitenancy.tenants.Tenant;
import com.sfd.thesmartestate.multitenancy.tenants.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author kuldeep
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TenantRefresher {
    private final TenantService tenantService;
    private final TenantAWSService tenantAWSService;

    private final DataSourceConfig dataSourceConfig;

    @Scheduled(cron = "0 0 0 * * *")
    public void refreshTenantsConfig() {
        tenantAWSService.loadTenants();
        Iterable<Tenant> updatedTenants = tenantService.createTenant(TenantCache.list());
        updatedTenants.forEach(t -> log.info("Tenant config refreshed: " + t.getOrganizationId()));
        dataSourceConfig.refreshDataSource();

    }
}
