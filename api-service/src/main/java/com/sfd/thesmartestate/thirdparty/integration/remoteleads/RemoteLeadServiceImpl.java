package com.sfd.thesmartestate.thirdparty.integration.remoteleads;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RemoteLeadServiceImpl implements RemoteLeadService {
    private final RemoteLeadRepository repository;

    @Override
    @Transactional
    public void saveAll(Collection<RemoteLead> leads) {
        repository.saveAll(leads);
    }

    @Override
    public List<RemoteLead> getAll() {
        return repository.findAll();
    }

    @Override
    public List<RemoteLead> getByAssigneeId(Long assigneeId) {
        return repository.findByAssigneeId(assigneeId);
    }

    @Override
    public RemoteLead findByCustomerPhoneAndProjectName(String phone, String name) {
        return repository.findByCustomerPhoneAndProjectName(phone, name);
    }

    @Override
    @Transactional
    public RemoteLead create(RemoteLead remoteLead) {
        return repository.save(remoteLead);
    }
}
