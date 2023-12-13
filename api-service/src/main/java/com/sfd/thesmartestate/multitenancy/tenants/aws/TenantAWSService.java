package com.sfd.thesmartestate.multitenancy.tenants.aws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfd.thesmartestate.common.services.FileService;
import com.sfd.thesmartestate.multitenancy.databaseconfig.MySqlSchemaGenerator;
import com.sfd.thesmartestate.multitenancy.tenants.Tenant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author kuldeep
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TenantAWSService {
    @Value("${tenant.filepath}")
    private String tenantsJsonPath;
    @Value("${tenant.default.aws.bucket}")
    private String defaultAwsBucketName;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final FileService fileService;
    private final MySqlSchemaGenerator mySqlSchemaGenerator;
    public List<Tenant> loadTenants() {
        final List<Tenant> tenants;
        try {
            Resource resource = fileService.download(tenantsJsonPath, defaultAwsBucketName);
            tenants = linkedHashMapToTenantConfigInfo(objectMapper.readValue(resource.getInputStream(), List.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tenants;
    }

    private List<Tenant> linkedHashMapToTenantConfigInfo(List list) {
        List<Tenant> tenantList = new ArrayList<>();
        for (Object object : list) {
            LinkedHashMap<String, Object> map = (LinkedHashMap) object;
            Tenant tenant = Tenant.Builder.build(map);
            log.info("Loading Tenant: " + tenant);
            TenantCache.cacheTenant(tenant);
            mySqlSchemaGenerator.generate(tenant);
            tenantList.add(tenant);
        }
        return tenantList;
    }
}
