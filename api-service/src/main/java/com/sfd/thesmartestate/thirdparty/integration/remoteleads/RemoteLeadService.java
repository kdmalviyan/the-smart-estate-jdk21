package com.sfd.thesmartestate.thirdparty.integration.remoteleads;

import java.util.Collection;
import java.util.List;

public interface RemoteLeadService {
    void saveAll(Collection<RemoteLead> leads);
    List<RemoteLead> getAll();
    List<RemoteLead> getByAssigneeId(Long assigneeId);
    RemoteLead findByCustomerPhoneAndProjectName(String phone, String name);
    RemoteLead create(RemoteLead remoteLead);
}
