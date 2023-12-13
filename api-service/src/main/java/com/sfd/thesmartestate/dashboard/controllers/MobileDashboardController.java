package com.sfd.thesmartestate.dashboard.controllers;

import com.sfd.thesmartestate.dashboard.dtos.DashboardHomeResponse;
import com.sfd.thesmartestate.dashboard.services.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping(value = "mobile/dashboard")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MobileDashboardController {
    private final DashboardService dashboardService;

    @GetMapping("")
    public ResponseEntity<DashboardHomeResponse> home() {
        return ResponseEntity.ok(dashboardService.home());
    }

}
