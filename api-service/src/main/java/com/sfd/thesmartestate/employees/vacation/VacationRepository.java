package com.sfd.thesmartestate.employees.vacation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacationRepository extends JpaRepository<Vacation, Long> {
    List<Vacation> findByStatusAndCreatedForUsername(VacationStatus vacationStatus, String username);

    List<Vacation> findByCreatedForUsername(String username);

    List<Vacation> findByApprover(Long userId);

    List<Vacation> findVacationByApproverAndStatus(Long userId, VacationStatus status);

    List<Vacation> findVacationByApproverAndStatusNot(Long id, VacationStatus status);
}
