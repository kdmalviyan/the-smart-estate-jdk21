package com.sfd.thesmartestate.uipermissions;

import com.sfd.thesmartestate.common.services.RoleService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@Slf4j
@Transactional
@Configuration
public class PermissionInitializer {
    /**
     * HOME("Home", "Home"),
     * USER_MANAGEMENT("User Management", "User Management"),
     * LEAD_MANAGEMENT("Lead Management", "Lead Management"),
     * PROJECT_MANAGEMENT("Project Management", "Project Management"),
     * TEAM_MANAGEMENT("Team Management", "Team Management"),
     * METADATA_MANAGEMENT("Metadata Management", "Metadata Management");
     * VACATION_MANAGEMENT("Vacation Management", "Vacation Management");
     * RAW_LEAD("raw Management", "raw Management");
     */

    private final PermissionTabService permissionTabService;
    private final RoleService roleService;

    public PermissionInitializer(PermissionTabService permissionTabService, RoleService roleService) {
        this.permissionTabService = permissionTabService;
        this.roleService = roleService;
        createUIPermissions();
        initializePermissions();
    }

    // Mandatory Tabs, create other tabs from metadata UI
    private void createUIPermissions() {
        PermissionTab home = permissionTabService.findByPath("/dashboard/main");
        if (Objects.isNull(home)) {
            List<PermissionTab> permissionTabs = List.of(
                    new PermissionTab()
                            .build("/dashboard/main",
                                    "Dashboard",
                                    1,
                                    "dashboard",
                                    "monitor",
                                    "",
                                    false,
                                    Set.of(),
                                    true),
                    new PermissionTab().build(
                            "",
                            "Meta Data",
                            100,
                            "metaData",
                            "database",
                            "menu-toggle",
                            false,
                            Set.of(
                                    new PermissionTab().build(
                                            "/metaData/lead-source",
                                            "Lead Source",
                                            101,
                                            "lead-source",
                                            "",
                                            "ml-menu",
                                            false,
                                            Set.of(),
                                            false),
                                    new PermissionTab().build(
                                            "/metaData/ui-permissions",
                                            "UI Permissions",
                                            102,
                                            "ui-permissions",
                                            "",
                                            "ml-menu",
                                            false,
                                            Set.of(),
                                            false),
                                    new PermissionTab().build(
                                            "/metaData/ui-tabs",
                                            "UI Tabs",
                                            103,
                                            "ui-tabs",
                                            "",
                                            "ml-menu",
                                            false,
                                            Set.of(),
                                            false),
                                    new PermissionTab().build(
                                            "/metaData/lead-inventory",
                                            "Lead inventory",
                                            104,
                                            "lead-inventory",
                                            "",
                                            "ml-menu",
                                            false,
                                            Set.of(),
                                            false),
                                    new PermissionTab().build(
                                            "/metaData/lead-status",
                                            "Lead Status",
                                            105,
                                            "lead-status",
                                            "",
                                            "ml-menu",
                                            false,
                                            Set.of(),
                                            false),
                                    new PermissionTab().build(
                                            "/metaData/lead-type",
                                            "Lead Type",
                                            106,
                                            "lead-type",
                                            "",
                                            "ml-menu",
                                            false,
                                            Set.of(),
                                            false),
                                    new PermissionTab().build(
                                            "/metaData/deactivate-reason",
                                            "Deactivate Reasons",
                                            107,
                                            "deactivate-type",
                                            "",
                                            "ml-menu",
                                            false,
                                            Set.of(),
                                            false),
                                    new PermissionTab().build(
                                            "/metaData/thirdParty-integration",
                                            "ThirdParty Integration",
                                            108,
                                            "thirdParty-integration",
                                            "",
                                            "ml-menu",
                                            false,
                                            Set.of(),
                                            false)
                            ),
                            true),
                    new PermissionTab()
                            .build("/user-management",
                                    "User Management",
                                    2,
                                    "user-management",
                                    "user",
                                    "",
                                    false,
                                    Set.of(),
                                    true),
                    new PermissionTab()
                            .build("/follow-up",
                                    "Follow Up",
                                    3,
                                    "follow-up",
                                    "watch",
                                    "",
                                    false,
                                    Set.of(),
                                    true),
                    new PermissionTab()
                            .build("/lead-management",
                                    "Lead Management",
                                    4,
                                    "lead-management",
                                    "trending-up",
                                    "",
                                    false,
                                    Set.of(),
                                    true),
                    new PermissionTab()
                            .build("/project-management",
                                    "Project Management",
                                    5,
                                    "project-management",
                                    "codesandbox",
                                    "",
                                    false,
                                    Set.of(),
                                    true),
                    new PermissionTab()
                            .build("/team-management",
                                    "Team Management",
                                    6,
                                    "team-management",
                                    "employee",
                                    "",
                                    false,
                                    Set.of(),
                                    true),
                    new PermissionTab()
                            .build("/booking-management",
                                    "Booking Management",
                                    4,
                                    "booking-management",
                                    "book-open",
                                    "",
                                    false,
                                    Set.of(),
                                    true),
                    new PermissionTab()
                            .build("/targets",
                                    "Targets",
                                    4,
                                    "targets",
                                    "target",
                                    "",
                                    false,
                                    Set.of(),
                                    true),
                    new PermissionTab()
                            .build("/raw-lead-management",
                                    "Raw Leads",
                                    4,
                                    "raw-leads",
                                    "trending-down",
                                    "",
                                    false,
                                    Set.of(),
                                    true),
                    new PermissionTab()
                            .build("/vacation-management",
                                    "Vacation Management",
                                    4,
                                    "Vacation",
                                    "send",
                                    "",
                                    false,
                                    Set.of(),
                                    true)
                    );
            permissionTabService.saveAll(permissionTabs);
        }
    }


    public void initializePermissions() {
        // Initialize Superadmin permissions, he has all the access
        List<PermissionTab> permissionTabs = permissionTabService.findAll();
        roleService.assignPermissionsToRole("ROLE_SUPERADMIN", permissionTabs);
        // Initialize Superadmin permissions, he has all the access (TODO: can be reconsider later)
        roleService.assignPermissionsToRole("ROLE_ADMIN", permissionTabs);
    }


}
