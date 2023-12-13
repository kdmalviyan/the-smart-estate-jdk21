package com.sfd.thesmartestate.common.entities;

import com.sfd.thesmartestate.uipermissions.PermissionTab;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_roles")
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class Role implements Serializable, Comparable<Role> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "display_name")
    private String displayName;

    public Role(String roleName) {
        this.name = roleName;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<PermissionTab> uiPermissions;

    public Role() {
    }


    @Override
    public int compareTo(Role role) {
        return this.getName().compareTo(role.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
