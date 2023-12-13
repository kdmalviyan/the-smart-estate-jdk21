package com.sfd.thesmartestate.notifications.services;

import com.sfd.thesmartestate.notifications.enums.NotificationType;
import com.sfd.thesmartestate.notifications.mobile.PushNotificationSender;
import com.sfd.thesmartestate.notifications.mobile.SMSNotificationSender;
import com.sfd.thesmartestate.notifications.web.BrowserNotificationSender;
import com.sfd.thesmartestate.notifications.web.EmailNotificationSender;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Map;

@AllArgsConstructor
@Component
@Slf4j
@SuppressFBWarnings("EI_EXPOSE_REP")

public class NotificationSenderFactory implements InitializingBean {
    private final PushNotificationSender pushNotificationSender;
    private final SMSNotificationSender smsNotificationSender;
    private final BrowserNotificationSender browserNotificationSender;
    private final EmailNotificationSender emailNotificationSender;
    private Map<NotificationType, NotificationSender> senderFactory;

    public NotificationSender get(NotificationType notificationType) {
        return senderFactory.get(notificationType);
    }

    public void add(NotificationType notificationType, NotificationSender notificationSender) {
        senderFactory.put(notificationType, notificationSender);
    }

    public void reset(Map<NotificationType, NotificationSender> senderFactory) {
        this.senderFactory = senderFactory;
    }


    @Override
    public void afterPropertiesSet() {
        senderFactory.put(NotificationType.BROWSER, browserNotificationSender);
        senderFactory.put(NotificationType.EMAIL, emailNotificationSender);
        senderFactory.put(NotificationType.PUSH, pushNotificationSender);
        senderFactory.put(NotificationType.SMS, smsNotificationSender);
    }
}
