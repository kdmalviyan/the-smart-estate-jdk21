package com.sfd.thesmartestate.dashboard.dtos;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Builder(setterPrefix = "with")
@Slf4j
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class DashboardHomeResponse {
    private Long totalLeadCount;
    private Long activeLeadCount;
    private Long followupCount;
    private List<TargetCountResponseDto> targetVisitsCounts;
    private List<TargetCountResponseDto> weeklyTargetVisitsCounts;
    private List<TargetCountResponseDto> monthlyTargetVisitsCounts;
    private List<TargetCountResponseDto> targetBookingsCounts;
    private List<TargetCountResponseDto> weeklyTargetBookingsCounts;
    private List<TargetCountResponseDto> monthlyTargetBookingsCounts;


}
