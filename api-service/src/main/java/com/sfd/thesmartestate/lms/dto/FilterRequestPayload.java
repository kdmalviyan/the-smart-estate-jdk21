package com.sfd.thesmartestate.lms.dto;

import com.sfd.thesmartestate.lms.entities.Budget;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class FilterRequestPayload {
    private int pageSize;
    private int pageNumber;
    private String orderDir;
    private String orderColumn;
    private String assignedTo;
    private String status;
    private String source;
    private String type;
    private String searchText;
    private String startDate;
    private String endDate;
    private String deactivationReason;
    private String project;
    private String leadInventorySize;
    private Budget budget;
    private boolean isOpen;
}
