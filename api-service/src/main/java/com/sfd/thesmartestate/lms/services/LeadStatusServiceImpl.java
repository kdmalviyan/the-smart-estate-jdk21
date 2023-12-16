package com.sfd.thesmartestate.lms.services;

import com.sfd.thesmartestate.lms.entities.LeadStatus;
import com.sfd.thesmartestate.lms.exceptions.LeadException;
import com.sfd.thesmartestate.lms.repositories.LeadStatusRepository;
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

public class LeadStatusServiceImpl implements LeadStatusService {

    private final LeadStatusRepository leadStatusRepository;
    private final EmployeeService employeeService;

    @Override
    public List<LeadStatus> findAll() {
        return leadStatusRepository.findAll().stream().filter(LeadStatus::getActive).sorted(Comparator.comparing(LeadStatus::getLastUpdateAt).reversed()).collect(Collectors.toList());
    }

    @Override
    public LeadStatus create(LeadStatus leadStatus) {
        LeadStatus existingLeadStatus = findByName(createLeadStatusName(leadStatus.getName()));
        if (Objects.isNull(existingLeadStatus)) {
            leadStatus.setName(createLeadStatusName(leadStatus.getName()));
            leadStatus.setCreatedAt(LocalDateTime.now());
            leadStatus.setCreatedBy(employeeService.findLoggedInEmployee());
            leadStatus.setUpdatedBy(employeeService.findLoggedInEmployee());
            leadStatus.setLastUpdateAt(LocalDateTime.now());
            leadStatus.setActive(true);
            return leadStatusRepository.save(leadStatus);
        } else {
            throw new LeadException("Lead Status already Exists.");
        }
    }

    @Override
    public LeadStatus update(LeadStatus leadStatus) {
        LeadStatus exLeadStatus = findById(leadStatus.getId());
        if (Objects.nonNull(exLeadStatus)) {
            if (Objects.isNull(findByName(createLeadStatusName(leadStatus.getName())))) {
                exLeadStatus.setDescription(leadStatus.getDescription());
                exLeadStatus.setName(createLeadStatusName(leadStatus.getName()));
                exLeadStatus.setLastUpdateAt(LocalDateTime.now());
                exLeadStatus.setUpdatedBy(employeeService.findLoggedInEmployee());
                return leadStatusRepository.save(exLeadStatus);
            } else {
                throw new LeadException("Lead Status already present with this name.");
            }
        } else {
            throw new LeadException("Lead Status Not found");
        }
    }

    @Override
    public LeadStatus findById(Long id) {
        return leadStatusRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(LeadStatus leadStatus) {
        leadStatusRepository.delete(leadStatus);
    }

    @Override
    public long count() {
        return leadStatusRepository.count();
    }

    @Override
    public LeadStatus findByName(String leadStatusName) {
        return leadStatusRepository.findByName(leadStatusName).orElse(null);
    }

    @Override
    public LeadStatus deactivateLeadStatus(Long id) {
        LeadStatus leadStatus = findById(id);
        if (Objects.nonNull(leadStatus)) {
            leadStatus.setActive(false);
            leadStatus.setLastUpdateAt(LocalDateTime.now());
            leadStatus.setUpdatedBy(employeeService.findLoggedInEmployee());
            return leadStatusRepository.save(leadStatus);
        } else {
            throw new LeadException("Lead Status Not Found");
        }
    }

    @Override
    public String createLeadStatusName(String statusDescription) {
        if(Objects.nonNull(statusDescription)) {
            String replaceText = statusDescription.stripLeading().stripTrailing();
            replaceText = replaceText.replace(' ', '-');
            replaceText = replaceText.toUpperCase();
            return replaceText;
        }
        return "";
    }
}
