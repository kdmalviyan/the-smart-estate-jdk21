package com.sfd.thesmartestate.dashboard.services;

import com.sfd.thesmartestate.dashboard.dtos.DashboardHomeResponse;
import com.sfd.thesmartestate.lms.followups.FollowupService;
import com.sfd.thesmartestate.lms.services.LeadService;
import com.sfd.thesmartestate.lms.targets.TargetService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class DashboardService {

    private final LeadService leadService;
    private final FollowupService followupService;
    private final TargetService targetService;

    public DashboardHomeResponse home() {

        return DashboardHomeResponse.builder()
                .withTotalLeadCount(totalLeads())
                .withActiveLeadCount(activeLeads())
                .withFollowupCount(followUps())
                .withTargetVisitsCounts(targetService.getVisitsTargetCount())
                .withWeeklyTargetVisitsCounts(targetService.getVisitsWeeklyTargetCount())
                .withMonthlyTargetVisitsCounts(targetService.getVisitsMonthlyTargetCount())
                .withTargetBookingsCounts(targetService.getBookingsTargetCount())
                .withWeeklyTargetBookingsCounts(targetService.getBookingsWeeklyTargetCount())
                .withMonthlyTargetBookingsCounts(targetService.getBookingsMonthlyTargetCount())
                .build();

    }

    public Long totalLeads() {
        return leadService.findAllCount();
    }

    public Long activeLeads() {
        return leadService.findCountByAllActiveStatus();
    }

    public Long followUps() {
        return followupService.findAllOpenCount();
    }
}
