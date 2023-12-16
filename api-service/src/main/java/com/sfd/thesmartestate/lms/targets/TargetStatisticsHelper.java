package com.sfd.thesmartestate.lms.targets;

import com.sfd.thesmartestate.dashboard.dtos.TargetWeekResponseDto;
import com.sfd.thesmartestate.employee.entities.Employee;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
@SuppressFBWarnings("EI_EXPOSE_REP")

public class TargetStatisticsHelper {
    private final TargetRepository repository;

    public List<TargetWeekResponseDto> createMonthlyVisitDoneMap(List<Target> targets) {
        Map<Integer, Long> map = new TreeMap<>();
        for (Target target : targets) {
            LocalDate startDate = target.getStartDate();
            int monthOfYear = startDate.getMonth().getValue();
            if (!map.containsKey(monthOfYear)) {
                map.put(monthOfYear, Objects.isNull(target.getSiteVisitDoneCount()) ? 0 : target.getSiteVisitDoneCount());
            } else {
                map.put(monthOfYear, map.get(monthOfYear) + (Objects.isNull(target.getSiteVisitDoneCount()) ? 0 : target.getSiteVisitDoneCount()));
            }
        }
        return convertToTargetDtoObject(map);
    }

    public List<TargetWeekResponseDto> createMonthlyVisitMap(List<Target> targets) {
        Map<Integer, Long> map = new TreeMap<>();
        for (Target target : targets) {
            LocalDate startDate = target.getStartDate();
            int monthOfYear = startDate.getMonth().getValue();
            if (!map.containsKey(monthOfYear)) {
                map.put(monthOfYear, Objects.isNull(target.getSiteVisitCount()) ? 0 : target.getSiteVisitCount());
            } else {
                map.put(monthOfYear, map.get(monthOfYear) + (Objects.isNull(target.getSiteVisitCount()) ? 0 : target.getSiteVisitCount()));
            }
        }
        return convertToTargetDtoObject(map);
    }

    public List<TargetWeekResponseDto> createWeeklyVisitDoneMap(List<Target> targets) {
        Map<Integer, Long> map = new TreeMap<>();
        for (Target target : targets) {
            LocalDate startDate = target.getStartDate();
            int weekOfYear = startDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());
            if (!map.containsKey(weekOfYear)) {
                map.put(weekOfYear, Objects.isNull(target.getSiteVisitDoneCount()) ? 0 : target.getSiteVisitDoneCount());
            } else {
                map.put(weekOfYear, map.get(weekOfYear) + (Objects.isNull(target.getSiteVisitDoneCount()) ? 0 : target.getSiteVisitDoneCount()));
            }
        }
        return convertToTargetDtoObject(map);
    }

    public List<TargetWeekResponseDto> createWeeklyVisitMap(List<Target> targets) {
        Map<Integer, Long> map = new TreeMap<>();
        for (Target target : targets) {
            LocalDate startDate = target.getStartDate();
            int weekOfYear = startDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());
            if (!map.containsKey(weekOfYear)) {
                map.put(weekOfYear, target.getSiteVisitCount());
            } else {
                map.put(weekOfYear, map.get(weekOfYear) + target.getSiteVisitCount());
            }
        }
        return convertToTargetDtoObject(map);
    }

    public Map<String, List<Target>> createProjectWiseTargets(Employee loggedInEmployee) {
        Map<String, List<Target>> projectWiseTargets = new HashMap<>();
        for (Target target : getActiveTargets(loggedInEmployee)) {
            if (!projectWiseTargets.containsKey(target.getProject().getName())) {
                projectWiseTargets.put(target.getProject().getName(), new ArrayList<>());
            }
            projectWiseTargets.get(target.getProject().getName()).add(target);
        }
        return projectWiseTargets;
    }

    public Collection<Target> getActiveTargets(Employee loggedInEmployee) {
        return loggedInEmployee.isSuperAdmin() || loggedInEmployee.isAdmin()
                ? repository.findByActive(true)
                : repository.findByActiveAndAssignedTo(true, loggedInEmployee);
    }

    public List<TargetWeekResponseDto> createMonthlyBookingDoneMap(List<Target> targets) {
        Map<Integer, Long> map = new TreeMap<>();
        for (Target target : targets) {
            LocalDate startDate = target.getStartDate();
            int monthOfYear = startDate.getMonth().getValue();
            if (!map.containsKey(monthOfYear)) {
                map.put(monthOfYear, Objects.isNull(target.getBookingDoneCount()) ? 0 : target.getBookingDoneCount());
            } else {
                map.put(monthOfYear, map.get(monthOfYear) + (Objects.isNull(target.getBookingDoneCount()) ? 0 : target.getBookingDoneCount()));
            }
        }
        return convertToTargetDtoObject(map);
    }

    public List<TargetWeekResponseDto> createMonthlyBookingMap(List<Target> targets) {
        Map<Integer, Long> map = new TreeMap<>();
        for (Target target : targets) {
            LocalDate startDate = target.getStartDate();
            int monthOfYear = startDate.getMonth().getValue();
            if (!map.containsKey(monthOfYear)) {
                map.put(monthOfYear, Objects.isNull(target.getBookingCount()) ? 0 : target.getBookingCount());
            } else {
                map.put(monthOfYear, map.get(monthOfYear) + (Objects.isNull(target.getBookingCount()) ? 0 : target.getBookingCount()));
            }
        }

        return convertToTargetDtoObject(map);
    }

    public List<TargetWeekResponseDto> createWeeklyBookingDoneMap(List<Target> targets) {
        Map<Integer, Long> map = new TreeMap<>();
        for (Target target : targets) {
            LocalDate startDate = target.getStartDate();
            int weekOfYear = startDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());
            if (!map.containsKey(weekOfYear)) {
                map.put(weekOfYear, Objects.isNull(target.getBookingDoneCount()) ? 0 : target.getBookingDoneCount());
            } else {
                map.put(weekOfYear, map.get(weekOfYear) + (Objects.isNull(target.getBookingDoneCount()) ? 0 : target.getBookingDoneCount()));
            }
        }
        return convertToTargetDtoObject(map);
    }

    public List<TargetWeekResponseDto> createWeeklyBookingMap(List<Target> targets) {
        Map<Integer, Long> map = new TreeMap<>();
        for (Target target : targets) {
            LocalDate startDate = target.getStartDate();
            int weekOfYear = startDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());
            if (!map.containsKey(weekOfYear)) {
                map.put(weekOfYear, Objects.isNull(target.getBookingCount()) ? 0 : target.getBookingCount());
            } else {
                map.put(weekOfYear, map.get(weekOfYear) + (Objects.isNull(target.getBookingCount()) ? 0 : target.getBookingCount()));
            }
        }

        return convertToTargetDtoObject(map);
    }

    public List<TargetWeekResponseDto> convertToTargetDtoObject(Map<Integer, Long> targetsMap) {
        return targetsMap.entrySet().stream().map(k -> new TargetWeekResponseDto(k.getKey(), k.getValue())).collect(Collectors.toList());
    }
}
