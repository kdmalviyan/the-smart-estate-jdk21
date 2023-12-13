package com.sfd.thesmartestate.dashboard.dtos;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(setterPrefix = "with")
@SuppressFBWarnings("EI_EXPOSE_REP")

public class TargetCountResponseDto {
    private String projectName;
    private String targetType;

    private List<TargetWeekResponseDto> targetCountMap;
    private List<TargetWeekResponseDto> targetDoneCountMap;
}
