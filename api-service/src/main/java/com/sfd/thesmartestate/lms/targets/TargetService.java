package com.sfd.thesmartestate.lms.targets;

import com.sfd.thesmartestate.common.LeadEvents;
import com.sfd.thesmartestate.dashboard.dtos.TargetCountResponseDto;
import com.sfd.thesmartestate.users.entities.Employee;

import java.time.LocalDate;
import java.util.List;

public interface TargetService {
    Target create(Target target);

    List<Target> findByCreatedByAndStartDate(Employee employee, LocalDate startDate);

    Target update(Target target);

    Target findAndUpdateUserTarget(Employee employee, LeadEvents leadEvent);

    List<Target> findAll(boolean groupBy);

    void delete(Long targetId);

    List<TargetCountResponseDto> getVisitsTargetCount();
    List<TargetCountResponseDto> getVisitsWeeklyTargetCount();
    List<TargetCountResponseDto>  getVisitsMonthlyTargetCount();

    List<TargetCountResponseDto> getBookingsTargetCount();

    List<TargetCountResponseDto> getBookingsWeeklyTargetCount();

    List<TargetCountResponseDto> getBookingsMonthlyTargetCount();
}
