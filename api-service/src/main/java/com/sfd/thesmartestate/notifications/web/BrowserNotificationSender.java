package com.sfd.thesmartestate.notifications.web;

import com.sfd.thesmartestate.notifications.NotificationMessage;
import com.sfd.thesmartestate.notifications.services.NotificationSender;
import com.sfd.thesmartestate.notifications.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service(BrowserNotificationSender.BEAN_NAME)
@Slf4j
public class BrowserNotificationSender extends NotificationSender {
    public static final String BEAN_NAME = "browserNotificationSender";

    public BrowserNotificationSender(final NotificationService notificationService) {
        super(notificationService);
    }

    @Override
    public void send(NotificationMessage notificationMessage, Set<String> recipients) {
        if (Objects.isNull(recipients) || recipients.isEmpty()) {
            throw new IllegalArgumentException("Empty Recipients");
        }
        log.info(notificationMessage.toString());
    }
}
