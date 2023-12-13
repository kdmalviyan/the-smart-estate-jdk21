package com.sfd.thesmartestate.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ExceptionDto {
    private String message;
    private Integer status;
}
