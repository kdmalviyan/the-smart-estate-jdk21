package com.sfd.thesmartestate.projects.inventory;

import com.sfd.thesmartestate.projects.entities.InventoryStatus;
import com.sfd.thesmartestate.employee.entities.Employee;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_inventory")
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class Inventory implements Serializable, Comparable<Inventory> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "inventory_name")
    private String name;

    @Column(name = "inventory_size_sqr")
    private Long size;

    @Column(name = "is_corner")
    private boolean isCorner;

    @Column(name = "facing")
    private String facing;

    @Column(name = "selling_price")
    private Long sellingPrice;


    private Employee updatedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdateAt;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private InventoryStatus inventoryStatus;

    @Column(name = "tower")
    private String tower;


    @Override
    public int compareTo(Inventory o) {
        return this.getId().compareTo(o.getId());
    }
}
