package com.example.flexiMed.service;

import com.example.flexiMed.controller.NotificationController;
import com.example.flexiMed.exceptions.ErrorResponse.NotificationFailedException;
import com.example.flexiMed.model.AmbulanceEntity;
import com.example.flexiMed.model.UserEntity;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for sending notifications related to ambulance dispatch.
 * This service interacts with the {@link NotificationController} to send notifications
 * to users when an ambulance is dispatched.
 */
@Service
public class NotificationService {

    private final NotificationController notificationController;

    /**
     * Constructs a {@code NotificationService} with the specified {@link NotificationController}.
     *
     * @param notificationController the controller responsible for handling notifications.
     */
    public NotificationService(NotificationController notificationController) {
        this.notificationController = notificationController;
    }

    /**
     * Sends a notification to a user informing them that an ambulance has been dispatched.
     *
     * @param user        the {@link UserEntity} representing the user receiving the notification.
     * @param ambulance   the {@link AmbulanceEntity} representing the dispatched ambulance.
     * @param etaInMinutes the estimated time of arrival (ETA) of the ambulance in minutes.
     * @throws NotificationFailedException if sending the notification fails.
     */
    public void sendUserNotifications(String message, UserEntity user, AmbulanceEntity ambulance, String etaInMinutes) {
        NotificationController.AmbulanceNotificationDTO notificationDTO = new NotificationController.AmbulanceNotificationDTO();
        notificationDTO.setUserId(user.getUserId());
        notificationDTO.setAmbulanceId(ambulance.getId());
        notificationDTO.setMessage(message);
        notificationDTO.setAmbulancePlateNumber(ambulance.getPlateNumber());
        notificationDTO.setDriverName(ambulance.getDriverName());
        notificationDTO.setDriverContact(ambulance.getDriverContact());
        notificationDTO.setEta(etaInMinutes);

        try {
            notificationController.sendAmbulanceDispatchedNotification(user.getUserId(), notificationDTO);
        } catch (Exception e) {
            throw new NotificationFailedException("Failed to send ambulance dispatched notification", e);
        }
    }
}
