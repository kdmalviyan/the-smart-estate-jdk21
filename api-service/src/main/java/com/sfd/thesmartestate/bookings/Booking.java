package com.sfd.thesmartestate.bookings;

import com.sfd.thesmartestate.customer.entities.Customer;
import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.projects.entities.Project;
import com.sfd.thesmartestate.projects.inventory.Inventory;
import com.sfd.thesmartestate.users.entities.Employee;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_bookings")
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer buyer;

    private String coBuyer;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Project.class)
    private Project project;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Inventory.class)
    private Inventory inventory;

    private Double sellingPrice;

    private Double discount;

    private float discountPercent;

    @ManyToOne
    @JoinColumn(name = "business_executive_id")
    private Employee businessExecutive;

    @ManyToOne
    @JoinColumn(name = "business_manager_id")
    private Employee businessManager;

    @ManyToOne
    @JoinColumn(name = "business_head_id")
    private Employee businessHead;

    private String channelPartner;

    private String applicationFormPath;

    private String buyerPanCardPath;

    private String coBuyerPanCardPath;

    private String buyerAadhaarCardPath;

    private String coBuyerAadhaarCardPath;

    private String paymentCopyPath;

    private String remark;

    private LocalDateTime createdAt;

    private LocalDateTime bookingDate;

    @Column(columnDefinition = "boolean default true")
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private Employee createdBy;

    private LocalDateTime lastUpdatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private Employee updatedBy;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    private Lead lead;
}
