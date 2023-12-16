package com.sfd.thesmartestate.multitenancy.databaseconfig;

import com.sfd.thesmartestate.common.services.FileService;
import com.sfd.thesmartestate.multitenancy.tenants.Tenant;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author kuldeep
 */
@Component
@Slf4j
public class MySqlSchemaGenerator implements SchemaGenerator {
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;

    @Value("${tenant.tenantsSchemaPath}")
    private String tenantsSchemaPath;
    @Value("${tenant.default.aws.bucket}")
    private String defaultAwsBucketName;
    @Value("${tenant.run-db-scripts}")
    private boolean runDbScripts;

    public MySqlSchemaGenerator(final FileService fileService,
                                final PasswordEncoder passwordEncoder) {
        this.fileService = fileService;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void generate(Tenant tenant) {
        log.info("Schema: Generating database for tenant " + tenant.getOrganizationId());
        Connection connection = null;
        try {
            log.info("Getting database connection.");
            connection = ConnectionManager.getConnection(tenant);
            if(!Objects.equals(tenant.getOrganizationId().toLowerCase(), "sfd")
                    && !isDatabaseAlreadyExists(connection)) {
                log.info("Importing database schema and initial data feed.");
                executeDatabaseScript(connection);
                createTenantSuperadmin(connection, tenant);
                log.info("Database is ready to use.");
            }
        } catch (SQLException | IOException e) {
            log.error("ALERT: Unable to configure database for " + tenant.getOrganizationId() + ". Manual intervene required");
        } finally {
           if(Objects.nonNull(connection)) {
               ConnectionManager.closeConnection(connection);
           }
        }
    }

    private void createTenantSuperadmin(Connection connection, Tenant tenant) throws SQLException {
        final String USER_INSERT = "INSERT INTO `tb_employees` " +
                "VALUES (1,NULL,NULL,'" +
                LocalDateTime.now() +  "',NULL," +
                "'super@dupar@gmail.com',true," +
                "'Male',false,true,NULL,NULL,'" +
                tenant.getOrganizationId()+"'," +
                "'" + passwordEncoder.encode("Password@1") + "'" +
                ",NULL,NULL," +
                "'" + tenant.getOrganizationId() + "_admin',NULL,NULL,NULL)";
        final String USER_ROLES_INSERT = "INSERT INTO `tb_employees_roles` VALUES (1,3)";
        PreparedStatement userStatement = connection.prepareStatement(USER_INSERT);
        userStatement.executeUpdate();
        PreparedStatement rolesStatement = connection.prepareStatement(USER_ROLES_INSERT);
        rolesStatement.executeUpdate();
        connection.commit();
    }

    private boolean isDatabaseAlreadyExists(Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM tb_employees");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String username = resultSet.getString("username");
            return Objects.nonNull(username);
        } catch (SQLException e) {
            return false;
        }
    }

    private void executeDatabaseScript(Connection connection) throws IOException {
        if(runDbScripts) {
            Resource resource = fileService.download(tenantsSchemaPath, defaultAwsBucketName);
            ScriptRunner sr = new ScriptRunner(connection);
            Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            sr.runScript(reader);
        }
    }
}
