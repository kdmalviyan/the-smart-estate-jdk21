package com.sfd.thesmartestate.employees.vacation.type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VacationTypeRepository extends JpaRepository<VacationType, Integer> {

    @Query("SELECT vt FROM VacationType vt WHERE isActive = true")
    List<VacationType> findAllActive();
}
