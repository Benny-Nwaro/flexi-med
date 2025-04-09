package com.example.flexiMed.controller;

import com.example.flexiMed.dto.AmbulanceNotificationDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Constructor to initialize the NotificationController with the messaging template.
     *
     * @param messagingTemplate The SimpMessagingTemplate to send WebSocket messages.
     */
    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Sends a notification to a user about an ambulance dispatch.
     *
     * @param userId       The ID of the user to whom the notification will be sent.
     * @param notificationDTO The DTO containing details of the ambulance dispatch.
     */
    public void sendAmbulanceDispatchedNotification(UUID userId, AmbulanceNotificationDTO notificationDTO) {
        System.out.println("INSIDE sendAmbulanceDispatchedNotification!");
        messagingTemplate.convertAndSend("/topic/ambulance/" + userId, notificationDTO);
    }
}
