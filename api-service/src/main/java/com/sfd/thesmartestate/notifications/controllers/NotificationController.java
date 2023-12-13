package com.sfd.thesmartestate.notifications.controllers;

import com.sfd.thesmartestate.notifications.entities.Notification;
import com.sfd.thesmartestate.notifications.enums.NotificationType;
import com.sfd.thesmartestate.notifications.mobile.PushNotificationMessage;
import com.sfd.thesmartestate.notifications.mobile.SMSNotificationMessage;
import com.sfd.thesmartestate.notifications.services.NotificationManager;
import com.sfd.thesmartestate.notifications.services.NotificationService;
import com.sfd.thesmartestate.notifications.web.BrowserNotificationMessage;
import com.sfd.thesmartestate.notifications.web.EmailNotificationMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "notification")
@Slf4j
@AllArgsConstructor
@CrossOrigin("*")
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationManager notificationManager;

    @GetMapping
    public ResponseEntity<List<Notification>> findAll() {
        notificationManager.send(new EmailNotificationMessage(
                "Test Email Message", "Test Email Subject", false
        ), NotificationType.EMAIL, Set.of("kdmalviyan@gmail.com"));

        notificationManager.send(new PushNotificationMessage(
                "Test Push Message", "Test Push Subject", false
        ), NotificationType.PUSH, Set.of("kdmalviyan@gmail.com"));

        notificationManager.send(new SMSNotificationMessage(
                "Test SMS Message", "Test SMS Subject", false
        ), NotificationType.SMS, Set.of("kdmalviyan@gmail.com"));

        notificationManager.send(new BrowserNotificationMessage(
                "Test BrowserNotificationMessage Message", "Test BrowserNotificationMessage Subject", false
        ), NotificationType.BROWSER, Set.of("kdmalviyan@gmail.com"));
        return ResponseEntity.ok(notificationService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Notification> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(notificationService.findById(id));
    }

}
