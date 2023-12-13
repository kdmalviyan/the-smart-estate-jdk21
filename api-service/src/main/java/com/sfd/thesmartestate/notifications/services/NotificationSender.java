package com.sfd.thesmartestate.notifications.services;

import com.sfd.thesmartestate.notifications.NotificationMessage;
import lombok.Data;

import java.util.Set;

@Data
public abstract class NotificationSender {
    protected final NotificationService notificationService;

    public NotificationSender(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public abstract void send(NotificationMessage notificationMessage, Set<String> recipients);
}
