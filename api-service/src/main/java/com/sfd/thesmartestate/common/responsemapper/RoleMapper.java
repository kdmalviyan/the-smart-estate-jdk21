package com.sfd.thesmartestate.common.responsemapper;

import com.sfd.thesmartestate.common.entities.Role;
import com.sfd.thesmartestate.users.dtos.RoleDto;

public class RoleMapper {
    private RoleMapper() {
        throw new IllegalStateException("Constructor can't be initialize error");
    }

    public static RoleDto mapToRole(Role role) {
        RoleDto response = new RoleDto();
        if (role != null) {
            response.setId(role.getId());
            response.setDescription(role.getDescription());
            response.setDisplayName(role.getDisplayName());
            response.setName(role.getName());
        }
        return response;

    }
}
