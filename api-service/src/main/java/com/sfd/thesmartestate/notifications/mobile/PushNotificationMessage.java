package com.sfd.thesmartestate.notifications.mobile;

import com.sfd.thesmartestate.notifications.NotificationMessage;

public class PushNotificationMessage extends NotificationMessage {
    public PushNotificationMessage(String message, String subject, boolean isTransactional) {
        super(message, subject, isTransactional);
    }
}
