package com.sfd.thesmartestate.dashboard.controllers;

import com.sfd.thesmartestate.common.dto.CountResponse;
import com.sfd.thesmartestate.dashboard.dtos.DashboardHomeResponse;
import com.sfd.thesmartestate.dashboard.dtos.TargetCountResponseDto;
import com.sfd.thesmartestate.dashboard.services.DashboardService;
import com.sfd.thesmartestate.lms.targets.TargetService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequestMapping(value = "dashboard")
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")
@CrossOrigin("*")
public class DashboardController {
    private final DashboardService dashboardService;
    private final TargetService targetService;

    @GetMapping("")
    public ResponseEntity<DashboardHomeResponse> home() {
        return ResponseEntity.ok(dashboardService.home());
    }

    @GetMapping("/totalLeads")
    public ResponseEntity<CountResponse> totalLead() {
        return ResponseEntity.ok(new CountResponse("totalLeadCount", dashboardService.totalLeads()));
    }

    @GetMapping("/activeLeads")
    public ResponseEntity<CountResponse> activeLead() {
        return ResponseEntity.ok(new CountResponse("activeLeadCount", dashboardService.activeLeads()));
    }

    @GetMapping("/followUps")
    public ResponseEntity<CountResponse> followUps() {
        CountResponse countResponse = new CountResponse("followupCount", dashboardService.followUps());
        log.info("count response {} ", countResponse);
        return ResponseEntity.ok(countResponse);
    }

    @GetMapping("/targets/visits")
    public ResponseEntity<List<TargetCountResponseDto>> targetVisitsCounts() {
        List<TargetCountResponseDto> targetVisitCountsMap = targetService.getVisitsTargetCount();
        return ResponseEntity.ok(targetVisitCountsMap);
    }

    @GetMapping("/targets/visits/weekly")
    public ResponseEntity<List<TargetCountResponseDto>> weeklyTargetVisitsCounts() {
        List<TargetCountResponseDto> targetVisitCountsMap = targetService.getVisitsWeeklyTargetCount();
        return ResponseEntity.ok(targetVisitCountsMap);
    }

    @GetMapping("/targets/visits/monthly")
    public ResponseEntity<List<TargetCountResponseDto>> monthlyTargetVisitsCounts() {
        List<TargetCountResponseDto> targetVisitCountsMap = targetService.getVisitsMonthlyTargetCount();
        return ResponseEntity.ok(targetVisitCountsMap);
    }

    @GetMapping("/targets/bookings")
    public ResponseEntity<List<TargetCountResponseDto>> targetBookingsCounts() {
        List<TargetCountResponseDto> targetBookingsCountsMap = targetService.getBookingsTargetCount();
        return ResponseEntity.ok(targetBookingsCountsMap);
    }

    @GetMapping("/targets/bookings/weekly")
    public ResponseEntity<List<TargetCountResponseDto>> weeklyTargetBookingsCounts() {
        List<TargetCountResponseDto> targetBookingsCountsMap = targetService.getBookingsWeeklyTargetCount();
        return ResponseEntity.ok(targetBookingsCountsMap);
    }

    @GetMapping("/targets/bookings/monthly")
    public ResponseEntity<List<TargetCountResponseDto>> monthlyTargetBookingsCounts() {
        List<TargetCountResponseDto> targetBookingsCountsMap = targetService.getBookingsMonthlyTargetCount();
        return ResponseEntity.ok(targetBookingsCountsMap);
    }
}
