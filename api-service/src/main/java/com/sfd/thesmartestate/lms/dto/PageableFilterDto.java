package com.sfd.thesmartestate.lms.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(setterPrefix = "with")
public class PageableFilterDto {
    private int pageSize;
    private int pageNumber;
    private String orderDir;
    private String orderColumn;
    private String assignedTo;
    private String status;
    private String type;
    private String searchText;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String deactivationReason;
    private String project;
    private String leadInventorySize;
    private Double budgetFrom;
    private Double budgetTo;
    private String source;
    private boolean isOpen;
}
