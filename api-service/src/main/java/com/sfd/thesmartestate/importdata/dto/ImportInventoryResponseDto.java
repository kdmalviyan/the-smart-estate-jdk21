package com.sfd.thesmartestate.importdata.dto;

import com.sfd.thesmartestate.projects.inventory.Inventory;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class ImportInventoryResponseDto {

    private String message;
    private List<String> errors;
    private List<String> warnings;
    private int rowsSkipped;
    private int rowsSuccess;
    private Set<Inventory> inventories;
}
