package com.sfd.thesmartestate.lms.followups;

import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.users.entities.User;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_followups")
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class Followup implements Serializable, Comparable<Followup> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "followup_message")
    private String followupMessage;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    private Lead lead;

    @Column(name = "followup_time")

    private LocalDateTime followupTime;

    @Column(name = "is_open")
    private boolean isOpen = true;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;

    @Column(name = "created_at")

    private LocalDateTime createdAt;

    @Column(name = "last_updated_at")

    private LocalDateTime lastUpdateAt;

    @Override
    public int compareTo(Followup followup) {
        return followupTime.compareTo(followup.followupTime);
    }
}