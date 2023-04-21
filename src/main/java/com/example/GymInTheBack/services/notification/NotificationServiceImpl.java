package com.example.GymInTheBack.services.notification;

import com.example.GymInTheBack.entities.Notification;
import com.example.GymInTheBack.repositories.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements  NotificationService{
    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }


    @Override
    public List<Notification> findALlNotification() {
        return notificationRepository.findAll();
    }

    @Override
    public void save(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public void delete(Long idNotif) {
        notificationRepository.deleteById(idNotif);
    }

    @Override
    public void markAsRead() {
        notificationRepository.markASRead();
    }

    @Override
    public Long numNotifNotRead() {
        return notificationRepository.numNotifNotRead();
    }


}
