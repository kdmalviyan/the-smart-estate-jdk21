package com.sfd.thesmartestate.notifications.repositories;

import com.sfd.thesmartestate.notifications.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
