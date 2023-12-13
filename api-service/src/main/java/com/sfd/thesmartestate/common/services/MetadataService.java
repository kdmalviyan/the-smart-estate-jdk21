package com.sfd.thesmartestate.common.services;

import com.sfd.thesmartestate.common.dto.MetadataResponse;
import com.sfd.thesmartestate.common.entities.Role;
import com.sfd.thesmartestate.lms.entities.LeadInventorySize;
import com.sfd.thesmartestate.projects.dto.ProjectDTO;
import com.sfd.thesmartestate.users.dtos.UserResponse;

import java.util.List;

public interface MetadataService {
    List<MetadataResponse> fetchAllLeadStatus();

    List<MetadataResponse> fetchAllLeadSource();

    List<MetadataResponse> fetchAllLeadType();

    List<Role> fetchAllRoles();

    List<UserResponse> fetchAllUsers();

    List<MetadataResponse> fetchAllDeactivationReason();

    List<ProjectDTO> fetchAllProjects();

    List<MetadataResponse> fetchAllInventoryStatus();
    List<LeadInventorySize> fetchAllLeadInventorySize();

    UserResponse formatLoggedInUserResponse();
}
