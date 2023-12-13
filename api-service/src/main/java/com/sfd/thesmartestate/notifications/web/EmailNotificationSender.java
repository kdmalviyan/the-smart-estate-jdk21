package com.sfd.thesmartestate.notifications.web;

import com.sfd.thesmartestate.notifications.NotificationMessage;
import com.sfd.thesmartestate.notifications.exceptions.EmailSendException;
import com.sfd.thesmartestate.notifications.services.NotificationSender;
import com.sfd.thesmartestate.notifications.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service(EmailNotificationSender.BEAN_NAME)
@Slf4j
public class EmailNotificationSender extends NotificationSender {
    public static final String BEAN_NAME = "emailNotificationSender";
    private final JavaMailSender emailSender;
    @Value("notification.from")
    private String fromEmail;
    @Value("notification.recipients")
    private String recipients;

    public EmailNotificationSender(final NotificationService notificationService,
                                   final JavaMailSender emailSender) {
        super(notificationService);
        this.emailSender = emailSender;
    }

    @Override
    public void send(NotificationMessage notificationMessage, Set<String> recipients) {
        if (Objects.isNull(recipients) || recipients.isEmpty()) {
            throw new IllegalArgumentException("Empty Recipients");
        }
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(notificationMessage.getSubject());
        email.setText(notificationMessage.getMessage());
        email.setTo(recipients.toArray(new String[]{}));
        email.setFrom(fromEmail);
        try {
            emailSender.send(email);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new EmailSendException("Unable to send Email ,please try after some time");
        }
        log.info(notificationMessage.toString());
    }
}
