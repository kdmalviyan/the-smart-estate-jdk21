package com.sfd.thesmartestate.importdata.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import java.util.List;

@Data
@SuppressFBWarnings("EI_EXPOSE_REP")
public class ImportResponseDto {

    private String message;
    private List<ErrorDto> errors;
    private List<ErrorDto> warnings;
    private int rowsSkipped;
    private int rowsSuccess;
}
