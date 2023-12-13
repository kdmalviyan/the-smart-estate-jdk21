package com.sfd.thesmartestate.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class CountResponse {
    private String name;
    private long count;
}
