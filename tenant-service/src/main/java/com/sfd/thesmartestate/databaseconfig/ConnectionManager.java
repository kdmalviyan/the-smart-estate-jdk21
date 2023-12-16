package com.sfd.thesmartestate.databaseconfig;

import com.sfd.thesmartestate.entities.Tenant;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author kuldeep
 */
@Slf4j
public class ConnectionManager {
    public static Connection getConnection(Tenant tenant) throws SQLException {
        return DriverManager.getConnection(tenant.getConnectionString(), tenant.getDbUser(), tenant.getDbPassword());
    }

    public static void closeConnection(Connection connection) {
        try {
            log.info("Closing database connection.");
            connection.close();
            log.info("Database connection closed");
        } catch (SQLException e) {
            log.warn("ALERT: Unable to close database connection, attention required.\n" + e.getMessage());
        }
    }
}
