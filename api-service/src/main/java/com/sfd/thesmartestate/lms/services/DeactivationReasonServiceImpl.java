package com.sfd.thesmartestate.lms.services;

import com.sfd.thesmartestate.lms.entities.DeactivationReason;
import com.sfd.thesmartestate.lms.exceptions.LeadException;
import com.sfd.thesmartestate.lms.repositories.DeactivationReasonRepository;
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

public class DeactivationReasonServiceImpl implements DeactivationReasonService {

    private final DeactivationReasonRepository deactivationReasonRepository;
    private final EmployeeService employeeService;

    @Override
    public DeactivationReason create(DeactivationReason deactivationReason) {
        DeactivationReason existingDeactivationReason = this.findByName(deactivationReason.getName());
        if (Objects.isNull(existingDeactivationReason)) {
            deactivationReason.setName(createDeactivationReasonName(deactivationReason.getName()));
            deactivationReason.setCreatedAt(LocalDateTime.now());
            deactivationReason.setLastUpdateAt(LocalDateTime.now());
            deactivationReason.setCreatedBy(employeeService.findLoggedInEmployee());
            deactivationReason.setUpdatedBy(employeeService.findLoggedInEmployee());
            deactivationReason.setActive(true);
            return deactivationReasonRepository.save(deactivationReason);
        } else {
            throw new LeadException("Deactivation Reason already Exists with this Name");
        }
    }

    @Override
    public DeactivationReason update(DeactivationReason deactivationReason) {
        DeactivationReason existingDeactivationReason = findById(deactivationReason.getId());
        if (Objects.nonNull(existingDeactivationReason)) {
            String name = this.createDeactivationReasonName(deactivationReason.getName());
            if (Objects.isNull(this.findByName(deactivationReason.getName())) || existingDeactivationReason.getName().equalsIgnoreCase(name)) {
                existingDeactivationReason.setName(name);
                existingDeactivationReason.setDescription(deactivationReason.getDescription());
                existingDeactivationReason.setLastUpdateAt(LocalDateTime.now());
                existingDeactivationReason.setUpdatedBy(employeeService.findLoggedInEmployee());
                return deactivationReasonRepository.save(existingDeactivationReason);
            } else {
                throw new LeadException("Deactivation Reason already exists with this name");
            }
        } else {
            throw new LeadException("No Deactivation Reason Found");
        }
    }

    @Override
    public List<DeactivationReason> findAll() {
        return deactivationReasonRepository.findAll().stream().filter(DeactivationReason::getActive).sorted(Comparator.comparing(DeactivationReason::getLastUpdateAt).reversed()).collect(Collectors.toList());
    }

    @Override
    public DeactivationReason findByName(String name) {
        return deactivationReasonRepository.findByName(name);
    }

    @Override
    public DeactivationReason findById(Long id) {
        return deactivationReasonRepository.findById(id).orElse(null);
    }

    @Override
    public DeactivationReason delete(Long id) {
        DeactivationReason deactivationReason = findById(id);
        if (Objects.nonNull(deactivationReason)) {
            deactivationReason.setActive(false);
            deactivationReason.setUpdatedBy(employeeService.findLoggedInEmployee());
            deactivationReason.setLastUpdateAt(LocalDateTime.now());
            return deactivationReasonRepository.save(deactivationReason);
        } else {
            throw new LeadException("No Deactivation Reason Found");
        }
    }

    @Override
    public String createDeactivationReasonName(String leadDescription) {
        String replaceText = leadDescription.stripLeading().stripTrailing();
        replaceText = replaceText.replace(' ', '_');
        replaceText = replaceText.replace('-', '_');
        return replaceText;
    }
}
