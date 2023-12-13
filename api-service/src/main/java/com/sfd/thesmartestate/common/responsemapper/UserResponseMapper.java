package com.sfd.thesmartestate.common.responsemapper;

import com.sfd.thesmartestate.projects.dto.ProjectDTO;
import com.sfd.thesmartestate.users.dtos.RoleDto;
import com.sfd.thesmartestate.users.dtos.UserResponse;
import com.sfd.thesmartestate.users.entities.User;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class UserResponseMapper {
    private UserResponseMapper() {
        throw new IllegalStateException("Constructor can't be initialize error");
    }

    public static UserResponse mapToUserResponse(User user) {
        UserResponse response = null;
        if (user != null) {
            response = new UserResponse();

            Set<RoleDto> roleDtos = user.getRoles().stream()
                    .map(RoleMapper::mapToRole)
                    .sorted(Comparator.comparingLong(RoleDto::getId).reversed())
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            response.setId(user.getId());
            response.setName(user.getName());
            response.setEnabled(user.isEnabled());
            response.setUsername(user.getUsername());
            response.setRoles(roleDtos);
            response.setAdmin(user.isAdmin());
            response.setSuperAdmin(user.isSuperAdmin());
            if(Objects.nonNull(user.getProject())) {
                response.setProject(ProjectDTO
                        .builder().name(user.getProject().getName()).enabled(user.getProject().isEnabled())
                        .id(user.getProject().getId()).build());
            }
            response.setProfileImagePath(user.getProfileImagePath());
            response.setProfileImageThumbPath(user.getProfileImageThumbPath());
        }
        return response;

    }

}
