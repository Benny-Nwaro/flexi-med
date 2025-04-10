package com.example.flexiMed.controller;

import com.example.flexiMed.dto.AmbulanceNotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);


    /**
     * Constructor to initialize the NotificationController with the messaging template.
     *
     * @param messagingTemplate The SimpMessagingTemplate to send WebSocket messages.
     */
    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Sends a notification to a user about an ambulance dispatch with a short delay.
     *
     * @param userId          The ID of the user to whom the notification will be sent.
     * @param notificationDTO The DTO containing details of the ambulance dispatch.
     * @param delayMillis     The delay in milliseconds before sending the notification.
     */
    @Async
    public void sendAmbulanceDispatchedNotificationWithDelay(UUID userId, AmbulanceNotificationDTO notificationDTO, long delayMillis) {
        logger.info("INSIDE sendAmbulanceDispatchedNotificationWithDelay! for user: {}", userId);
        logger.info("Delaying notification for {} milliseconds...", delayMillis);
        try {
            TimeUnit.MILLISECONDS.sleep(delayMillis);
            logger.info("Sending delayed notification to user: {} on /queue/ambulance-locations", userId);
            // Send to the specific user via a user-specific queue
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(userId),
                    "/queue/ambulance-locations",
                    notificationDTO
            );
            logger.info("Delayed notification sent successfully to user: {}", userId);
        } catch (InterruptedException e) {
            logger.error("Thread interrupted during notification delay for user: {}", userId, e);
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }
    }

    /**
     * Sends a notification to a user about an ambulance dispatch with a default short delay (e.g., 1 second).
     *
     * @param userId          The ID of the user to whom the notification will be sent.
     * @param notificationDTO The DTO containing details of the ambulance dispatch.
     */
    public void sendAmbulanceDispatchedNotification(UUID userId, AmbulanceNotificationDTO notificationDTO) {
        sendAmbulanceDispatchedNotificationWithDelay(userId, notificationDTO, 1000); // Default delay of 1 second
    }

}
