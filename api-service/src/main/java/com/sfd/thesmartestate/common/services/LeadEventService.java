package com.sfd.thesmartestate.common.services;

import com.sfd.thesmartestate.common.LeadEvents;
import com.sfd.thesmartestate.lms.entities.Comment;
import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.lms.entities.LeadAssignHistory;
import com.sfd.thesmartestate.lms.followups.Followup;
import com.sfd.thesmartestate.lms.repositories.LeadRepository;
import com.sfd.thesmartestate.lms.services.CommentService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class LeadEventService {

    private final CommentService commentService;
    private final LeadRepository leadRepository;

    public void setLeadEventComment(Lead lead, LeadEvents leadEvents, Followup followup) {
        Lead existingLead = leadRepository.findById(lead.getId()).orElse(null);

        String message = "";
        String leadEventsType = "";
        switch (leadEvents) {
            case SITE_VISIT:
                message = "Site visited on  " + LocalDateTime.now().toLocalDate();
                leadEventsType = "Visit";
                break;
            case USER_ASSIGNED:
                message = "Lead assign to " + lead.getAssignedTo().getName() + " by " + lead.getUpdatedBy().getName();
                LeadAssignHistory leadAssignHistory = new LeadAssignHistory();
                leadAssignHistory.setAssignedTo(lead.getAssignedTo());
                assert existingLead != null;
                leadAssignHistory.setAssignedFrom(existingLead.getAssignedTo());
                leadAssignHistory.setAssignedBy(lead.getUpdatedBy());
                leadAssignHistory.setAssignmentTime(LocalDateTime.now());
                lead.getLeadAssignHistory().add(leadAssignHistory);

                leadEventsType = "Assignment";
                break;
            case STATUS_CHANGED:
                message = "Lead status changed to " + lead.getStatus().getDescription() + " by " + lead.getUpdatedBy().getName();
                leadEventsType = "Lead Progress";
                break;
            case SOURCE_CHANGED:
                message = "Lead source changed to " + lead.getSource().getDescription() + " by " + lead.getUpdatedBy().getName();
                leadEventsType = "Source";
                break;
            case INVENTORY_CHANGED:
                message = "Lead Inventory changed to " + lead.getLeadInventorySize().getDescription() + " by " + lead.getUpdatedBy().getName();
                leadEventsType = "Update";
                break;
            case FOLLOWUP_STATUS_UPDATED:
                if(Objects.nonNull(followup)) {
                    message = followup.getFollowupMessage();
                    leadEventsType = "Followup";
                }
                break;
            case TRANSFER_LEAD:
                message = "Lead is transfer to " + lead.getAssignedTo().getName();
                leadEventsType = "Transfer";
                break;
            case PROJECT_CHANGED:
                message = "Project change to " + lead.getProject().getName() + " Assigned to " + lead.getAssignedTo().getName();
                leadEventsType = "Project change";
                break;
            case LEAD_TYPE_CHANGED:
                message = "Lead type change to " + lead.getType().getDescription();
                leadEventsType = "Type change";
                break;
            default:
                break;
        }
        // Add comment
        Comment comment = new Comment();
        comment.setCommentType(leadEventsType);
        comment.setMessage(message);
        Comment commentAdded = commentService.create(comment);
        lead.getComments().add(commentAdded);
    }
}
