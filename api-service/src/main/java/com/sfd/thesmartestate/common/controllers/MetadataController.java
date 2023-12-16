package com.sfd.thesmartestate.common.controllers;

import com.sfd.thesmartestate.common.services.MetadataService;
import com.sfd.thesmartestate.uipermissions.PermissionTab;
import com.sfd.thesmartestate.employee.services.EmployeeService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "metadata")
@RequiredArgsConstructor
@Slf4j
@SuppressFBWarnings("EI_EXPOSE_REP")
@CrossOrigin("*")
public class MetadataController {

    private final MetadataService metadataService;
    private final EmployeeService employeeService;

    @GetMapping("")
    private ResponseEntity<?> loadMetadata() {
        log.info("Loading metadata");
        JSONObject response = new JSONObject();
        response.put("loggedInUser", metadataService.formatLoggedInUserResponse());
        response.put("leadStatus", metadataService.fetchAllLeadStatus());
        response.put("leadSource", metadataService.fetchAllLeadSource());
        response.put("roles", metadataService.fetchAllRoles());
        response.put("leadType", metadataService.fetchAllLeadType());
        response.put("leadInventorySizes", metadataService.fetchAllLeadInventorySize());
        response.put("employee", metadataService.fetchAllUsers());
        response.put("deactivationReason", metadataService.fetchAllDeactivationReason());
        response.put("projects", metadataService.fetchAllProjects());
        response.put("inventoryStatus", metadataService.fetchAllInventoryStatus());
        log.info("Metadata loaded");
        return ResponseEntity.ok(response);
    }

    @GetMapping("sidemenuPermission")
    public ResponseEntity<List<PermissionTab>> getUiPermissions() {
        List<PermissionTab> permissionTabs = new ArrayList<>();
        employeeService.findLoggedInEmployee()
                .getRoles()
                .forEach(role -> permissionTabs.addAll(role.getUiPermissions().stream()
                        .filter(PermissionTab::isTopMenu)
                        .sorted(Comparator.comparing(PermissionTab::getIndex))
                        .toList()));
        return ResponseEntity.ok(permissionTabs);
    }

    @GetMapping("allTabs")
    public ResponseEntity<List<PermissionTab>> getAllTabs() {
        List<PermissionTab> permissionTabs = new ArrayList<>();
        employeeService.findLoggedInEmployee()
                .getRoles()
                .forEach(role -> permissionTabs.addAll(role.getUiPermissions().stream()
                        .filter(PermissionTab::isTopMenu)
                        .sorted(Comparator.comparing(PermissionTab::getIndex))
                        .toList()));
        return ResponseEntity.ok(permissionTabs);
    }
}
