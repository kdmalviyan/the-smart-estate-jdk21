package com.sfd.thesmartestate.common.responsemapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDto {
    private String message;
    private Long status;
}
