package com.sfd.thesmartestate.employee.teams.repository;

import com.sfd.thesmartestate.employee.teams.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByName(String name);

    List<Team> findByMembers_id(Long id);

    List<Team> findByMembers_idAndIsActive(Long id, boolean isActive);

    List<Team> findByProject_id(Long projectId);
}
