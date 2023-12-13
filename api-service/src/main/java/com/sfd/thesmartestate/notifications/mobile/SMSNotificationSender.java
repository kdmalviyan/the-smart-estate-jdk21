package com.sfd.thesmartestate.notifications.mobile;

import com.sfd.thesmartestate.notifications.NotificationMessage;
import com.sfd.thesmartestate.notifications.services.NotificationSender;
import com.sfd.thesmartestate.notifications.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service(SMSNotificationSender.BEAN_NAME)
@Slf4j
public class SMSNotificationSender extends NotificationSender {
    public static final String BEAN_NAME = "smsNotificationSender";

    public SMSNotificationSender(NotificationService notificationService) {
        super(notificationService);
    }

    @Override
    public void send(NotificationMessage notificationMessage, Set<String> recipients) {
        if (Objects.isNull(recipients) || recipients.isEmpty()) {
            throw new RuntimeException("Empty Recipients");
        }
        log.info(notificationMessage.toString());
    }
}
