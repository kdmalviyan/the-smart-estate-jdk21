package com.sfd.thesmartestate.common.responsemapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MobileResponseDto {
    private String message;
    private Object resultData;
    private String result;
}
