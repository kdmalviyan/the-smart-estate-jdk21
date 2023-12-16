package com.sfd.thesmartestate.setup;

import com.sfd.thesmartestate.common.Constants;
import com.sfd.thesmartestate.common.entities.Role;
import com.sfd.thesmartestate.common.services.RoleService;
import com.sfd.thesmartestate.lms.entities.*;
import com.sfd.thesmartestate.lms.repositories.*;
import com.sfd.thesmartestate.lms.services.*;
import com.sfd.thesmartestate.projects.entities.InventoryStatus;
import com.sfd.thesmartestate.projects.entities.Project;
import com.sfd.thesmartestate.projects.repositories.InventoryStatusRepository;
import com.sfd.thesmartestate.projects.services.InventoryStatusService;
import com.sfd.thesmartestate.projects.services.ProjectService;
import com.sfd.thesmartestate.users.entities.Employee;
import com.sfd.thesmartestate.users.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Transactional
@Configuration
@Slf4j
public class InitialDataLoader {

    private static final Map<String, String> leadSource = Map.of(
            "FACEBOOK", "Facebook",
            "INSTAGRAM", "Instagram",
            "GOOGLE", "Google",
            "WEBSITE", "Website",
            "IVR", "IVR",
            "WALK_IN", "Walk-in",
            "BROKER_DEALER", "Broker-Dealer",
            "NEWS_PORTALS", "News Portals",
            "NEWS_PAPER_ADVERTISEMENT", "News Paper Advertisement",
            "OUT_DOOR_MEDIA", "Out Door Media");
    private static final Map<String, String> leadType = Map.of(
            "HOT", "Hot",
            "COLD", "Cold",
            "WARM", "Warm");
    private static final Map<String, String> leadStatus = Map.of(
            Constants.ACTIVE, "Active",
            Constants.IN_PROCESS, "In Process",
            Constants.BOOKED, "Booked",
            Constants.DEACTIVE, "Deactive",
            Constants.FOLLOW_UP, "Follow Up",
            Constants.FOLLOW_UP_EXPIRE, "Follow Up Expire",
            Constants.FOLLOW_UP_COMPLETE, "Follow Up Complete");
    private static final Map<String, String> initialRoles = Map.of(
            "ROLE_SUPERADMIN", "Super Admin",
            "ROLE_ADMIN", "Admin",
            "ROLE_BUSINESS_EXECUTIVE", "Business Executive",
            "ROLE_BUSINESS_HEAD", "Business Head",
            "ROLE_BUSINESS_MANAGER", "Business Manager");
    private static final Map<String, String> initialInventorySize = Map.of(
            "4_bhk_+_servant_room", "4 BHK+servant room",
            "4_bhk", "4 BHK",
            "3_bhk", "3 BHK",
            "3_bhk_+_servant_room", "3 BHK+servant room",
            "2_bhk_+_servant_room", "2 BHK+servant room",
            "2_bhk", "2 BHK",
            "penthouse", "Penthouse",
            "Duplex", "Duplex",
            "plot", "Plot",
            "shop", "Shop");

    private static final Map<String, String> inventoryBookingStatus = Map.of(
            Constants.ON_HOLD, "On Hold",
            Constants.BOOKED, "Booked",
            Constants.AVAILABLE, "Available");

    private static final Map<String, String> leadDeactivationReasons = Map.of(
            Constants.BUDGET, "Budget",
            Constants.LOCATION, "Location",
            Constants.READY_TO_MOVE, "Ready To Move",
            Constants.RENT, "Rent");

    final LeadSourceService leadSourceService;
    final LeadStatusService leadStatusService;
    final LeadTypeService leadTypeService;
    final InventoryStatusService inventoryStatusService;
    final LeadInventorySizeService leadInventorySizeService;
    final ProjectService projectService;
    final DeactivationReasonService deactivationReasonService;
    private final RoleService roleService;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final LeadSourceRepository leadSourceRepository;
    private final LeadStatusRepository leadStatusRepository;
    private final LeadTypeRepository leadTypeRepository;
    private final LeadInventorySizeRepository leadInventorySizeRepository;
    private final DeactivationReasonRepository deactivationReasonRepository;
    private final InventoryStatusRepository inventoryStatusRepository;

    public InitialDataLoader(final RoleService roleService,
                             final UserService userService,
                             final BCryptPasswordEncoder bCryptPasswordEncoder,
                             final LeadSourceService leadSourceService,
                             final LeadStatusService leadStatusService,
                             final LeadInventorySizeService leadInventorySizeService,
                             final ProjectService projectService,
                             final LeadTypeService leadTypeService,
                             final InventoryStatusService inventoryStatusService,
                             final DeactivationReasonService deactivationReasonService,
                             final LeadSourceRepository leadSourceRepository,
                             final LeadStatusRepository leadStatusRepository,
                             final LeadTypeRepository leadTypeRepository,
                             final LeadInventorySizeRepository leadInventorySizeRepository,
                             final DeactivationReasonRepository deactivationReasonRepository,
                             final InventoryStatusRepository inventoryStatusRepository) {
        this.roleService = roleService;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.leadSourceService = leadSourceService;
        this.leadStatusService = leadStatusService;
        this.leadTypeService = leadTypeService;
        this.leadInventorySizeService = leadInventorySizeService;
        this.inventoryStatusService = inventoryStatusService;
        this.projectService = projectService;
        this.deactivationReasonService = deactivationReasonService;
        this.leadSourceRepository = leadSourceRepository;
        this.leadStatusRepository = leadStatusRepository;
        this.leadTypeRepository = leadTypeRepository;
        this.leadInventorySizeRepository = leadInventorySizeRepository;
        this.deactivationReasonRepository = deactivationReasonRepository;
        this.inventoryStatusRepository = inventoryStatusRepository;
        loadRoles();
        loadUsers();
        loadLeadSources();
        loadLeadType();
        loadLeadStatus();
        loadLeadInventorySize();
        createBenchProject();
        loadLeadDeactivationReason();
        InventoryBookingStatus();
    }
    private void createBenchProject() {
        Project project = projectService.findByName("Bench");
        if (project == null) {
            Project projectBench = new Project();
            projectBench.setEnabled(true);
            projectBench.setName("Bench");
            projectBench.setAddress("ERP System");
            projectBench.setEmail("bench@softforgedata.com");
            projectService.create(projectBench);
        }
    }

