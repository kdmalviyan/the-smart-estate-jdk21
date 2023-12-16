package com.sfd.thesmartestate.lms.services;

import com.sfd.thesmartestate.lms.entities.LeadSource;
import com.sfd.thesmartestate.lms.exceptions.LeadException;
import com.sfd.thesmartestate.lms.repositories.LeadSourceRepository;
import com.sfd.thesmartestate.users.services.EmployeeService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class LeadSourceServiceImpl implements LeadSourceService {

    private final LeadSourceRepository leadSourceRepository;
    private EmployeeService employeeService;

    @Override
    public List<LeadSource> findAll() {
        return leadSourceRepository.findAll().stream().filter(LeadSource::getActive).sorted(Comparator.comparing(LeadSource::getLastUpdateAt).reversed()).collect(Collectors.toList());
    }

    @Override
    public LeadSource create(LeadSource leadSource) {
        LeadSource existingLeadSource = findByName(createLeadSourceName(leadSource.getName()));
        if (Objects.isNull(existingLeadSource)) {
            leadSource.setName(createLeadSourceName(leadSource.getName()));
            leadSource.setCreatedAt(LocalDateTime.now());
            leadSource.setCreatedBy(employeeService.findLoggedInEmployee());
            leadSource.setLastUpdateAt(LocalDateTime.now());
            leadSource.setUpdatedBy(employeeService.findLoggedInEmployee());
            leadSource.setActive(true);
            return leadSourceRepository.save(leadSource);
        } else {
            throw new LeadException("Lead Source already Exist");
        }
    }

    @Override
    public LeadSource update(LeadSource leadSource) {
        LeadSource existingLeadSource = findById(leadSource.getId());
        if (Objects.nonNull(existingLeadSource)) {
            if (Objects.isNull(findByName(createLeadSourceName(leadSource.getName()))) || existingLeadSource.getName().equals(createLeadSourceName(leadSource.getName()))) {
                existingLeadSource.setName(createLeadSourceName(leadSource.getName()));
                existingLeadSource.setDescription(leadSource.getDescription());
                existingLeadSource.setLastUpdateAt(LocalDateTime.now());
                existingLeadSource.setUpdatedBy(employeeService.findLoggedInEmployee());
                return leadSourceRepository.save(existingLeadSource);
            } else {
                throw new LeadException("Lead Source already exists with this name");
            }
        } else {
            throw new LeadException("Lead Source not Found");
        }
    }

    @Override
    public LeadSource findById(Long id) {
        return leadSourceRepository.findById(id).orElse(null);
    }

    @Override
    public LeadSource findByName(String leadSourceName) {
        return leadSourceRepository.findByName(leadSourceName).orElse(null);
    }


    @Override
    public LeadSource deactivateLeadSource(Long id) {
        LeadSource leadSource = findById(id);
        if (Objects.nonNull(leadSource)) {
            leadSource.setActive(false);
            leadSource.setLastUpdateAt(LocalDateTime.now());
            leadSource.setUpdatedBy(employeeService.findLoggedInEmployee());
            return leadSourceRepository.save(leadSource);
        } else {
            throw new LeadException("Lead Source Not Found");
        }
    }

    @Override
    public long count() {
        return leadSourceRepository.count();
    }

    @Override
    public String createLeadSourceName(String leadDescription) {
        if(Objects.nonNull(leadDescription)) {
            String replaceText = leadDescription.stripLeading().stripTrailing();
            replaceText = replaceText.replace(' ', '_');
            replaceText = replaceText.replace('-', '_');
            replaceText = replaceText.toUpperCase();
            return replaceText;
        }
        return "";
    }
}
