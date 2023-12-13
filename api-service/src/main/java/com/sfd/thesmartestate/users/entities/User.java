package com.sfd.thesmartestate.users.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sfd.thesmartestate.common.Constants;
import com.sfd.thesmartestate.common.entities.Role;
import com.sfd.thesmartestate.projects.entities.Project;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_users")
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class User implements UserDetails, Comparable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "gender")
    private String gender;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "vendor_id")
    private String vendor;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "alternate_phone")
    private String alternatePhone;

    @Column(nullable = false)
    private boolean enabled;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "supervisor_id")
    private User supervisor;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<User> subordinates;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<Role> roles;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdateAt;

    private boolean isAdmin;

    private boolean isSuperAdmin;

    @Column(name = "profile_image_path")
    private String profileImagePath;

    @Column(name = "profile_image_thumb_path")
    private String profileImageThumbPath;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        roles.forEach(e -> grantedAuthorities.add((GrantedAuthority) e::getName));
        return grantedAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public int compareTo(User o) {
        return this.getUsername().compareTo(o.getUsername());
    }

    public void setRoles(Set<Role> roles) {
        if (Objects.nonNull(roles) && roles.contains(new Role(Constants.ADMIN))) {
            this.setAdmin(true);
        }
        if (Objects.nonNull(roles) && roles.contains(new Role(Constants.SUPERADMIN))) {
            this.setSuperAdmin(true);
        }
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
