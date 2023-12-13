package com.sfd.thesmartestate.lms.entities;

import com.sfd.thesmartestate.customer.entities.Customer;
import com.sfd.thesmartestate.lms.calls.Call;
import com.sfd.thesmartestate.lms.exceptions.LeadException;
import com.sfd.thesmartestate.projects.entities.Project;
import com.sfd.thesmartestate.users.entities.User;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tb_leads")
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class Lead implements Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Customer.class)
    private Customer customer;

    // Mandatory
    @ManyToOne
    @JoinColumn(name = "source_id")
    private LeadSource source;

    // Mandatory
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private LeadType type; //Hot/Warm/Cold;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "leadInventorySize_id")
    private LeadInventorySize leadInventorySize;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private LeadStatus status; //Visit Done, Followup

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "tb_leads_comments",
            joinColumns = @JoinColumn(name = "lead_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id")
    )
    @OrderBy("createdAt DESC")
    private Set<Comment> comments;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;

    @Column(name = "last_updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime lastUpdateAt;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = LeadAssignHistory.class)
    @OrderBy("assignmentTime DESC")
    private Set<LeadAssignHistory> leadAssignHistory;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Budget.class)
    private Budget budget;

    @Column(name = "site_visit")
    private boolean siteVisit = false;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = DeactivationReason.class)
    private DeactivationReason deactivationReason;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "tb_leads_calls",
            joinColumns = @JoinColumn(name = "lead_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "call_id", referencedColumnName = "id")
    )
    @OrderBy("createdAt DESC")
    private Set<Call> calls;

    @Override
    public Object clone() {
        try {
            Lead lead = (Lead) super.clone();
            lead.setId(null);
            return lead;
        } catch (CloneNotSupportedException e) {
            throw new LeadException("Unable to copy leads");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lead lead = (Lead) o;

        if (!customer.equals(lead.customer)) return false;
        return project.equals(lead.project);
    }

    @Override
    public int hashCode() {
        int result = customer.hashCode();
        result = 31 * result + project.hashCode();
        return result;
    }
}
