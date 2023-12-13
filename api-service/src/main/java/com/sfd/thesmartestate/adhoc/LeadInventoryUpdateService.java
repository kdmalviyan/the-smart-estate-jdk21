package com.sfd.thesmartestate.adhoc;

public interface LeadInventoryUpdateService {
    void updateLeadInventorySize(UpdateInventorySizeDTO updateInventorySizeDTO, String referenceId);
    void updateDuplicateLead(String referenceId);

}
