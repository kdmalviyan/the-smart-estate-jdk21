package com.sfd.thesmartestate.employee.teams.services;

import com.sfd.thesmartestate.employee.entities.Employee;
import com.sfd.thesmartestate.employee.teams.entities.Team;

import java.util.List;
import java.util.Set;

public interface TeamService {
    Team create(Team team);

    Team update(Team team);

    Team addMember(Long teamId, Long userId);

    Team removeMember(Long teamId, Long userId);

    List<Team> findTeamsByMemberIdAndIsActive(Long userId,boolean isActive);

    List<Team> findByProjectId(Long projectId);

    List<Team> findAll();

    Team findById(Long teamId);

    Team assignTeamLeader(Long teamId, Long userId);

    Team changeTeamLeader(Long teamId, Long currentLead, Long newLead);

    Set<Employee> findMembersByTeamId(Long teamId);

    Team changeProject(Long teamId, Long projectId, Long newProjectId);

    Team deactivateTeam(Long teamId);
}
