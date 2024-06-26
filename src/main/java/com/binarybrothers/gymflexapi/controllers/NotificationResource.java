package com.binarybrothers.gymflexapi.controllers;

import com.binarybrothers.gymflexapi.entities.Notification;
import com.binarybrothers.gymflexapi.services.notification.NotificationService;
import com.binarybrothers.gymflexapi.utils.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class NotificationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);
    private static final String ENTITY_NAME = "notification";

    @Value("${APPLICATION_NAME}")
    private String APPLICATION_NAME;

    private final NotificationService notificationService;

    /**
     * {@code GET  /notifications} : get all notifications.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notifications in body.
     */
    @GetMapping("/notifications")
    public List<Notification> getAllNotifications() {
        log.debug("REST request to get all notifications");
        return notificationService.findALlNotification();
    }

    /**
     * {@code DELETE  /notifications/:id} : delete the "id" notification.
     *
     * @param id the id of the notification to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notifications/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        log.debug("REST request to delete Notification : {}", id);
        notificationService.delete(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(APPLICATION_NAME, true, ENTITY_NAME, id.toString()))
                .build();
    }

    /**
     * {@code PUT  /notifications/read} : mark all notifications as read.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}.
     */
    @PutMapping("/notifications/read")
    public ResponseEntity<Void> markNotificationsAsRead() {
        log.debug("REST request to mark all notifications as read");
        notificationService.markAsRead();
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, "all"))
                .build();
    }
}
