package com.sfd.thesmartestate.lms.rawleads;

import com.sfd.thesmartestate.lms.dto.PageableFilterDto;
import com.sfd.thesmartestate.lms.dto.PageableRawLeadsResponse;

import java.util.Collection;
import java.util.List;

public interface RawLeadService {
    void saveAll(Collection<RawLead> leads);
    List<RawLead> getAll();
    List<RawLead> getByAssigneeId(Long assigneeId);
    RawLead findByCustomerPhone(String phone);
    RawLead create(RawLead remoteLead);
    RawLead updateConvertedLeadStatus(Long id);
    PageableRawLeadsResponse findPageableAll(PageableFilterDto pageableFilterDto);


}
