package com.sfd.thesmartestate.dashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
public class TargetWeekResponseDto {
    private Integer weekNo;
    private Long value;
}
