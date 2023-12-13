package com.sfd.thesmartestate.common.services;

import com.sfd.thesmartestate.common.dto.MetadataResponse;
import com.sfd.thesmartestate.common.entities.Role;
import com.sfd.thesmartestate.common.responsemapper.MetadataResponseMapper;
import com.sfd.thesmartestate.common.responsemapper.UserResponseMapper;
import com.sfd.thesmartestate.lms.entities.LeadInventorySize;
import com.sfd.thesmartestate.lms.services.*;
import com.sfd.thesmartestate.projects.dto.ProjectDTO;
import com.sfd.thesmartestate.projects.services.InventoryStatusService;
import com.sfd.thesmartestate.projects.services.ProjectService;
import com.sfd.thesmartestate.users.dtos.UserResponse;
import com.sfd.thesmartestate.users.services.UserService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class MetadataServiceImpl implements MetadataService {

    @Autowired
    private LeadSourceService leadSourceService;
    @Autowired
    private LeadTypeService leadTypeService;
    @Autowired
    private LeadStatusService leadStatusService;
    @Autowired
    private LeadInventorySizeService leadInventorySizeService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private DeactivationReasonService deactivationReasonService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private InventoryStatusService inventoryStatusService;

    @Override
    public List<MetadataResponse> fetchAllLeadStatus() {
        return leadStatusService.findAll().stream()
                .map(MetadataResponseMapper::mapToLeadStatus).collect(Collectors.toList());
    }

    @Override
    public UserResponse formatLoggedInUserResponse() {
        return UserResponseMapper.mapToUserResponse(userService.findLoggedInUser());
    }

    @Override
    public List<MetadataResponse> fetchAllLeadSource() {
        return leadSourceService.findAll().stream()
                .map(MetadataResponseMapper::mapToLeadSource).collect(Collectors.toList());
    }

    @Override
    public List<MetadataResponse> fetchAllLeadType() {
        return leadTypeService.findAll().stream()
                .map(MetadataResponseMapper::mapToLeadType).collect(Collectors.toList());
    }

    @Override
    public List<LeadInventorySize> fetchAllLeadInventorySize() {
        return leadInventorySizeService.findAll();
    }

    @Override
    public List<Role> fetchAllRoles() {
        return roleService.findAll();
    }

    @Override
    public List<UserResponse> fetchAllUsers() {
        return userService.findAll().stream().map(UserResponseMapper::mapToUserResponse)
                .sorted(Comparator.comparing(UserResponse::getName)).collect(Collectors.toList());
    }

    @Override
    public List<MetadataResponse> fetchAllDeactivationReason() {
        return deactivationReasonService.findAll().stream()
                .map(MetadataResponseMapper::mapToDeactivationReason).collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> fetchAllProjects() {
        return projectService.findAllMinimalFields();
    }

    @Override
    public List<MetadataResponse> fetchAllInventoryStatus() {
        return inventoryStatusService.findAll().stream()
                .map(MetadataResponseMapper::mapToInventoryStatus).collect(Collectors.toList());
    }

}
