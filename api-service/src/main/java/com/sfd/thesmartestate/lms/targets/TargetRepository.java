package com.sfd.thesmartestate.lms.targets;

import com.sfd.thesmartestate.projects.entities.Project;
import com.sfd.thesmartestate.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface TargetRepository extends JpaRepository<Target, Long> {
    List<Target> findByCreatedBy(User user);

    List<Target> findByCreatedByIn(List<User> users);


    List<Target> findAllByCreatedByAndProjectAndStartDateGreaterThanEqualAndEndDateLessThanEqual(User user, Project project, LocalDate startDate, LocalDate endDate);

    List<Target> findByCreatedByAndStartDate(User user, LocalDate startDate);

    Collection<Target> findByAssignedTo(User loggedInUser);

    Collection<Target> findByActive(boolean active);

    Collection<Target> findByActiveAndAssignedTo(boolean active, User loggedInUser);
}
