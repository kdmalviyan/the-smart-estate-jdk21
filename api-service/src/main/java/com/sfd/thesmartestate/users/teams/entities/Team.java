package com.sfd.thesmartestate.users.teams.entities;

import com.sfd.thesmartestate.projects.entities.Project;
import com.sfd.thesmartestate.users.entities.User;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tb_teams")
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class Team implements Serializable, Comparable<Team> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supervisor_id", referencedColumnName = "id")
    private User supervisor;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tb_team_members",
            joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id")
    )
    private Set<User> members;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdateAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @Override
    public int compareTo(Team o) {
        return this.name.compareTo(o.name);
    }
}
