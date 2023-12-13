package com.sfd.thesmartestate.multitenancy.tenants;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * @author kuldeep
 */
@Table(name = "tb_tenants")
@Data
@Entity
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String organizationName;
    private String organizationId;
    private String dbUser;
    private String dbPassword;
    private String connectionString;
    private boolean isActive;
    private boolean isSuspended;
    private LocalDateTime suspendedAt;
    private String suspensionReason;
    private LocalDateTime registrationDate;
    @OneToOne(targetEntity = Subscription.class, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name="subscription_id")
    private Subscription subscription;
    private String datasourceDriverClassName;


    public static class Builder {
        public static Tenant build(Map<String, Object> map) {
            Tenant tenant = new Tenant();
            tenant.setRegistrationDate(LocalDateTime.now());
            tenant.setOrganizationName(Objects.toString(map.get("name")));
            tenant.setDbUser(Objects.toString(map.get("datasourceUsername")));
            tenant.setDbPassword(Objects.toString(map.get("datasourcePassword")));
            tenant.setDatasourceDriverClassName(Objects.toString(map.get("datasourceDriverClassName")));
            tenant.setConnectionString(Objects.toString(map.get("datasourceUrl")));
            tenant.setActive(true);
            tenant.setSuspended(false);
            tenant.setRegistrationDate(LocalDateTime.now());
            tenant.setSubscription(createSubscription(map));
            tenant.setOrganizationId(TenantHelper.createTenantOrganizationId(tenant));
            return tenant;
        }

        //TODO: Change this for actual subscription info
        private static Subscription createSubscription(Map<String, Object> map) {
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
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tenant tenant = (Tenant) o;

        if (!getOrganizationName().equals(tenant.getOrganizationName())) return false;
        return getOrganizationId().equals(tenant.getOrganizationId());
    }

    @Override
    public int hashCode() {
        int result = getOrganizationName().hashCode();
        result = 31 * result + getOrganizationId().hashCode();
        return result;
    }
}
