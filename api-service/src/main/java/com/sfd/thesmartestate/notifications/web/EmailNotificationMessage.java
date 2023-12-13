package com.sfd.thesmartestate.notifications.web;

import com.sfd.thesmartestate.notifications.NotificationMessage;

public class EmailNotificationMessage extends NotificationMessage {
    public EmailNotificationMessage(String message, String subject, boolean isTransactional) {
        super(message, subject, isTransactional);
    }
}
