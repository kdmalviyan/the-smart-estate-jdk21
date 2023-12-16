package com.sfd.thesmartestate.common.responsemapper;

import com.sfd.thesmartestate.projects.dto.ProjectDTO;
import com.sfd.thesmartestate.employee.dtos.RoleDto;
import com.sfd.thesmartestate.employee.dtos.UserResponse;
import com.sfd.thesmartestate.employee.entities.Employee;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class UserResponseMapper {
    private UserResponseMapper() {
        throw new IllegalStateException("Constructor can't be initialize error");
    }

    public static UserResponse mapToUserResponse(Employee employee) {
        UserResponse response = null;
        if (employee != null) {
            response = new UserResponse();

            Set<RoleDto> roleDtos = employee.getRoles().stream()
                    .map(RoleMapper::mapToRole)
                    .sorted(Comparator.comparingLong(RoleDto::getId).reversed())
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            response.setId(employee.getId());
            response.setName(employee.getName());
            response.setEnabled(employee.isActive());
            response.setUsername(employee.getUsername());
            response.setRoles(roleDtos);
            response.setAdmin(employee.isAdmin());
            response.setSuperAdmin(employee.isSuperAdmin());
            if(Objects.nonNull(employee.getProject())) {
                response.setProject(ProjectDTO
                        .builder().name(employee.getProject().getName()).enabled(employee.getProject().isEnabled())
                        .id(employee.getProject().getId()).build());
            }
            response.setProfileImagePath(employee.getProfileImagePath());
            response.setProfileImageThumbPath(employee.getProfileImageThumbPath());
        }
        return response;

    }

}
