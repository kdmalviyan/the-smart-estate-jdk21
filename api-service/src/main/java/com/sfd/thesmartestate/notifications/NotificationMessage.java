package com.sfd.thesmartestate.notifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public abstract class NotificationMessage {
    public String message;
    public String subject;
    public boolean isTransactional;
}
