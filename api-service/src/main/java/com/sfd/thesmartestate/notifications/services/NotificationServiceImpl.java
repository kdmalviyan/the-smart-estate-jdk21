package com.sfd.thesmartestate.notifications.services;

import com.sfd.thesmartestate.notifications.entities.Notification;
import com.sfd.thesmartestate.notifications.repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(NotificationServiceImpl.BEAN_NAME)
@AllArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    public static final String BEAN_NAME = "notificationService";
    private final NotificationRepository notificationRepository;

    @Override
    public Notification create(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public Notification update(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification findById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }
}
