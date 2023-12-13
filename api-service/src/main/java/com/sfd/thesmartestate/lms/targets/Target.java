package com.sfd.thesmartestate.lms.targets;

import com.sfd.thesmartestate.projects.entities.Project;
import com.sfd.thesmartestate.users.entities.User;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_target")
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class Target implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "booking")
    private Long bookingCount;

    @Column(name = "booking_done")
    private Long bookingDoneCount;

    @Column(name = "siteVisit")
    private Long siteVisitCount;

    @Column(name = "siteVisit_done")
    private Long siteVisitDoneCount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "duration")
    private String duration;

    @Column(columnDefinition = "boolean default true")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}