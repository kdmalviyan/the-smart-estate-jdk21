package com.sfd.thesmartestate.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetadataResponse {
    private Long id;
    private String name;
    private boolean enabled;
    private String description;
}
