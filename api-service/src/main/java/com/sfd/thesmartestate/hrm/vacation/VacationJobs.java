package com.sfd.thesmartestate.hrm.vacation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class VacationJobs {
    @Value("${vacation.approval.auto-approved-time}")
    private int autoApprovalDelay;

    private final VacationService service;

    @Scheduled(cron = "0 0 06 * * *") // 8PM every day
    // @Scheduled(cron = "0 0/2 * * * *") // every min
    public void autoApproveVacations() {
        List<Vacation> vacations = service.findByStatus(VacationStatus.REQUESTED);

    }
}
