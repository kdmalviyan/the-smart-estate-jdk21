package com.sfd.thesmartestate.importdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDto {
    private String message;
    private String messageType;
    private int rowNumber;
    private long leadId;
}
