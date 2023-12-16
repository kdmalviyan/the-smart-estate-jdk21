package com.sfd.thesmartestate.multitenancy.tenants.aws;

import com.sfd.thesmartestate.multitenancy.DataSourceConfig;
import com.sfd.thesmartestate.multitenancy.tenants.Tenant;
import com.sfd.thesmartestate.multitenancy.tenants.TenantClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author kuldeep
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TenantRefresher {
    private final TenantClientService tenantClientService;

    private final DataSourceConfig dataSourceConfig;

    @Scheduled(cron = "0 0 0 * * *")
    public void refreshTenantsConfig() {
        List<Tenant> tenants = tenantClientService.fetchAllTenants();
        dataSourceConfig.refreshDataSource(tenants);

    }
}
