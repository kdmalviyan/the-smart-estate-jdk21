package com.sfd.thesmartestate.employees.vacation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VacationStatus {
    REQUESTED(1, "Requested", "Requested By User"),
    PENDING(2, "Pending", "Decision Pending with Supervisor"),
    APPROVED(3, "Approved", "Approved by Supervisor"),
    REJECTED(4, "Rejected", "Rejected By Supervisor"),
    CANCELLED(5, "Cancelled", "Request is cancelled");

    private final int id;
    private final String name;
    private final String description;
}
