package com.example.GymInTheBack.services.notification;

import com.example.GymInTheBack.entities.Notification;
import java.util.List;


public interface NotificationService {

    List<Notification> findALlNotification();

    void save(Notification notification);
    void delete(Long idNotif);

    void markAsRead();

    Long numNotifNotRead();
}
