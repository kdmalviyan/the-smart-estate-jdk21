package com.sfd.thesmartestate.lms.dto;

import com.sfd.thesmartestate.lms.entities.Comment;
import com.sfd.thesmartestate.lms.entities.DeactivationReason;
import com.sfd.thesmartestate.lms.entities.LeadStatus;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

@Data
@SuppressFBWarnings("EI_EXPOSE_REP")
public class DeactivateLeadDto {
    private DeactivationReason deactivationReason;
    private Comment comment;
    private LeadStatus leadStatus;
}
