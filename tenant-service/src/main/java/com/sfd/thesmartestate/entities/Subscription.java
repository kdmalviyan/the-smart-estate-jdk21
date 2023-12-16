package com.sfd.thesmartestate.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author kuldeep
 */
@Data
@Entity
@Table(name = "tb_tenant_subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isActive;
    private boolean isExpired;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int numberOfUsersAllowed;
    private int numberOfRegisteredUsers;
    private int numberOfActiveUsers;
}
