package com.sfd.thesmartestate.projects.repositories;

import com.sfd.thesmartestate.projects.dto.ProjectDTO;
import com.sfd.thesmartestate.projects.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(value = "select new com.sfd.thesmartestate.projects.dto.ProjectDTO(id, name, enabled) FROM Project")
    List<ProjectDTO> findAllMinimal();

    Optional<Project> findByName(String projectName);
}
