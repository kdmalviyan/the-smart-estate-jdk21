package com.sfd.thesmartestate.cloud.aws;

import com.sfd.thesmartestate.entities.Tenant;
import com.sfd.thesmartestate.entities.TenantService;
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
    private final TenantDatabaseService tenantDatabaseService;
    private final TenantService tenantService;

    @Scheduled(cron = "0 */30 * * * *")
    public void refreshTenantsConfig() {
        tenantDatabaseService.loadTenants();
    }
}
