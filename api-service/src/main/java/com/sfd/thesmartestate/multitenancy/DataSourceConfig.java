package com.sfd.thesmartestate.multitenancy;

import com.sfd.thesmartestate.multitenancy.tenants.Tenant;
import com.sfd.thesmartestate.multitenancy.tenants.TenantClientService;
import com.sfd.thesmartestate.multitenancy.tenants.TenantRoutingDataSource;
import com.sfd.thesmartestate.multitenancy.tenants.aws.TenantCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class DataSourceConfig {
    private final ApplicationContext applicationContext;
    private final TenantClientService tenantClientService;
    private static final Map<Object, Object> targetDataSources = new HashMap<>();
    public DataSourceConfig(final ApplicationContext applicationContext,
                            final TenantClientService tenantClientService) {
        this.applicationContext = applicationContext;
        this.tenantClientService = tenantClientService;
    }

    @Bean
    public DataSource dataSource() {
        List<Tenant> tenants = tenantClientService.fetchAllTenants();
        createDatasourceMap(tenants);
        TenantRoutingDataSource routingDataSource = new TenantRoutingDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        return routingDataSource;
    }

    private void createDatasourceMap(List<Tenant> tenants) {
        tenants.forEach(tenant -> targetDataSources.put(tenant.getOrganizationId(),
                createDataSource(tenant)));
    }

    private DataSource createDataSource(final Tenant tenant) {
        TenantCache.cacheTenant(tenant);
        return DataSourceBuilder.
                create()
                .password(tenant.getDbPassword())
                .username(tenant.getDbUser())
                .url(tenant.getConnectionString())
                .driverClassName(tenant.getDatasourceDriverClassName())
                .build();
    }

    public void refreshDataSource(List<Tenant> tenants) {
        AbstractRoutingDataSource abstractRoutingDataSource
                = applicationContext.getBean(AbstractRoutingDataSource.class);
        for (Tenant tenant : tenants) {
            DataSource dataSource = createDataSource(tenant);
            targetDataSources.put(tenant.getOrganizationId(), dataSource);
        }
        abstractRoutingDataSource.setTargetDataSources(targetDataSources);
        abstractRoutingDataSource.afterPropertiesSet();
    }
}