    private void loadUsers() {
        if (userService.count() == 0) {
            Employee superadmin = createUser("superadmin", bCryptPasswordEncoder.encode("Password@1"), "ROLE_SUPERADMIN");
            userService.createUser(superadmin);
        }
    }

    private Employee createUser(String username, String password, String roleName) {
        Employee employee = new Employee();
        employee.setPassword(password);
        employee.setUsername(username);
        employee.getLoginDetails().setPassword(password);
        employee.getLoginDetails().setUsername(username);
        String employeeUniqueId = UUID.randomUUID().toString();
        employee.getLoginDetails().setEmployeeUniqueId(employeeUniqueId);
        employee.setEmployeeUniqueId(employeeUniqueId);
        employee.getLoginDetails().setRoles(Set.of(roleService.findByName(roleName)));
        employee.getLoginDetails().setName("SuperAdmin");
        employee.setName("SuperAdmin");
        employee.setEmail("super@dupar@gmail.com");
        employee.setCreatedAt(LocalDateTime.now());
        employee.setGender("Male");
        employee.setSubordinates(new TreeSet<>());
        employee.setSupervisor(null);
        employee.setActive(true);
        employee.setRoles(Set.of(roleService.findByName(roleName)));
        return employee;
    }

    private void loadRoles() {
        initialRoles.forEach((name, desc) -> {
            if (roleService.findByName(name) == null) {
                Role role = new Role(name);
                role.setDescription(desc);
                roleService.save(role);
            }
        });
    }

    private void loadLeadSources() {
        if (leadSourceService.count() == 0) {
            leadSource.forEach((name, displayName) -> {
                if (leadSourceService.findByName(name) == null) {
                    LeadSource leadSource = new LeadSource();
                    leadSource.setDescription(displayName);
                    leadSource.setName(leadSourceService.createLeadSourceName(leadSource.getName()));
                    leadSource.setCreatedAt(LocalDateTime.now());
                    leadSource.setLastUpdateAt(LocalDateTime.now());
                    leadSource.setActive(true);
                    leadSourceRepository.save(leadSource);
                }
            });
        }
    }

    private void loadLeadStatus() {
        leadStatus.forEach((name, displayName) -> {
            LeadStatus leadInDB = leadStatusService.findByName(name);
            if (Objects.isNull(leadInDB)) {
                LeadStatus leadStatus = new LeadStatus();
                leadStatus.setName(leadStatusService.createLeadStatusName(leadStatus.getName()));
                leadStatus.setDescription(displayName);
                leadStatus.setCreatedAt(LocalDateTime.now());
                leadStatus.setLastUpdateAt(LocalDateTime.now());
                leadStatus.setActive(true);
                leadStatusRepository.save(leadStatus);
            }
        });
    }

    private void loadLeadType() {
        leadType.forEach((name, displayName) -> {
            if (leadTypeService.findByType(name) == null) {
                LeadType leadType = new LeadType();
                leadType.setDescription(displayName);
                leadType.setName(leadTypeService.createLeadType(name));
                leadType.setLastUpdateAt(LocalDateTime.now());
                leadType.setCreatedAt(LocalDateTime.now());
                leadType.setActive(true);
                leadTypeRepository.save(leadType);
            }
        });
    }

    private void loadLeadInventorySize() {
        initialInventorySize.forEach((size, displayName) -> {
            if (leadInventorySizeService.findBySize(size) == null) {
                LeadInventorySize leadInventorySize = new LeadInventorySize();
                leadInventorySize.setDescription(displayName);
                leadInventorySize.setSize(leadInventorySizeService.createInventorySize(size));
                leadInventorySize.setCreatedAt(LocalDateTime.now());
                leadInventorySize.setLastUpdateAt(LocalDateTime.now());
                leadInventorySize.setActive(true);
                leadInventorySizeRepository.save(leadInventorySize);
            }
        });
    }

    private void loadLeadDeactivationReason() {
        leadDeactivationReasons.forEach((name, displayName) -> {
            if (deactivationReasonService.findByName(name) == null) {
                DeactivationReason deactivationReason = new DeactivationReason();
                deactivationReason.setDescription(displayName);
                deactivationReason.setName(deactivationReasonService.createDeactivationReasonName(name));
                deactivationReason.setCreatedAt(LocalDateTime.now());
                deactivationReason.setLastUpdateAt(LocalDateTime.now());
                deactivationReason.setActive(true);
                deactivationReasonRepository.save(deactivationReason);
            }
        });
    }

    private void InventoryBookingStatus() {
        inventoryBookingStatus.forEach((name, displayName) -> {
            InventoryStatus leadInDB = inventoryStatusService.findByName(name);
            if (Objects.isNull(leadInDB)) {
                InventoryStatus inventoryStatus = new InventoryStatus();
                inventoryStatus.setName(name);
                inventoryStatus.setActive(true);
                inventoryStatus.setCreatedAt(LocalDateTime.now());
                inventoryStatus.setDescription(displayName);
                inventoryStatusRepository.save(inventoryStatus);
            }
        });
    }
}
