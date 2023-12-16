package com.sfd.thesmartestate.lms.targets;

import com.sfd.thesmartestate.common.LeadEvents;
import com.sfd.thesmartestate.dashboard.dtos.TargetCountResponseDto;
import com.sfd.thesmartestate.lms.exceptions.TargetException;
import com.sfd.thesmartestate.projects.services.ProjectService;
import com.sfd.thesmartestate.users.entities.Employee;
import com.sfd.thesmartestate.users.services.EmployeeService;
import com.sfd.thesmartestate.users.teams.entities.Team;
import com.sfd.thesmartestate.users.teams.services.TeamService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class TargetServiceImpl implements TargetService {
    private final TargetRepository repository;
    private final EmployeeService employeeService;
    private final ProjectService projectService;
    private final TargetStatisticsHelper helper;
    private final TeamService teamService;

    @Override
    public Target create(Target target) {
        Employee loggedInEmployee = employeeService.findLoggedInEmployee();
        target.setCreatedAt(LocalDateTime.now());
        target.setCreatedBy(loggedInEmployee);
        if (Objects.isNull(target.getAssignedTo())) {
            target.setAssignedTo(loggedInEmployee);
        }
        if ("weekly".equalsIgnoreCase(target.getDuration())) {
            LocalDate starDate = target.getStartDate();
            LocalDate endDate = starDate.plusDays(6);
            //date selected from UI is coming -1 days so setting it to +1
            target.setStartDate(starDate.plusDays(1));
            target.setEndDate(endDate);

        } else if ("monthly".equalsIgnoreCase(target.getDuration())) {
            LocalDate starDate = target.getStartDate();
            YearMonth thisYearMonth = YearMonth.of(starDate.getYear(), starDate.getMonth());
            LocalDate starDateFirstDay = thisYearMonth.atDay(1);
            LocalDate endDate = thisYearMonth.atEndOfMonth();
            target.setEndDate(endDate);
            target.setStartDate(starDateFirstDay);
        }
        List<Target> targets = findByCreatedByAndStartDate(loggedInEmployee, target.getStartDate());
        if (targets.size() > 0) {
            throw new TargetException("Target for this range already Exists start-" + target.getStartDate() + " End Date-" + target.getEndDate());
        }
        target.setSiteVisitDoneCount(0L);
        target.setBookingDoneCount(0L);
        target.setActive(true);
        target.setProject(projectService.findById(target.getProject().getId()));
        target = repository.saveAndFlush(target);
        return target;
    }

    @Override
    public List<Target> findByCreatedByAndStartDate(Employee employee, LocalDate startDate) {
        return repository.findByCreatedByAndStartDate(employee, startDate);
    }

    @Override
    public Target update(Target target) {
        return repository.saveAndFlush(target);
    }

    @Override
    public Target findAndUpdateUserTarget(Employee employee, LeadEvents leadEvent) {
        List<Target> targetList;
        Target targetToUpdate;
        // check weekly target present and update that target startday-sunday ,endday-sunday
        LocalDate starDateWeekly = LocalDate.now().with(DayOfWeek.MONDAY).minusDays(1);
        LocalDate endDateWeekly = LocalDate.now().with(DayOfWeek.SATURDAY).plusDays(1);
        targetList = repository.findAllByCreatedByAndProjectAndStartDateGreaterThanEqualAndEndDateLessThanEqual(employee, employee.getProject(), starDateWeekly, endDateWeekly);

        //if no entry found for week then mark for this month
        if (targetList.isEmpty()) {
            YearMonth thisYearMonth = YearMonth.of(LocalDate.now().getYear(), LocalDate.now().getMonth());
            LocalDate startDateMontly = thisYearMonth.atDay(1);
            LocalDate endDateMontly = thisYearMonth.atEndOfMonth();
            targetList = repository.findAllByCreatedByAndProjectAndStartDateGreaterThanEqualAndEndDateLessThanEqual(employee, employee.getProject(), startDateMontly, endDateMontly);
        }

        // in case no week target found check for monthly target
        if (!targetList.isEmpty()) {
            targetToUpdate = targetList.get(0);
            if (LeadEvents.SITE_VISIT.equals(leadEvent)) {
                targetToUpdate.setSiteVisitDoneCount(targetToUpdate.getSiteVisitDoneCount() + 1);
            } else if (LeadEvents.STATUS_CHANGED.equals(leadEvent)) {
                targetToUpdate.setBookingDoneCount(targetToUpdate.getBookingDoneCount() + 1);
            }
            //update target
            update(targetToUpdate);

        } else {
            throw new TargetException("No target set for this week or Month please set target to mark your Targets ");
        }

        return targetToUpdate;
    }


    @Override
    public List<Target> findAll(boolean groupBy) {
        Employee loggedImEmployee = employeeService.findLoggedInEmployee();
        List<Target> targets;
        if (loggedImEmployee.isSuperAdmin() || loggedImEmployee.isAdmin()) {
            if (groupBy) {
                targets = groupByProjectAndTarget(repository.findAll());
            } else {
                targets = repository.findAll();
            }
        } else {
            List<Team> teams = teamService.findTeamsByMemberIdAndIsActive(loggedImEmployee.getId(),true);
            List<Employee> teamMembers;
            //user is supervisor so fetch all subordinates targets
            if (!teams.isEmpty() && teams.get(0).getSupervisor().getId().equals(loggedImEmployee.getId())) {
                Team team = teams.get(0);
                teamMembers = new ArrayList<>(team.getMembers());
                targets = repository.findByCreatedByIn(teamMembers);
            } else {
                targets = repository.findByCreatedBy(loggedImEmployee);
            }
        }
        return targets;
    }

    @Override
    public void delete(Long targetId) {
        repository.deleteById(targetId);
    }

    @Override
    public List<TargetCountResponseDto> getVisitsTargetCount() {
        Employee loggedInEmployee = employeeService.findLoggedInEmployee();
        Map<String, List<Target>> projectWiseTargets = helper.createProjectWiseTargets(loggedInEmployee);
        List<TargetCountResponseDto> response = new ArrayList<>();
        for (String projectName : projectWiseTargets.keySet()) {
            List<Target> targets = projectWiseTargets.get(projectName);
            TargetCountResponseDto visitTargetCount = TargetCountResponseDto.builder()
                    .withProjectName(projectName)
                    .withTargetType("VISIT_TARGET")
                    .withTargetCountMap(
                            helper.convertToTargetDtoObject(new HashMap<>() {{
                                put(-1, targets.stream()
                                        .map(t -> Objects.isNull(t.getSiteVisitCount()) ? 0 : t.getSiteVisitCount())
                                        .mapToLong(e -> e)
                                        .sum());
                            }}))

                    .withTargetDoneCountMap(
                            helper.convertToTargetDtoObject(new HashMap<>() {{
                                                                put(-1, targets.stream()
                                                                        .map(t -> Objects.isNull(t.getSiteVisitDoneCount()) ? 0 : t.getSiteVisitDoneCount())
                                                                        .mapToLong(e -> e)
                                                                        .sum());
                                                            }}
                            ))
                    .build();
            response.add(visitTargetCount);
        }
        return response;
    }

    @Override
    public List<TargetCountResponseDto> getVisitsWeeklyTargetCount() {
        Employee loggedInEmployee = employeeService.findLoggedInEmployee();
        Map<String, List<Target>> projectWiseTargets = helper.createProjectWiseTargets(loggedInEmployee);
        List<TargetCountResponseDto> response = new ArrayList<>();
        for (String projectName : projectWiseTargets.keySet()) {
            List<Target> targets = projectWiseTargets.get(projectName);
            TargetCountResponseDto visitTargetCount = TargetCountResponseDto.builder()
                    .withProjectName(projectName)
                    .withTargetType("VISIT_TARGET")
                    .withTargetCountMap(helper.createWeeklyVisitMap(targets))
                    .withTargetDoneCountMap(helper.createWeeklyVisitDoneMap(targets))
                    .build();
            response.add(visitTargetCount);
        }
        return response;
    }

    @Override
    public List<TargetCountResponseDto> getVisitsMonthlyTargetCount() {
        Employee loggedInEmployee = employeeService.findLoggedInEmployee();
        Map<String, List<Target>> projectWiseTargets = helper.createProjectWiseTargets(loggedInEmployee);
        List<TargetCountResponseDto> response = new ArrayList<>();
        for (String projectName : projectWiseTargets.keySet()) {
            List<Target> targets = projectWiseTargets.get(projectName);
            TargetCountResponseDto visitTargetCount = TargetCountResponseDto.builder()
                    .withProjectName(projectName)
                    .withTargetType("VISIT_TARGET")
                    .withTargetCountMap(helper.createMonthlyVisitMap(targets))
                    .withTargetDoneCountMap(helper.createMonthlyVisitDoneMap(targets))
                    .build();
            response.add(visitTargetCount);
        }
        return response;
    }

    @Override
    public List<TargetCountResponseDto> getBookingsTargetCount() {
        Employee loggedInEmployee = employeeService.findLoggedInEmployee();
        Map<String, List<Target>> projectWiseTargets = helper.createProjectWiseTargets(loggedInEmployee);
        List<TargetCountResponseDto> response = new ArrayList<>();
        for (String projectName : projectWiseTargets.keySet()) {
            List<Target> targets = projectWiseTargets.get(projectName);
            TargetCountResponseDto visitTargetCount = TargetCountResponseDto.builder()
                    .withProjectName(projectName)
                    .withTargetType("BOOKING_TARGET")
                    .withTargetCountMap(helper.convertToTargetDtoObject(new HashMap<>() {{
                        put(-1, targets.stream()
                                .map(t -> Objects.isNull(t.getBookingCount()) ? 0 : t.getBookingCount())
                                .mapToLong(e -> e)
                                .sum());
                    }}))
                    .withTargetDoneCountMap(
                            helper.convertToTargetDtoObject(new HashMap<>() {{
                                                                put(-1, targets.stream()
                                                                        .map(t -> Objects.isNull(t.getBookingDoneCount()) ? 0 : t.getBookingDoneCount())
                                                                        .mapToLong(e -> e)
                                                                        .sum());
                                                            }}
                            ))
                    .build();
            response.add(visitTargetCount);
        }
        return response;
    }

    @Override
    public List<TargetCountResponseDto> getBookingsWeeklyTargetCount() {
        Employee loggedInEmployee = employeeService.findLoggedInEmployee();
        Map<String, List<Target>> projectWiseTargets = helper.createProjectWiseTargets(loggedInEmployee);
        List<TargetCountResponseDto> response = new ArrayList<>();
        for (String projectName : projectWiseTargets.keySet()) {
            List<Target> targets = projectWiseTargets.get(projectName);
            TargetCountResponseDto visitTargetCount = TargetCountResponseDto.builder()
                    .withProjectName(projectName)
                    .withTargetType("BOOKING_TARGET")
                    .withTargetCountMap(helper.createWeeklyBookingMap(targets))
                    .withTargetDoneCountMap(helper.createWeeklyBookingDoneMap(targets))
                    .build();
            response.add(visitTargetCount);
        }
        return response;
    }

    @Override
    public List<TargetCountResponseDto> getBookingsMonthlyTargetCount() {
        Employee loggedInEmployee = employeeService.findLoggedInEmployee();
        Map<String, List<Target>> projectWiseTargets = helper.createProjectWiseTargets(loggedInEmployee);
        List<TargetCountResponseDto> response = new ArrayList<>();
        for (String projectName : projectWiseTargets.keySet()) {
            List<Target> targets = projectWiseTargets.get(projectName);
            TargetCountResponseDto visitTargetCount = TargetCountResponseDto.builder()
                    .withProjectName(projectName)
                    .withTargetType("Booking_TARGET")
                    .withTargetCountMap(helper.createMonthlyBookingMap(targets))
                    .withTargetDoneCountMap(helper.createMonthlyBookingDoneMap(targets))
                    .build();
            response.add(visitTargetCount);
        }
        return response;
    }

    private List<Target> groupByProjectAndTarget(List<Target> targets) {
        Map<String, List<Target>> grouped = targets.stream()
                .collect(Collectors.groupingBy(target -> (target.getCreatedBy().getId()) + "-" + target.getProject().getId()));

        return grouped.values().stream()
                .map(targetList -> targetList.stream().reduce(this::createTarget))
                .map(Optional::get).collect(Collectors.toList());
    }

    private Target createTarget(Target t1, Target t2) {
        Target target = new Target();
        target.setId(t1.getId());
        target.setBookingCount(t1.getBookingCount() + t2.getBookingCount());
        target.setSiteVisitCount(t1.getSiteVisitCount() + t2.getSiteVisitCount());
        target.setCreatedBy(t1.getCreatedBy());
        target.setProject(t1.getProject());
        target.setBookingDoneCount(t1.getBookingDoneCount() + t2.getBookingDoneCount());
        target.setSiteVisitDoneCount(t1.getSiteVisitDoneCount() + t2.getSiteVisitDoneCount());
        target.setStartDate(compareDatetime(t1.getStartDate(), t2.getStartDate()));
        target.setEndDate(compareEndDatetime(t1.getEndDate(), t2.getEndDate()));
        target.setAssignedTo(t1.getAssignedTo());
        return target;
    }

    private LocalDate compareDatetime(LocalDate t1, LocalDate t2) {
        return t1.isBefore(t2) ? t1 : t2;
    }

    private LocalDate compareEndDatetime(LocalDate t1, LocalDate t2) {
        return t1.isAfter(t2) ? t1 : t2;
    }
}
