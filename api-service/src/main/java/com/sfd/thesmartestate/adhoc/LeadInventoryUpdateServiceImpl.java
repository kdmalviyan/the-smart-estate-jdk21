package com.sfd.thesmartestate.adhoc;

import com.sfd.thesmartestate.adhoc.dto.DuplicateResponse;
import com.sfd.thesmartestate.lms.entities.Comment;
import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.lms.entities.LeadInventorySize;
import com.sfd.thesmartestate.lms.followups.Followup;
import com.sfd.thesmartestate.lms.followups.FollowupRepository;
import com.sfd.thesmartestate.lms.repositories.LeadInventorySizeRepository;
import com.sfd.thesmartestate.lms.repositories.LeadRepository;
import com.sfd.thesmartestate.employee.entities.Employee;
import com.sfd.thesmartestate.employee.services.EmployeeService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@SuppressFBWarnings("EI_EXPOSE_REP")
@RequiredArgsConstructor
public class LeadInventoryUpdateServiceImpl implements LeadInventoryUpdateService {
    private final LeadRepository leadRepository;
    private final FollowupRepository followupRepository;
    private final LeadInventorySizeRepository leadInventorySizeRepository;

    private final EmployeeService employeeService;

    @Override
    public void updateLeadInventorySize(UpdateInventorySizeDTO updateInventorySizeDTO, String referenceId) {
        long start = System.currentTimeMillis();
        long counter = 0L;
        List<Lead> leads = leadRepository.findByLeadWithNullInventorySize();
        Employee employee = employeeService.findLoggedInEmployee();
        for (Lead lead : leads) {
            if (updateInventorySizeDTO.getExcludeProject().contains(lead.getProject().getName())) {
                log.info("Request Id " + referenceId + ": Skipping lead id " + lead.getId() + " for project " + lead.getProject().getName());
                continue;
            }
            log.info("Request Id " + referenceId + ": updating " + lead.getId());
            Double budgetUpperLimit = lead.getBudget().getAbsoluteEndAmount();
            LeadInventorySize leadInventorySize;
            if (budgetUpperLimit <= 8500000) {
                leadInventorySize = leadInventorySizeRepository.findBySize("2_bhk");
            } else if (budgetUpperLimit <= 10000000) {
                leadInventorySize = leadInventorySizeRepository.findBySize("3_bhk");
            } else if (budgetUpperLimit <= 15000000) {
                leadInventorySize = leadInventorySizeRepository.findBySize("3_bhk_+_servant_room");
            } else if (budgetUpperLimit <= 30000000) {
                leadInventorySize = leadInventorySizeRepository.findBySize("4_bhk");
            } else if (budgetUpperLimit <= 45000000) {
                leadInventorySize = leadInventorySizeRepository.findBySize("4_bhk_+_servant_room");
            } else if (budgetUpperLimit <= 65000000) {
                leadInventorySize = leadInventorySizeRepository.findBySize("5_bhk");
            } else {
                leadInventorySize = leadInventorySizeRepository.findBySize("Penthouse");
            }
            lead.setLeadInventorySize(leadInventorySize);
            lead.setLastUpdateAt(LocalDateTime.now());
            lead.setUpdatedBy(employee);
            leadRepository.saveAndFlush(lead);
            counter++;
        }
        long end = System.currentTimeMillis();
        log.info("Update Null repository size in lead for requestId " + referenceId + " is completed. Total " + counter + " leads updated." +
                " It took total " + ((end - start) / 1000) + " seconds.");
    }

    @Override
    @Transactional
    public void updateDuplicateLead(String referenceId) {
        List<DuplicateResponse> duplicateLeads = leadRepository.findCustomerAndPhoneDuplicateLeads();

        for (DuplicateResponse duplicateResponse : duplicateLeads) {
            List<Lead> leads = leadRepository.findByCustomerIdAndProjectId(duplicateResponse.getCustomerId(), duplicateResponse.getProjectId());
            leads.sort(Comparator.comparing(Lead::getId));
            Lead firstLead = leads.get(0);
            Lead duplicateLead = leads.get(1);

            System.out.println(leads.get(0).getId() + "-" + leads.get(1).getId());

            List<Comment> comments = duplicateLead.getComments().stream().map(LeadInventoryUpdateServiceImpl::addAndUpdateComment).collect(Collectors.toList());
            firstLead.getComments().addAll(comments);

            Set<Followup> followups = followupRepository.findByLead(duplicateLead);
            followups.forEach(followupRepository::delete);
            leadRepository.save(firstLead);
            leadRepository.delete(duplicateLead);
        }
    }

    private static Comment addAndUpdateComment(Comment comment) {
        Comment c = new Comment();
        c.setCommentType(comment.getCommentType());
        c.setMessage(comment.getMessage().concat(" - COPIED from duplicated Lead"));
        c.setCreatedAt(comment.getCreatedAt());
        c.setCreatedBy(comment.getCreatedBy());
        return c;
    }
}
