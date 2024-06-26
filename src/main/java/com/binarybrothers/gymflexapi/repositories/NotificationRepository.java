package com.binarybrothers.gymflexapi.repositories;

import com.binarybrothers.gymflexapi.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Modifying
    @Query("update Notification e set e.readed = true ")
    void markASRead();

    @Query("select count(e.id) from Notification e where e.readed = false ")
    Long numNotifNotRead();

}