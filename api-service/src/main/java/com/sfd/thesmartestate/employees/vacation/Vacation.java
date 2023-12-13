package com.sfd.thesmartestate.employees.vacation;

import com.sfd.thesmartestate.employees.vacation.action.VacationAction;
import com.sfd.thesmartestate.employees.vacation.exceptions.VacationException;
import com.sfd.thesmartestate.employees.vacation.type.VacationType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_vacations")
@Data
@Slf4j
public class Vacation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_half_day", columnDefinition = "boolean default false")
    private boolean isHalfDay;

    @Column(name = "is_full_day", columnDefinition = "boolean default false")
    private boolean isFullDay;

    @Column(name = "is_first_half", columnDefinition = "boolean default false")
    private boolean isFirstHalf;

    @Column(name = "is_second_half", columnDefinition = "boolean default false")
    private boolean isSecondHalf;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private VacationStatus status;

    @Column(name = "is_auto_approved", columnDefinition = "boolean default true")
    private boolean isAutoApproved;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id")
    private VacationType vacationType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_for")
    private String createdForUsername;

    @Column(name = "approver_id")
    private Long approver;

    @Lob  
    @Column(name = "reason")
    private String reason;

    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<VacationAction> actions;

    public void validate() {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            throw new VacationException("Please select proper dates for vacation.");
        }
        if (Objects.isNull(createdForUsername)) {
            throw new VacationException("You should provide user id for which vacation is being created.");
        }
        if (Objects.isNull(vacationType)) {
            throw new VacationException("Please select vacation type.");
        }
        if (Objects.isNull(reason)) {
            log.warn("Please provide reason for the vacation.");
        }
    }
}
