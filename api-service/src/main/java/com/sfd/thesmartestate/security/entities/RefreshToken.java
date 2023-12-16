package com.sfd.thesmartestate.security.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sfd.thesmartestate.users.entities.Employee;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "tb_refresh_tokens")
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class RefreshToken {
    public RefreshToken(){}
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private Employee employee;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;
}
