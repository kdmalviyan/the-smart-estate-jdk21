package com.sfd.thesmartestate.employee.entities;

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
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_employees")
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class Employee implements UserDetails, Comparable<Employee> {
    public Employee() {
        this.loginDetails = new LoginDetails();
    }
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

    @Column(name = "gender")
    private String gender;

    @Column(name = "vendor_id")
    private String vendor;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "alternate_phone")
    private String alternatePhone;

    @Column(nullable = false)
    private boolean active;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "supervisor_id")
    private Employee supervisor;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Employee> subordinates;

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

    @OneToOne(cascade = CascadeType.PERSIST)
    private LoginDetails loginDetails;

    @Column(name = "employee_unique_id")
    private String employeeUniqueId;

    @Override
    public int compareTo(Employee o) {
        return loginDetails.getUsername().compareTo(o.loginDetails.getUsername());
    }

    public void setRoles(Set<Role> roles) {
        if (Objects.nonNull(roles) && roles.contains(new Role(Constants.ADMIN))) {
            this.setAdmin(true);
        }
        if (Objects.nonNull(roles) && roles.contains(new Role(Constants.SUPERADMIN))) {
            this.setSuperAdmin(true);
        }
        loginDetails.setRoles(roles);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return loginDetails.getUsername().equals(employee.loginDetails.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(loginDetails.getUsername());
    }

    public Set<Role> getRoles() {
        return loginDetails.getRoles();
    }

    public void setPassword(String password) {
        loginDetails.setPassword(password);
    }
    public String getPassword() {
        return loginDetails.getPassword();
    }

    public String getUsername() {
        return loginDetails.getUsername();
    }

    //TODO: Need to delete following methods or properties
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return loginDetails.getAuthorities();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }


    private String username;

    private String password;
}
