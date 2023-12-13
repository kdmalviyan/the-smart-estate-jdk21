package com.sfd.thesmartestate.multitenancy;

import com.sfd.thesmartestate.multitenancy.tenants.Tenant;
import com.sfd.thesmartestate.multitenancy.tenants.TenantRoutingDataSource;
import com.sfd.thesmartestate.multitenancy.tenants.aws.TenantAWSService;
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
    private final TenantAWSService tenantAWSService;
    private final ApplicationContext applicationContext;
    private static final Map<Object, Object> targetDataSources = new HashMap<>();
    public DataSourceConfig(final TenantAWSService tenantAWSService,
                            final ApplicationContext applicationContext) {
        this.tenantAWSService = tenantAWSService;
        this.applicationContext = applicationContext;
    }

    @Bean
    public DataSource dataSource() {
        List<Tenant> tenants = tenantAWSService.loadTenants();
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
        return DataSourceBuilder.
                create()
                .password(tenant.getDbPassword())
                .username(tenant.getDbUser())
                .url(tenant.getConnectionString())
                .driverClassName(tenant.getDatasourceDriverClassName())
                .build();
    }

    public void refreshDataSource() {
        AbstractRoutingDataSource abstractRoutingDataSource
                = applicationContext.getBean(AbstractRoutingDataSource.class);
        for (Tenant tenant : TenantCache.list()) {
            DataSource dataSource = createDataSource(tenant);
            targetDataSources.put(tenant.getOrganizationId(), dataSource);
        }
        abstractRoutingDataSource.setTargetDataSources(targetDataSources);
        abstractRoutingDataSource.afterPropertiesSet();
        System.out.println(abstractRoutingDataSource.getResolvedDataSources());
    }
}


