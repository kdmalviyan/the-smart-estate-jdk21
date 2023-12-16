package com.sfd.thesmartestate.projects.entities;

import com.sfd.thesmartestate.employee.entities.Employee;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_inventory_status")
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class InventoryStatus implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status_name")
    private String name;

    @Column(name = "status_description")
    private String description;

    @Column(name = "created_at")

    private LocalDateTime createdAt;

    @Column(name = "last_updated_at")

    private LocalDateTime lastUpdateAt;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private Employee updatedBy;

    @Column(name = "is_active")
    private Boolean active;

}
