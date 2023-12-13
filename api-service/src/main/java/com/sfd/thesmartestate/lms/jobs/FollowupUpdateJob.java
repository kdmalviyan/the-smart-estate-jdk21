package com.sfd.thesmartestate.lms.jobs;

import com.sfd.thesmartestate.common.LeadEvents;
import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.lms.entities.LeadStatus;
import com.sfd.thesmartestate.lms.followups.Followup;
import com.sfd.thesmartestate.lms.followups.FollowupService;
import com.sfd.thesmartestate.lms.repositories.LeadRepository;
import com.sfd.thesmartestate.lms.services.LeadStatusService;
import com.sfd.thesmartestate.lms.services.LeadUpdateService;
import com.sfd.thesmartestate.notifications.NotificationMessage;
import com.sfd.thesmartestate.notifications.services.NotificationSender;
import com.sfd.thesmartestate.notifications.web.EmailNotificationMessage;
import com.sfd.thesmartestate.notifications.web.EmailNotificationSender;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Component
@Slf4j
@SuppressFBWarnings("EI_EXPOSE_REP")

public class FollowupUpdateJob {

    private final LeadStatusService leadStatusService;
    private final LeadRepository leadRepository;
    private final LeadUpdateService leadUpdateService;
    private final NotificationSender notificationSender;
    private final FollowupService followupService;

    @Value("#{'${notification.from}'.split(',')}")
    private Set<String> recipients;

    public FollowupUpdateJob(final LeadStatusService leadStatusService,
                             final LeadRepository leadRepository,
                             final LeadUpdateService leadUpdateService,
                             final FollowupService followupService,
                             @Qualifier(EmailNotificationSender.BEAN_NAME) final NotificationSender notificationSender) {
        this.leadStatusService = leadStatusService;
        this.leadRepository = leadRepository;
        this.leadUpdateService = leadUpdateService;
        this.notificationSender = notificationSender;
        this.followupService = followupService;
    }

    @Scheduled(cron = "0 0 20 * * *") // 8PM every day
    // @Scheduled(cron = "0 0/2 * * * *") // every min
    public void leadStatusChange() {
        log.info("Followup Update Job is started");
        // Using leadRepository because we do not want to apply any filters of role during the job run
        leadRepository.findAll().parallelStream().filter(lead -> Objects.nonNull(getFollowup(lead))).forEach(lead -> {
            log.info("Updating followup for lead " + lead.getId());
            if (isExpired(getFollowup(lead)) && !Objects.equals(lead.getStatus().getName(), "FOLLOW-UP")) {
                findAndSetStatus(lead, "FOLLOW-UP-EXPIRE");
                //TDO:
                //sendExpireNotification(lead);
            } else {
                findAndSetStatus(lead, "FOLLOW-UP");
            }
        });
        log.info("Followup Update Job is Completed");
    }

    private void sendExpireNotification(Lead lead) {
        String message = lead.getAssignedTo().getName() +
                " " +
                "has missed a followup call with" +
                " " +
                lead.getCustomer().getName() +
                " " +
                "for" +
                " " +
                lead.getProject().getName() +
                "\n" +
                "Either user has not called the customer of forgot to close the followup in system";
        String subject = "Followup Expire: Project "
                + lead.getProject().getName()
                + " User " + lead.getAssignedTo().getName()
                + " Lead Id" + lead.getId();
        boolean isTransactional = false;
        NotificationMessage emailNotificationMessage = new EmailNotificationMessage(message, subject, isTransactional);
        notificationSender.send(emailNotificationMessage, recipients);
    }

    private Followup getFollowup(Lead lead) {
        return followupService.findOpenFollowupByLead(lead);
    }

    @Transactional
    private void findAndSetStatus(Lead lead, String statusName) {
        LeadStatus status = leadStatusService.findByName(statusName);
        lead.setStatus(status);
        leadUpdateService.update(lead, LeadEvents.FOLLOWUP_STATUS_CHECK);
    }

    private boolean isExpired(Followup followup) {
        return Objects.nonNull(followup.getFollowupTime())
                && followup.getFollowupTime().isBefore(LocalDateTime.now());
    }
}
