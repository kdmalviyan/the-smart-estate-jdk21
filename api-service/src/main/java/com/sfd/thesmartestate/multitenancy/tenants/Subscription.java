package com.sfd.thesmartestate.multitenancy.tenants;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * @author kuldeep
 */
@Data
public class Subscription {
    private Long id;
    private boolean isActive;
    private boolean isExpired;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int numberOfUsersAllowed;
    private int numberOfRegisteredUsers;
    private int numberOfActiveUsers;
}
