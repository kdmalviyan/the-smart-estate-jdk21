package com.sfd.thesmartestate.notifications.entities;

import com.sfd.thesmartestate.notifications.enums.OTPTarget;
import com.sfd.thesmartestate.notifications.enums.OTPType;
import com.sfd.thesmartestate.employee.entities.Employee;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_otp")
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class OneTimePassword implements Serializable, Comparable<OneTimePassword> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String value;
    @Enumerated(EnumType.STRING)
    private OTPTarget target;

    @Enumerated(EnumType.STRING)
    private OTPType type;

    private String username;
    private boolean isUsed;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private Employee createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private Employee updatedBy;


    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdateAt;


    @Override
    public int compareTo(OneTimePassword otp) {
        return this.id.compareTo(otp.id);
    }
}
