package com.binarybrothers.gymflexapi.services.notification;

import com.binarybrothers.gymflexapi.entities.Notification;
import java.util.List;


public interface NotificationService {

    List<Notification> findALlNotification();

    void save(Notification notification);
    void delete(Long idNotif);

    void markAsRead();

    Long numNotifNotRead();
}
