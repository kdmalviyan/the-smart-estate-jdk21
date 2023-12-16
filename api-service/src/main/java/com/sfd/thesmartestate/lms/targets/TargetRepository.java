package com.sfd.thesmartestate.lms.targets;

import com.sfd.thesmartestate.projects.entities.Project;
import com.sfd.thesmartestate.employee.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface TargetRepository extends JpaRepository<Target, Long> {
    List<Target> findByCreatedBy(Employee employee);

    List<Target> findByCreatedByIn(List<Employee> employees);


    List<Target> findAllByCreatedByAndProjectAndStartDateGreaterThanEqualAndEndDateLessThanEqual(Employee employee, Project project, LocalDate startDate, LocalDate endDate);

    List<Target> findByCreatedByAndStartDate(Employee employee, LocalDate startDate);

    Collection<Target> findByAssignedTo(Employee loggedInEmployee);

    Collection<Target> findByActive(boolean active);

    Collection<Target> findByActiveAndAssignedTo(boolean active, Employee loggedInEmployee);
}
