package com.sfd.thesmartestate.employees.vacation.action;

import java.util.List;

public interface VacationActionService {
    VacationAction create(VacationAction vacationAction);
    List<VacationAction> findByUserId(Long userId);
    VacationAction findById(Long id);
    List<VacationAction> findByVacationId(Long vacationId);

    List<VacationAction> findByVacationIdAndUserId(Long vacationId, Long userId);
}
