package com.example.flexiMed.services;

import com.example.flexiMed.controller.NotificationController;
import com.example.flexiMed.dto.AmbulanceNotificationDTO;
import com.example.flexiMed.exceptions.ErrorResponse.NotificationFailedException;
import com.example.flexiMed.model.AmbulanceEntity;
import com.example.flexiMed.model.UserEntity;
import com.example.flexiMed.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the NotificationService.
 * This class tests the functionality of the NotificationService, ensuring that it interacts
 * correctly with the NotificationController and handles various scenarios related to sending
 * user notifications, especially ambulance dispatch notifications.
 */
@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationController notificationController;

    @InjectMocks
    private NotificationService notificationService;

    private UserEntity userEntity;
    private AmbulanceEntity ambulanceEntity;
    private AmbulanceNotificationDTO notificationDTO;
    private String message;
    private String eta;
    private UUID userId;
    private UUID ambulanceId;

    /**
     * Sets up the test environment before each test method.
     * Initializes the necessary objects, including user and ambulance entities,
     * and a notification DTO.
     */
    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        ambulanceId = UUID.randomUUID();

        userEntity = new UserEntity();
        userEntity.setUserId(userId);

        ambulanceEntity = new AmbulanceEntity();
        ambulanceEntity.setId(ambulanceId);
        ambulanceEntity.setPlateNumber("ABC-123");
        ambulanceEntity.setDriverName("John Doe");
        ambulanceEntity.setDriverContact("123-456-7890");

        message = "Test message";
        eta = "10 minutes";

        notificationDTO = new AmbulanceNotificationDTO();
        notificationDTO.setUserId(userId);
        notificationDTO.setAmbulanceId(ambulanceId);
        notificationDTO.setMessage(message);
        notificationDTO.setAmbulancePlateNumber(ambulanceEntity.getPlateNumber());
        notificationDTO.setDriverName(ambulanceEntity.getDriverName());
        notificationDTO.setDriverContact(ambulanceEntity.getDriverContact());
        notificationDTO.setEta(eta);
    }

    /**
     * Tests the scenario where user notifications are sent successfully.
     * Verifies that the NotificationController's sendAmbulanceDispatchedNotification method is called
     * with the correct parameters and that the captured DTO matches the expected values.
     */
    @Test
    void sendUserNotifications_shouldCallNotificationController() {
        notificationService.sendUserNotifications(message, userEntity, ambulanceEntity, eta);

        ArgumentCaptor<AmbulanceNotificationDTO> notificationDTOArgumentCaptor =
                ArgumentCaptor.forClass(AmbulanceNotificationDTO.class);

        verify(notificationController).sendAmbulanceDispatchedNotification(eq(userId), notificationDTOArgumentCaptor.capture());

        AmbulanceNotificationDTO capturedDTO = notificationDTOArgumentCaptor.getValue();

        // Verify field values
        assertEquals(userId, capturedDTO.getUserId());
        assertEquals(ambulanceId, capturedDTO.getAmbulanceId());
        assertEquals(message, capturedDTO.getMessage());
        assertEquals(ambulanceEntity.getPlateNumber(), capturedDTO.getAmbulancePlateNumber());
        assertEquals(ambulanceEntity.getDriverName(), capturedDTO.getDriverName());
        assertEquals(ambulanceEntity.getDriverContact(), capturedDTO.getDriverContact());
        assertEquals(eta, capturedDTO.getEta());
    }

    /**
     * Tests the scenario where the NotificationController throws an exception during notification sending.
     * Verifies that a NotificationFailedException is thrown by the NotificationService.
     */
    @Test
    void sendUserNotifications_shouldThrowNotificationFailedException_whenNotificationControllerThrowsException() {
        doThrow(new RuntimeException("Notification failed")).when(notificationController)
                .sendAmbulanceDispatchedNotification(any(), any());

        assertThrows(NotificationFailedException.class, () ->
                notificationService.sendUserNotifications(message, userEntity, ambulanceEntity, eta));
    }
}