package com.sfd.thesmartestate.lms.services;

import com.sfd.thesmartestate.common.Constants;
import com.sfd.thesmartestate.common.LeadEvents;
import com.sfd.thesmartestate.common.services.LeadEventService;
import com.sfd.thesmartestate.lms.calls.Call;
import com.sfd.thesmartestate.lms.entities.Comment;
import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.lms.exceptions.LeadException;
import com.sfd.thesmartestate.lms.followups.Followup;
import com.sfd.thesmartestate.lms.repositories.LeadRepository;
import com.sfd.thesmartestate.lms.targets.TargetService;
import com.sfd.thesmartestate.projects.entities.Project;
import com.sfd.thesmartestate.projects.services.ProjectService;
import com.sfd.thesmartestate.employee.entities.Employee;
import com.sfd.thesmartestate.employee.services.EmployeeService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressFBWarnings("EI_EXPOSE_REP")

public class LeadUpdateService {
    private final EmployeeService employeeService;
    private final LeadStatusService leadStatusService;
    private final LeadTypeService leadTypeService;
    private final LeadSourceService leadSourceService;
    private final LeadInventorySizeService leadInventorySizeService;
    private final LeadRepository repository;
    private final LeadEventService leadEventService;
    private final TargetService targetService;
    private final ProjectService projectService;
    private final CommentService commentService;

    public void validateDuplicateLeads(Lead lead) {
        if (!repository.findByCustomerPhoneAndProjectId(lead.getCustomer().getPhone(), lead.getProject().getId()).isEmpty()) {
            throw new LeadException("Lead is already exists for this customer phone number and project, please try with different phone of project");
        }
    }

    public void updateLeadComment(Long leadId, Comment comment) {
        Optional<Lead> optional = repository.findById(leadId);
        if (optional.isPresent()) {
            Lead lead = optional.get();
            lead.getComments().add(comment);
            lead.setUpdatedBy(employeeService.findLoggedInEmployee());
            lead.setLastUpdateAt(LocalDateTime.now());
            repository.save(lead);
        }
    }

    public void updateLeadCallDetails(Call call, Long leadId) {
        Optional<Lead> optional = repository.findById(leadId);
        if (optional.isPresent()) {
            Lead lead = optional.get();
            lead.getCalls().add(call);
            lead.setStatus(leadStatusService.findByName(Constants.FOLLOW_UP));
            lead.setUpdatedBy(employeeService.findLoggedInEmployee());
            lead.setLastUpdateAt(LocalDateTime.now());

            //add remark of call
            Comment comment = new Comment();
            comment.setMessage(call.getComment());
            comment.setCreatedBy(employeeService.findLoggedInEmployee());
            comment.setCreatedAt(LocalDateTime.now());
            comment.setCommentType(LeadEvents.MOBILE_REMARK.name());
            lead.getComments().add(comment);
            update(lead, LeadEvents.MOBILE_REMARK);
        }
    }

    @Transactional
    public Lead update(Lead lead, LeadEvents leadEvent) {
        Optional<Lead> optional = repository.findById(lead.getId());
        if (optional.isPresent()) {
            Lead leadInDb = optional.get();
            Employee loggedInEmployee = employeeService.findLoggedInEmployee();
            // update lead value on base of status
            setLeadStatusOnEvent(lead, leadEvent, leadInDb, loggedInEmployee);

            if (!commentRequired(leadEvent)) {
                //set comment for lead events
                leadEventService.setLeadEventComment(leadInDb, leadEvent, null);
            }

            return repository.saveAndFlush(leadInDb);
        }
        return null;
    }

    private void setLeadStatusOnEvent(Lead lead, LeadEvents leadEvent, Lead leadInDb, Employee loggedInEmployee) {
        switch (leadEvent) {
            case STATUS_CHANGED:
                leadInDb.setStatus(leadStatusService.findById(lead.getStatus().getId()));
                if (Constants.BOOKED.equals(lead.getStatus().getName())) {
                    targetService.findAndUpdateUserTarget(loggedInEmployee, leadEvent);
                }
                break;
            case USER_ASSIGNED:
                leadInDb.setAssignedTo(employeeService.findById(lead.getAssignedTo().getId()));
                break;
            case LEAD_TYPE_CHANGED:
                leadInDb.setType(leadTypeService.findById(lead.getType().getId()));
                break;
            case SOURCE_CHANGED:
                leadInDb.setSource(leadSourceService.findById(lead.getSource().getId()));
                break;
            case INVENTORY_CHANGED:
                leadInDb.setLeadInventorySize(leadInventorySizeService.findById(lead.getLeadInventorySize().getId()));
                break;
            case PROJECT_CHANGED:
                leadInDb.setProject(projectService.findById(lead.getProject().getId()));
                break;
            case SITE_VISIT:
                leadInDb.setSiteVisit(lead.isSiteVisit());
                if (lead.isSiteVisit()) {
                    targetService.findAndUpdateUserTarget(loggedInEmployee, leadEvent);
                }
                break;
            default:
                break;
        }
        leadInDb.setLastUpdateAt(LocalDateTime.now());
        leadInDb.setUpdatedBy(loggedInEmployee);
    }

    private boolean commentRequired(LeadEvents leadEvent) {
        return leadEvent.equals(LeadEvents.DEACTIVE_LEAD) || leadEvent.equals(LeadEvents.FOLLOWUP_STATUS_CHECK) ||
                leadEvent.equals(LeadEvents.MOBILE_REMARK);
    }

    public Lead updateProject(long leadId, Long projectId, Long userId) {
        Lead persistentLead = repository.findById(leadId).orElseThrow(() -> new LeadException("Unable to find lead by id " + leadId));
        Project project = projectService.findById(projectId);
        repository.findByCustomerPhoneAndProjectName(persistentLead.getCustomer().getPhone(), project.getName())
                .orElseThrow(() -> new LeadException("Already in system with Customer Phone: " + persistentLead.getCustomer().getPhone() + "  and Project: " + project.getName()));
        persistentLead.setProject(project);
        persistentLead.setAssignedTo(employeeService.findById(userId));
        return update(persistentLead, LeadEvents.PROJECT_CHANGED);
    }

    public Lead updateLeadFollowup(Lead lead, Followup followup) {
        // Add comment
        Comment comment = new Comment();
        comment.setCommentType("Followup");
        comment.setMessage(followup.getFollowupMessage());
        Comment commentAdded = commentService.create(comment);
        lead.getComments().add(commentAdded);
        repository.saveAndFlush(lead);
        return lead;
    }
}
