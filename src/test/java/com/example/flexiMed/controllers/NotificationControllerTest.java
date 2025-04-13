package com.example.flexiMed.controllers;

import com.example.flexiMed.controller.NotificationController;
import com.example.flexiMed.dto.AmbulanceNotificationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link NotificationController} class.
 * <p>
 * This class uses JUnit 5 and Mockito to verify the behavior of the
 * {@link NotificationController} methods, particularly the sending of ambulance
 * dispatch notifications.
 */
class NotificationControllerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate; // Mocked SimpMessagingTemplate
    private NotificationController notificationController; // Instance of the controller to test

    /**
     * Setup method executed before each test case.
     * <p>
     * Initializes the Mockito mocks and creates an instance of the
     * {@link NotificationController} with the mocked messaging template.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize Mockito mocks
        notificationController = new NotificationController(messagingTemplate); // Create the controller with the mock
    }

    /**
     * Tests the {@link NotificationController#sendAmbulanceDispatchedNotificationWithDelay(UUID, AmbulanceNotificationDTO, long)}
     * method.
     * <p>
     * This test verifies that the method correctly sends a notification with the
     * specified delay and that the {@link SimpMessagingTemplate} is called with the
     * expected arguments.
     *
     * @throws InterruptedException if the thread sleep is interrupted.
     */
    @Test
    void testSendAmbulanceDispatchedNotificationWithDelay() throws InterruptedException {
        // Given - Set up the test data
        UUID userId = UUID.randomUUID();
        AmbulanceNotificationDTO notificationDTO = new AmbulanceNotificationDTO();
        long delayMillis = 500;

        // Use ArgumentCaptor to capture the arguments passed to convertAndSendToUser
        ArgumentCaptor<String> userCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> destinationCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> payloadCaptor = ArgumentCaptor.forClass(Object.class);

        // When - Call the method under test
        notificationController.sendAmbulanceDispatchedNotificationWithDelay(userId, notificationDTO, delayMillis);

        // Verify that convertAndSendToUser was called with the correct arguments
        verify(messagingTemplate, times(1)).convertAndSendToUser(
                userCaptor.capture(),
                destinationCaptor.capture(),
                payloadCaptor.capture()
        );

        // Assert the captured values
        assertEquals(userId.toString(), userCaptor.getValue());
        assertEquals("/queue/ambulance-locations", destinationCaptor.getValue());
        assertEquals(notificationDTO, payloadCaptor.getValue());

        // Verify the delay.
        Thread.sleep(delayMillis + 100); // Allow extra time to ensure delay
    }

    /**
     * Tests the {@link NotificationController#sendAmbulanceDispatchedNotification(UUID, AmbulanceNotificationDTO)}
     * method.
     * <p>
     * This test verifies that the method calls
     * {@link NotificationController#sendAmbulanceDispatchedNotificationWithDelay(UUID, AmbulanceNotificationDTO, long)}
     * with the correct default delay.
     */
    @Test
    void testSendAmbulanceDispatchedNotification() {
        // Given - Set up test data
        UUID userId = UUID.randomUUID();
        AmbulanceNotificationDTO notificationDTO = new AmbulanceNotificationDTO();

        // When - Call the method under test
        notificationController.sendAmbulanceDispatchedNotification(userId, notificationDTO);

        // Then - Verify the expected behavior
        verify(messagingTemplate, times(1)).convertAndSendToUser(eq(userId.toString()), eq("/queue/ambulance-locations"), eq(notificationDTO));
    }
}

