package com.sfd.thesmartestate.employee.dtos;

import com.sfd.thesmartestate.projects.dto.ProjectDTO;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import java.util.Set;

@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class UserResponse {
    private Long id;
    private String name;
    private boolean enabled;
    private String username;
    //
    private UserResponse supervisor;
    private Set<UserResponse> subordinates;
    //role
    private Set<RoleDto> roles;
    private boolean isAdmin;
    private boolean isSuperAdmin;
    private ProjectDTO project;

    private String profileImagePath;
    private String profileImageThumbPath;

}
