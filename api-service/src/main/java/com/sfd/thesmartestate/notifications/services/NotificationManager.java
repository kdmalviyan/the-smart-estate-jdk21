package com.sfd.thesmartestate.notifications.services;

import com.sfd.thesmartestate.notifications.NotificationMessage;
import com.sfd.thesmartestate.notifications.enums.NotificationType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component(NotificationManager.BEAN_NAME)
@Slf4j
@AllArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class NotificationManager {
    public static final String BEAN_NAME = "notificationManager";
    private final NotificationSenderFactory notificationSenderFactory;

    public void send(NotificationMessage notificationMessage, NotificationType notificationType, Set<String> recipients) {
        NotificationSender notificationSender = notificationSenderFactory.get(notificationType);
        notificationSender.send(notificationMessage, recipients);
    }
}
