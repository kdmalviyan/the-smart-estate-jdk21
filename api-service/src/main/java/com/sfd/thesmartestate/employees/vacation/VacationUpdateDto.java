package com.sfd.thesmartestate.employees.vacation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacationUpdateDto {
    private Long vacationId;
    private Long userId;
    private String comment;
}
