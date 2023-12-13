package com.sfd.thesmartestate.lms.rawleads;

import com.sfd.thesmartestate.thirdparty.exceptions.ThirdPartyExceptions;
import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Data
@Table(name = "tb_raw_leads")
public class RawLead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // START: Required parameter from remote
    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "lead_date")
    private LocalDate leadDate;
    // END: Required parameter from remote

    @Column(name = "is_lead_created")
    private boolean isLeadCreated;

    @Column(name = "is_lead_archived")
    private boolean isArchived;

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by_id")
    private Long updatedById;

    @Column(name = "assignee_id")
    private Long assigneeId;

    @Column(name = "is_lead_converted")
    private boolean isLeadConverted;

    public void validate() {
        if(Objects.isNull(this.customerPhone) || Objects.equals("", this.customerPhone.trim())
        || Objects.isNull(this.customerName) || Objects.equals("", this.customerName.trim())){
            throw new ThirdPartyExceptions("Customer name And phone is empty, remote lead can not be created.");
        }
    }
}
