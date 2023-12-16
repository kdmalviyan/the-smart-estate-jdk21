package com.sfd.thesmartestate.cloud.aws;

import com.sfd.thesmartestate.databaseconfig.MySqlSchemaGenerator;
import com.sfd.thesmartestate.entities.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author kuldeep
 */

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        value="spring.profiles.active",
        havingValue = "dev", matchIfMissing = true)
@Log
public class TenantLocalService implements TenantDatabaseService {
    private final TenantService tenantService;
    private final List<Tenant> tenants = List.of(
            createLocalTenant("SFD", "real_estate_db_sfd"),
            createLocalTenant("L1", "real_estate_db_l1"),
            createLocalTenant("L2", "real_estate_db_l2"),
            createLocalTenant("L3", "real_estate_db_l3")
    );

    private Tenant createLocalTenant(String name, String databaseName) {
        Tenant tenant = new Tenant();
        tenant.setRegistrationDate(LocalDateTime.now());
        tenant.setOrganizationName(name);
        tenant.setDbUser("root");
        tenant.setDbPassword("Admin@123");
        tenant.setDatasourceDriverClassName("com.mysql.cj.jdbc.Driver");
        tenant.setConnectionString(String.format("jdbc:mysql://localhost:3306/%s?createDatabaseIfNotExist=true", databaseName));
        tenant.setActive(true);
        tenant.setSuspended(false);
        tenant.setRegistrationDate(LocalDateTime.now());
        tenant.setSubscription(createSubscription());
        tenant.setOrganizationId(TenantHelper.createTenantOrganizationId(tenant));
        return tenant;
    }

    private static Subscription createSubscription() {
        Subscription subscription = new Subscription();
        subscription.setActive(true);
        subscription.setExpired(false);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusYears(1));
        subscription.setNumberOfActiveUsers(0);
        subscription.setNumberOfRegisteredUsers(0);
        subscription.setNumberOfUsersAllowed(100);
        return subscription;
    }

    private final MySqlSchemaGenerator mySqlSchemaGenerator;

    @Override
    public void loadTenants() {
        for(Tenant tenant : tenants) {
            log.info("Loading Tenant: " + tenant);
            mySqlSchemaGenerator.generate(tenant);
            tenant = tenantService.createTenant(tenant);
            log.info(String.format("Tenant created. Name %s, OrgId %s",
                    tenant.getOrganizationName(), tenant.getOrganizationId()));
        }
    }
}
