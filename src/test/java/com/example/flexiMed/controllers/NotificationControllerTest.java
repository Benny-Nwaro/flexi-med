//package com.example.flexiMed.controllers;
//
//import com.example.flexiMed.controller.NotificationController;
//import com.example.flexiMed.dto.AmbulanceNotificationDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//
//import java.util.UUID;
//
//import static org.mockito.Mockito.verify;
//
///**
// * Unit tests for the NotificationController.
// * This class tests the functionality of the NotificationController, ensuring that it correctly
// * interacts with the SimpMessagingTemplate and handles the sending of ambulance dispatched notifications.
// */
//@ExtendWith(MockitoExtension.class)
//public class NotificationControllerTest {
//
//    @Mock
//    private SimpMessagingTemplate messagingTemplate;
//
//    @InjectMocks
//    private NotificationController notificationController;
//
//    private UUID userId;
//    private AmbulanceNotificationDTO notificationDTO;
//
//    /**
//     * Sets up the test environment before each test method.
//     * Initializes the necessary objects, including user ID and notification DTO.
//     */
//    @BeforeEach
//    void setUp() {
//        userId = UUID.randomUUID();
//        UUID ambulanceId = UUID.randomUUID();
//        notificationDTO = new AmbulanceNotificationDTO(
//                userId, ambulanceId, "Test Message", "ABC-123", "John Doe", "123-456-7890", "10 minutes");
//    }
//
//    /**
//     * Tests sending an ambulance dispatched notification.
//     * Verifies that the messaging template's convertAndSend method is called with the correct parameters.
//     */
//    @Test
//    void sendAmbulanceDispatchedNotification_shouldSendNotification() {
//        notificationController.sendAmbulanceDispatchedNotification(userId, notificationDTO);
//
//        verify(messagingTemplate).convertAndSend("/topic/ambulance/" + userId, notificationDTO);
//    }
//}