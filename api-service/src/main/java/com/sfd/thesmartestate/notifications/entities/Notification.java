package com.sfd.thesmartestate.notifications.entities;

import com.sfd.thesmartestate.notifications.enums.NotificationType;
import com.sfd.thesmartestate.users.entities.Employee;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "tb_notifications")
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class Notification implements Serializable, Comparable<Notification> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "notification_type")
    private NotificationType notificationType;

    @Column(name = "read_reciept")
    private boolean read;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private Employee createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private Employee updatedBy;


    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdateAt;

    @Override
    public int compareTo(Notification notification) {
        return this.id.compareTo(notification.id);
    }
}
