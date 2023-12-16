package com.sfd.thesmartestate.users.teams.services;

import com.sfd.thesmartestate.projects.entities.Project;
import com.sfd.thesmartestate.projects.services.ProjectService;
import com.sfd.thesmartestate.users.entities.Employee;
import com.sfd.thesmartestate.users.services.EmployeeService;
import com.sfd.thesmartestate.users.teams.entities.Team;
import com.sfd.thesmartestate.users.teams.exceptions.TeamException;
import com.sfd.thesmartestate.users.teams.repository.TeamRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final EmployeeService employeeService;
    private final ProjectService projectService;

    @Override
    public Team create(Team team) {
        validateTeam(team);
        team.setIsActive(true);
        team.setProject(validateProject(team.getProject()));
        team.setCreatedAt(LocalDateTime.now());
        team.setLastUpdateAt(LocalDateTime.now());
        team.setCreatedBy(employeeService.findLoggedInEmployee());
        team.setUpdatedBy(employeeService.findLoggedInEmployee());
        return teamRepository.save(team);
    }

    private void validateTeam(Team team) {
        validateTeamName(team.getName());
    }

    private Project validateProject(Project project) {
        Project project1 = projectService.findById(project.getId());
        validateProjectName(project1);
        return project1;
    }

    private void validateTeamName(String name) {
        if (Objects.isNull(name) ||
                !StringUtils.hasText(name)) {
            throw new TeamException("Team name can not be empty");
        }

        if (Objects.nonNull(teamRepository.findByName(name))) {
            throw new TeamException("Team name already exists");
        }
    }

    private void validateProjectName(Project project) {
        if (Objects.isNull(project)) {
            throw new TeamException("Project can not be empty");
        }
    }

    @Override
    public Team update(Team team) {
        validateTeam(team);
        validateProject(team.getProject());
        team.setLastUpdateAt(LocalDateTime.now());
        team.setUpdatedBy(employeeService.findLoggedInEmployee());
        return teamRepository.save(team);
    }

    @Override
    public Team addMember(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException("Team does not exists"));
        Set<Employee> members = team.getMembers();
        if (Objects.isNull(members)) {
            members = new HashSet<>();
        }
        if (!members.add(employeeService.findById(userId))) {
            throw new TeamException("Member can not be added, its already present in team members");
        }
        team.setLastUpdateAt(LocalDateTime.now());
        team.setUpdatedBy(employeeService.findLoggedInEmployee());
        return teamRepository.save(team);
    }

    @Override
    public Team removeMember(Long teamId, Long userId) {
        Team team = findById(teamId);
        Set<Employee> members = team.getMembers();
        checkMemberIsNotTeamLead(team.getSupervisor().getId(), userId);
        if (Objects.nonNull(members)) {
            members.remove(employeeService.findById(userId));
        }
        team.setLastUpdateAt(LocalDateTime.now());
        team.setUpdatedBy(employeeService.findLoggedInEmployee());
        return teamRepository.save(team);
    }

    private void checkMemberIsNotTeamLead(Long leadId, Long userId) {
        if (Objects.equals(leadId, userId)) {
            throw new TeamException("Team Lead can not be removed.");
        }
    }

    @Override
    public Team assignTeamLeader(Long teamId, Long userId) {
        Team team = findById(teamId);
        validateTeamLeader(team);
        Employee employee = employeeService.findById(userId);
        team.getMembers().add(employee);
        team.setSupervisor(employee);
        team.setLastUpdateAt(LocalDateTime.now());
        team.setUpdatedBy(employeeService.findLoggedInEmployee());
        return teamRepository.save(team);
    }

    private void validateTeamLeader(Team team) {
        if (isLeadAlreadyAssigned(team.getSupervisor())) {
            // At UI, send another request to changeTeamLeader
            throw new TeamException("Team leader is already assigned, if you want to change team leader, click continue");
        }
    }

    private boolean isLeadAlreadyAssigned(Employee leader) {
        return Objects.nonNull(leader);
    }

    @Override
    public Team changeTeamLeader(Long teamId, Long currentLeadId, Long newLeadId) {
        Team team = findById(teamId);
        Employee currentLead = employeeService.findById(currentLeadId);
        Employee newLead = employeeService.findById(newLeadId);
        if (isChangeRequestValid(team, currentLead, newLead)) {
            team.setSupervisor(newLead);
        } else {
            throw new TeamException("Current team lead is not valid, please try again");
        }
        team.getMembers().add(newLead);
        team.setLastUpdateAt(LocalDateTime.now());
        team.setUpdatedBy(employeeService.findLoggedInEmployee());
        return teamRepository.save(team);
    }

    private Team setLeadInMembersIfNotPresent(Team team, Employee newLead) {
        team.getMembers().add(newLead);
        return team;
    }

    @Override
    public List<Team> findByProjectId(Long projectId) {
        return teamRepository.findByProject_id(projectId);
    }

    @Override
    public List<Team> findAll() {
        return teamRepository.findAll().stream().filter(Team::getIsActive).collect(Collectors.toList());
    }

    @Override
    public Team findById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException("Team does not exists"));
    }

    private boolean isChangeRequestValid(Team team, Employee currentLead, Employee newLead) {
        return Objects.nonNull(team.getSupervisor())
                && team.getSupervisor().equals(currentLead)
                && Objects.nonNull(newLead);
    }

    @Override
    public Set<Employee> findMembersByTeamId(Long teamId) {
        return teamRepository
                .findById(teamId)
                .orElseThrow(() -> new TeamException("Team not found")).getMembers();
    }

    @Override
    public List<Team> findTeamsByMemberIdAndIsActive(Long userId,boolean isActive) {
        return teamRepository.findByMembers_idAndIsActive(userId,isActive);
    }

    @Override
    public Team changeProject(Long teamId, Long projectId, Long newProjectId) {
        Project newProject = projectService.findById(newProjectId);
        validateProject(projectId, newProjectId, newProject);
        Team team = findById(teamId);
        team.setProject(newProject);
        teamRepository.save(team);
        return team;
    }

    @Override
    public Team deactivateTeam(Long teamId) {
        Team team = teamRepository.findById(teamId).orElse(null);
        assert team != null;
        team.setIsActive(false);
        return teamRepository.saveAndFlush(team);
    }

    private void validateProject(Long projectId, Long newProjectId, Project newProject) {
        if (Objects.equals(projectId, newProjectId)) {
            throw new TeamException("New and old project can not be same");
        }
        if (Objects.isNull(newProject)) {
            throw new TeamException("Project can't be changed, new Project does not exists");
        }
    }
}
