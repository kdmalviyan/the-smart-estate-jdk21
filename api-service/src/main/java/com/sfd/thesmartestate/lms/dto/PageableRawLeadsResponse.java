package com.sfd.thesmartestate.lms.dto;

import com.sfd.thesmartestate.lms.rawleads.RawLead;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class PageableRawLeadsResponse {
    private long count;
    private List<RawLead> rawLeads;
}
