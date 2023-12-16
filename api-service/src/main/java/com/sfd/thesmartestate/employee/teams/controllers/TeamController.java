package com.sfd.thesmartestate.employee.teams.controllers;

import com.sfd.thesmartestate.employee.entities.Employee;
import com.sfd.thesmartestate.employee.services.EmployeeService;
import com.sfd.thesmartestate.employee.teams.entities.Team;
import com.sfd.thesmartestate.employee.teams.services.TeamService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("team")
@SuppressFBWarnings("EI_EXPOSE_REP")
@CrossOrigin("*")
public class TeamController {

    private final TeamService teamService;
    private final EmployeeService employeeService;

    @GetMapping("")
    public ResponseEntity<List<Team>> listAllTeams() {
        return ResponseEntity.ok(teamService.findAll());
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<Team> getTeamByTeamId(@PathVariable("teamId") Long teamId) {
        return ResponseEntity.ok(teamService.findById(teamId));
    }

    @PostMapping("")
    public ResponseEntity<Team> create(@RequestBody Team team) {
        return ResponseEntity.ok(teamService.create(team));
    }

    @PutMapping("")
    public ResponseEntity<Team> update(@RequestBody Team team) {
        return ResponseEntity.ok(teamService.update(team));
    }

    @PutMapping("/{teamId}/project/{projectId}/{newProjectId}")
    public ResponseEntity<Team> update(@PathVariable("teamId") Long teamId,
                                       @PathVariable("projectId") Long projectId,
                                       @PathVariable("newProjectId") Long newProjectId) {
        return ResponseEntity.ok(teamService.changeProject(teamId, projectId, newProjectId));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Team>> listAllTeamsByProject(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(teamService.findByProjectId(projectId));
    }

    @GetMapping("/members/{userId}")
    public ResponseEntity<List<Team>> findTeamsByMember(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(teamService.findTeamsByMemberIdAndIsActive(userId, true));
    }

    @PutMapping("members/add/{teamId}/{userId}")
    public ResponseEntity<Team> addTeamMember(@PathVariable("teamId") Long teamId,
                                              @PathVariable("userId") Long userId) {

        return ResponseEntity.ok(teamService.addMember(teamId, userId));
    }

    @PutMapping("members/remove/{teamId}/{userId}")
    public ResponseEntity<Team> removeTeamMember(@PathVariable("teamId") Long teamId,
                                                 @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(teamService.removeMember(teamId, userId));
    }

    @PutMapping("members/lead/assign/{teamId}/{userId}")
    public ResponseEntity<Team> assignLead(@PathVariable("teamId") Long teamId,
                                           @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(teamService.assignTeamLeader(teamId, userId));
    }

    @PutMapping("members/lead/change/{teamId}/{oldUserId}/{newUserId}")
    public ResponseEntity<Team> changeLead(@PathVariable("teamId") Long teamId,
                                           @PathVariable("oldUserId") Long oldUserId,
                                           @PathVariable("newUserId") Long newUserId) {
        return ResponseEntity.ok(teamService.changeTeamLeader(teamId, oldUserId, newUserId));
    }

    @GetMapping("/employee/{projectId}")
    public ResponseEntity<List<Employee>> listAllUsersByProject(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(employeeService.findEmployeesByProjectId(projectId));
    }

    @GetMapping("/deactivate/{id}")
    public ResponseEntity<Team> deactivateTeam(@PathVariable("id") Long id) {
        return ResponseEntity.ok(teamService.deactivateTeam(id));
    }
}
