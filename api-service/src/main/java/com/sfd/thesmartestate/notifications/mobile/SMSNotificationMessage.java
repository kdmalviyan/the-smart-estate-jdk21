package com.sfd.thesmartestate.notifications.mobile;

import com.sfd.thesmartestate.notifications.NotificationMessage;

public class SMSNotificationMessage extends NotificationMessage {
    public SMSNotificationMessage(String message, String subject, boolean isTransactional) {
        super(message, subject, isTransactional);
    }
}
