package com.sfd.thesmartestate.projects.entities;

import com.sfd.thesmartestate.projects.inventory.Inventory;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tb_projects")
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class Project implements Serializable, Comparable<Project> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = FileMetadata.class)
    private FileMetadata brochure;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "tb_project_price_file",
            joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "price_file_id", referencedColumnName = "id")
    )
    @Column(name = "price_file")
    private Set<FileMetadata> priceFiles;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "tb_projects_inventories",
            joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "inventory_id", referencedColumnName = "id")
    )
    private Set<Inventory> inventories;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdateAt;

    @Column(name = "is_enable")
    private boolean enabled;

    @Override
    public int compareTo(Project o) {
        return this.getId().compareTo(o.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        return name.equals(project.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
