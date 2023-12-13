package com.sfd.thesmartestate.employees.vacation.action;

import com.sfd.thesmartestate.employees.vacation.exceptions.VacationException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_vacation_actions")
@Data
@Slf4j
public class VacationAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name; // Cancelled, Rejected, Reverted etc

    @Column(name = "comment")
    private String comment;

    @Column(name = "action_date")
    private LocalDateTime actionDateTime; // Can't be updated

    @Column(name = "vacation_id")
    private Long vacationId;

    @Column(name = "action_taken_by_id")
    private Long userId; // Action taken by

    public void validate() {
        if(Objects.isNull(name) || Objects.equals("", name.trim())) {
            throw new VacationException("Vacation Action can not be null or empty.");
        }
        if(Objects.isNull(vacationId)) {
            throw new VacationException("Vacation Id can not be null");
        }
        if(Objects.isNull(userId)) {
            throw new VacationException("Action Taken By can not be null");
        }
    }
}
