package com.sfd.thesmartestate.common.responsemapper;

import com.sfd.thesmartestate.projects.dto.ProjectDTO;
import com.sfd.thesmartestate.projects.entities.Project;

public class ProjectResponseMapper {
    private ProjectResponseMapper() {
        throw new IllegalStateException("Constructor can't be initialize error");
    }

    public static ProjectDTO mapToProjectResponse(Project project) {
        ProjectDTO response = new ProjectDTO();
        if (project != null) {
            response.setId(project.getId());
            response.setName(project.getName());
            response.setEnabled(project.isEnabled());
        }
        return response;

    }
}
