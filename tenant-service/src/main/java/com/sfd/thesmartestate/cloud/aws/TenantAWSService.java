package com.sfd.thesmartestate.cloud.aws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfd.thesmartestate.databaseconfig.MySqlSchemaGenerator;
import com.sfd.thesmartestate.entities.Tenant;
import com.sfd.thesmartestate.entities.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author kuldeep
 */
@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(
        value="spring.profiles.active",
        havingValue = "docker")
public class TenantAWSService implements TenantDatabaseService {
    private final TenantService tenantService;
    @Value("${tenant.filepath}")
    private String tenantsJsonPath;
    @Value("${tenant.default.aws.bucket}")
    private String defaultAwsBucketName;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final S3CloudService fileService;
    private final MySqlSchemaGenerator mySqlSchemaGenerator;

    @Override
    public void loadTenants() {
        final List<Tenant> tenants;
        try {
            Resource resource = fileService.download(tenantsJsonPath, defaultAwsBucketName);
            linkedHashMapToTenantConfigInfo(objectMapper.readValue(resource.getInputStream(), List.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void linkedHashMapToTenantConfigInfo(List list) {
        for (Object object : list) {
            LinkedHashMap<String, Object> map = (LinkedHashMap) object;
            Tenant tenant = Tenant.Builder.build(map);
            log.info("Loading Tenant: " + tenant);
            mySqlSchemaGenerator.generate(tenant);
            tenant = tenantService.createTenant(tenant);
            log.info(String.format("Tenant created. Name %s, OrgId %s",
                    tenant.getOrganizationName(), tenant.getOrganizationId()));
        }
    }
}
