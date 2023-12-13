package com.sfd.thesmartestate.notifications.web;

import com.sfd.thesmartestate.notifications.NotificationMessage;


public class BrowserNotificationMessage extends NotificationMessage {
    public BrowserNotificationMessage(String message, String subject, boolean isTransactional) {
        super(message, subject, isTransactional);
    }
}
