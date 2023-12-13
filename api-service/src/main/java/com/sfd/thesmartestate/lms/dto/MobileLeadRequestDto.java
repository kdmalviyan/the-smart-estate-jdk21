package com.sfd.thesmartestate.lms.dto;

import com.sfd.thesmartestate.lms.entities.Budget;
import lombok.Data;

@Data
public class MobileLeadRequestDto {
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerAlternatePhone;
    private Long source;
    private Long project;
    private Long type;
    private Long assignedTo;
    private Long status;
    private Long leadInventorySize;
    private String comments;
    Budget budget;

}
