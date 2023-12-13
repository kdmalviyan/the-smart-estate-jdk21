package com.sfd.thesmartestate.projects.services;

import com.sfd.thesmartestate.projects.dto.ProjectDTO;
import com.sfd.thesmartestate.projects.entities.Project;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {
    Project create(Project project);

    Project update(Project project);

    Project findById(Long id);

    List<Project> findAll();

    List<ProjectDTO> findAllMinimalFields();

    void delete(Project project);

    Project findByName(String projectName);

    Project uploadAndSaveFile(Long projectId, String type, String fileType, MultipartFile file);

    Resource downloadImage(Long fileId);

    Project deleteFile(Long projectId, Long id);

}
