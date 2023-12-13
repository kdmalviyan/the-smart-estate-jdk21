package com.sfd.thesmartestate.employees.vacation;

import java.util.List;

public interface VacationService {
    Vacation create(Vacation vacation);
    Vacation reject(VacationUpdateDto vacationUpdateDto);
    Vacation approve(VacationUpdateDto vacationUpdateDto);

    List<Vacation> findByStatus(VacationStatus vacationStatus);

    Vacation changeStatus(Long vacationId, VacationStatus fromStatus, VacationStatus toStatus);

    List<Vacation> findMyVacations();

    List<Vacation> findVacationByApprover(Long userId);

    List<Vacation> findVacationByApproverAndStatus(Long userId, VacationStatus status);

    List<Vacation> findVacationApprovedByMe();

    List<Vacation> findVacationForMyApproval();
}
