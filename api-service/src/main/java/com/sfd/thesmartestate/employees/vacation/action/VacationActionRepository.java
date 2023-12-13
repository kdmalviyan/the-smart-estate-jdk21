package com.sfd.thesmartestate.employees.vacation.action;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacationActionRepository extends JpaRepository<VacationAction, Long> {
    List<VacationAction> userId(Long userId);

    List<VacationAction> findByVacationId(Long vacationId);

    List<VacationAction> findByVacationIdAndUserId(Long vacationId, Long userId);
}
