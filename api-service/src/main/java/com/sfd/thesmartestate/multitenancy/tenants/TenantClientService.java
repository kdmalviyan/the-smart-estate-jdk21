package com.sfd.thesmartestate.multitenancy.tenants;

import com.sfd.thesmartestate.multitenancy.tenants.aws.TenantCache;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author kuldeep
 */
@Service
public class TenantClientService {

    public List<Tenant> fetchAllTenants() {
        List<Tenant> tenants = List.of(createLocalTenant());
        TenantCache.cacheTenant(tenants);
        return tenants; //TODO: Replace this with RestTemplate call
    }

    public boolean isTenantValid(String tenantId) {
        return TenantCache.list().stream()
                .anyMatch(tenant -> Objects.equals(tenant.getOrganizationId(), tenantId));
    }


    // For testing only
    private Tenant createLocalTenant() {
        Tenant tenant = new Tenant();
        tenant.setActive(true);
        tenant.setSuspended(false);
        tenant.setOrganizationId("SFD");
        tenant.setDbPassword("Admin@123");
        tenant.setDbUser("root");
        tenant.setConnectionString("jdbc:mysql://localhost:3306/smart_estate_db_sfd?createDatabaseIfNotExist=true");
        tenant.setDatasourceDriverClassName("com.mysql.cj.jdbc.Driver");
        tenant.setRegistrationDate(LocalDateTime.now());
        Subscription subscription = new Subscription();
        subscription.setExpired(false);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusYears(2));
        subscription.setNumberOfActiveUsers(10);
        subscription.setNumberOfRegisteredUsers(10);
        subscription.setNumberOfUsersAllowed(100);
        subscription.setNumberOfRegisteredUsers(30);
        subscription.setActive(true);
        tenant.setSubscription(subscription);
        return tenant;
    }
}
