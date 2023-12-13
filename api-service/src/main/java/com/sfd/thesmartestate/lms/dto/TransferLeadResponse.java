package com.sfd.thesmartestate.lms.dto;

import com.sfd.thesmartestate.importdata.dto.ErrorDto;
import com.sfd.thesmartestate.lms.entities.Lead;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class TransferLeadResponse {
    private List<Lead> leadList;
    List<ErrorDto> errors = new ArrayList<>();

}
