package com.sfd.thesmartestate.adhoc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DuplicateResponse {
    private long recordCount;
    private long customerId;
    private long projectId;

}