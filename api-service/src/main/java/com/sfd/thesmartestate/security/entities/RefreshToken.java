package com.sfd.thesmartestate.security.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sfd.thesmartestate.employee.entities.Employee;
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

    @Column(name = "refresh_token", length = 5000)
    private String token;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;
}
