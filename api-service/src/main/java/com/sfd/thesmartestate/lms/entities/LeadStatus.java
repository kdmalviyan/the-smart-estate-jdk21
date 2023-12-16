package com.sfd.thesmartestate.lms.entities;

import com.sfd.thesmartestate.employee.entities.Employee;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_lead_status")
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class LeadStatus implements Serializable, Comparable<LeadSource> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lead_status_name")
    private String name;

    @Column(name = "lead_status_description")
    private String description;

    @Column(name = "created_at")

    private LocalDateTime createdAt;

    @Column(name = "last_updated_at")

    private LocalDateTime lastUpdateAt;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private Employee createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private Employee updatedBy;

    @Column(name = "is_active")
    private Boolean active;

    @Override
    public int compareTo(LeadSource leadSource) {
        return this.getName().compareTo(leadSource.getName());
    }
}
