package com.sfd.thesmartestate.lms.rawleads;

import com.sfd.thesmartestate.lms.dto.PageableFilterDto;
import com.sfd.thesmartestate.lms.dto.PageableRawLeadsResponse;
import com.sfd.thesmartestate.lms.helpers.PageableFilterRequestHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RawLeadServiceImpl implements RawLeadService {
    private final RawLeadRepository repository;
    private final PageableFilterRequestHelper pageableFilterRequestHelper;

    @Override
    @Transactional
    public void saveAll(Collection<RawLead> leads) {
        repository.saveAll(leads);
    }

    @Override
    public List<RawLead> getAll() {
        return repository.findAll();
    }

    @Override
    public List<RawLead> getByAssigneeId(Long assigneeId) {
        return repository.findByAssigneeId(assigneeId);
    }

    @Override
    public RawLead findByCustomerPhone(String phone) {
        return repository.findByCustomerPhone(phone);
    }

    @Override
    @Transactional
    public RawLead create(RawLead remoteLead) {
        return repository.save(remoteLead);
    }

    @Override
    public RawLead updateConvertedLeadStatus(Long id) {
        RawLead rawLead = repository.findById(id).get();
        rawLead.setLeadConverted(true);
        return repository.save(rawLead);
    }

    public PageableRawLeadsResponse findPageableAll(PageableFilterDto pageableFilterDto) {
        TypedQuery<RawLead> query = pageableFilterRequestHelper.createExecutableQueryRawLead(pageableFilterDto);
        List<RawLead> leads = query.getResultList();
        return new PageableRawLeadsResponse(pageableFilterRequestHelper.getCountOfAvailableRawLeadItems(pageableFilterDto), leads);

    }
}
