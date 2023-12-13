package com.sfd.thesmartestate.lms.dto;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class PageableFollowupResponse {
    private long count;
    private List<FollowupResponseDto> leads;
}
