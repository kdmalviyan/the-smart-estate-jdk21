package com.sfd.thesmartestate.notifications.services;

import com.sfd.thesmartestate.notifications.entities.Notification;

import java.util.List;

public interface NotificationService {

    Notification create(Notification notification);

    Notification update(Notification notification);

    List<Notification> findAll();

    Notification findById(Long id);
}
