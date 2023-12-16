package com.sfd.thesmartestate.lms.services;

import com.sfd.thesmartestate.lms.entities.LeadType;
import com.sfd.thesmartestate.lms.exceptions.LeadException;
import com.sfd.thesmartestate.lms.repositories.LeadTypeRepository;
import com.sfd.thesmartestate.employee.services.EmployeeService;
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

public class LeadTypeServiceImpl implements LeadTypeService {

    private final LeadTypeRepository leadTypeRepository;
    private EmployeeService employeeService;

    @Override
    public List<LeadType> findAll() {
        return leadTypeRepository.findAll().stream().filter(LeadType::getActive).sorted(Comparator.comparing(LeadType::getLastUpdateAt).reversed()).collect(Collectors.toList());
    }

    @Override
    public LeadType create(LeadType leadType) {
        LeadType existingLeadType = findByType(createLeadType(leadType.getName()));
        if (Objects.isNull(existingLeadType)) {
            leadType.setName(createLeadType(leadType.getName()));
            leadType.setCreatedBy(employeeService.findLoggedInEmployee());
            leadType.setLastUpdateAt(LocalDateTime.now());
            leadType.setUpdatedBy(employeeService.findLoggedInEmployee());
            leadType.setCreatedAt(LocalDateTime.now());
            leadType.setActive(true);
            return leadTypeRepository.save(leadType);
        } else {
            throw new LeadException("Lead Type already present with this Name");
        }
    }

    @Override
    public LeadType update(LeadType leadType) {
        LeadType existingLeadType = findById(leadType.getId());
        if (Objects.nonNull(existingLeadType)) {
            if (Objects.isNull(findByType(createLeadType(leadType.getName())))) {
                existingLeadType.setName(createLeadType(leadType.getName()));
                existingLeadType.setDescription(leadType.getDescription());
                existingLeadType.setUpdatedBy(employeeService.findLoggedInEmployee());
                existingLeadType.setLastUpdateAt(LocalDateTime.now());
                return leadTypeRepository.save(existingLeadType);
            } else {
                throw new LeadException("Lead Type already present with this name");
            }
        } else {
            throw new LeadException("Lead Type Not Found");
        }
    }

    @Override
    public LeadType findById(Long id) {
        return leadTypeRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(LeadType leadType) {
        leadTypeRepository.delete(leadType);
    }

    @Override
    public long count() {
        return leadTypeRepository.count();
    }

    @Override
    public LeadType findByType(String type) {
        return leadTypeRepository.findByName(type).orElse(null);
    }

    @Override
    public LeadType deactivateLeadType(Long id) {
        LeadType leadType = findById(id);
        if (Objects.nonNull(leadType)) {
            leadType.setActive(false);
            leadType.setLastUpdateAt(LocalDateTime.now());
            leadType.setUpdatedBy(employeeService.findLoggedInEmployee());
            return leadTypeRepository.save(leadType);
        } else {
            throw new LeadException("Lead Type Not Found");
        }
    }

    @Override
    public String createLeadType(String leadType) {
        String replaceText = leadType.stripLeading().stripTrailing();
        replaceText = replaceText.replace(' ', '_');
        replaceText = replaceText.replace('-', '_');
        replaceText = replaceText.toUpperCase();
        return replaceText;
    }
}
