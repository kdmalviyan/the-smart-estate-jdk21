package com.sfd.thesmartestate.multitenancy.tenants;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * @author kuldeep
 */
@Data
public class Tenant {
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
    private Subscription subscription;
    private String datasourceDriverClassName;

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
