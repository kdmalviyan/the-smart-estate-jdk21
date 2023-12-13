package com.sfd.thesmartestate.security.controllers;


import com.sfd.thesmartestate.projects.dto.ProjectDTO;
import com.sfd.thesmartestate.security.entities.RefreshToken;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
@SuppressFBWarnings("EI_EXPOSE_REP")
public class AuthResponse {
    private String token;
    private RefreshToken refreshToken;
    private String username;
    private String role;
    private boolean isAdmin;
    private boolean isSuperAdmin;
    private ProjectDTO project;
    private String profilePath;
}
