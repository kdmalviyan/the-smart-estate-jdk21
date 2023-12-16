package com.sfd.thesmartestate.lms.dto;

import com.sfd.thesmartestate.lms.helpers.LeadTransferType;
import com.sfd.thesmartestate.projects.entities.Project;
import com.sfd.thesmartestate.users.entities.Employee;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import java.util.List;

@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class TransferLeadDto {
    private Employee assignedTo;
    private Project project;
    private List<Long> leadList;
    private LeadTransferType leadTransferType;
}
