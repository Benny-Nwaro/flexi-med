package com.example.flexiMed.services;

import com.example.flexiMed.dto.RequestDTO;
import com.example.flexiMed.enums.RequestStatus;
import com.example.flexiMed.mapper.RequestMapper;
import com.example.flexiMed.model.AmbulanceEntity;
import com.example.flexiMed.model.RequestEntity;
import com.example.flexiMed.model.UserEntity;
import com.example.flexiMed.repository.AmbulanceRepository;
import com.example.flexiMed.repository.RequestRepository;
import com.example.flexiMed.repository.UserRepository;
import com.example.flexiMed.service.*;
import com.example.flexiMed.utils.GeoUtils;
import com.example.flexiMed.utils.TimeUtils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;
    @Mock
    private AmbulanceRepository ambulanceRepository;
    @Mock
    private AmbulanceService ambulanceService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PatientRecordsService patientRecordsService;
    @Mock
    private ServiceHistoryService serviceHistoryService;
    @Mock
    private UserService userService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private GeoUtils geoUtils;
    @Mock
    private TimeUtils timeUtils;

    @InjectMocks
    private RequestService requestService;

    private UUID userId;
    private UUID ambulanceId;
    private UUID requestId;
    private RequestDTO requestDTO;
    private RequestEntity requestEntity;
    private UserEntity userEntity;
    private AmbulanceEntity ambulanceEntity;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        ambulanceId = UUID.randomUUID();
        requestId = UUID.randomUUID();

        userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setPhoneNumber("1234567890");

        ambulanceEntity = new AmbulanceEntity();
        ambulanceEntity.setId(ambulanceId);
        ambulanceEntity.setLatitude(5.0);
        ambulanceEntity.setLongitude(5.0);
        ambulanceEntity.setDriverContact("9876543210");

        requestDTO = new RequestDTO();
        requestDTO.setUserId(userId);
        requestDTO.setLatitude(5.1);
        requestDTO.setLongitude(5.1);
        requestDTO.setDescription("Test emergency");

        requestEntity = RequestMapper.toEntity(requestDTO, userEntity, ambulanceEntity);
        requestEntity.setId(requestId);
        requestEntity.setRequestTime(LocalDateTime.now());
        requestEntity.setRequestStatus(RequestStatus.DISPATCHED);
    }

//    @Test
//    void createRequest_shouldCreateAndReturnRequestDTO() {
//        // Arrange: Mocking repository and service calls
//        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
//        when(ambulanceRepository.findFirstByAvailabilityStatusIsTrue()).thenReturn(Optional.of(ambulanceEntity));
//
//        RequestEntity savedRequestEntity = new RequestEntity();
//        savedRequestEntity.setId(UUID.randomUUID());
//        savedRequestEntity.setUser(userEntity);
//        savedRequestEntity.setAmbulance(ambulanceEntity);
//        savedRequestEntity.setDescription(requestDTO.getDescription());
//        savedRequestEntity.setLatitude(requestDTO.getLatitude());
//        savedRequestEntity.setLongitude(requestDTO.getLongitude());
//
//        when(requestRepository.save(any(RequestEntity.class))).thenReturn(savedRequestEntity);
//        when(ambulanceService.dispatchAmbulance(any(RequestEntity.class))).thenReturn(requestDTO);
//        when(geoUtils.calculateETA(requestDTO.getLatitude(), requestDTO.getLongitude(), ambulanceEntity.getLatitude(), ambulanceEntity.getLongitude())).thenReturn(10L);
//        when(timeUtils.formatTime(10L)).thenReturn("10 minutes");
//        when(userService.getUserByPhoneNumber(ambulanceEntity.getDriverContact())).thenReturn(Optional.of(UserMapper.toDTO(userEntity)));
//
//        // Act: Create a request and capture the result
//        RequestDTO result = requestService.createRequest(requestDTO);
//
//        // Assert: Check if the result is not null and description matches
//        assertNotNull(result);
//        assertEquals(requestDTO.getDescription(), result.getDescription());
//
//        // Capture arguments for patientRecordsService
//        ArgumentCaptor<PatientRecordsDTO> patientRecordCaptor = ArgumentCaptor.forClass(PatientRecordsDTO.class);
//        verify(patientRecordsService).addPatientRecord(patientRecordCaptor.capture());
//        PatientRecordsDTO capturedPatientRecord = patientRecordCaptor.getValue();
//        assertEquals(requestDTO.getUserId(), capturedPatientRecord.getPatientId());
//
//        // Capture arguments for notificationService
//        ArgumentCaptor<String> notificationMessageCaptor = ArgumentCaptor.forClass(String.class);
//
//        // Correct use of matchers in verify
//        verify(notificationService, times(2))
//                .sendUserNotifications(notificationMessageCaptor.capture(),
//                        eq(userEntity),
//                        eq(ambulanceEntity),
//                        anyString());
//
//        List<String> capturedMessages = notificationMessageCaptor.getAllValues();
//        assertTrue(capturedMessages.contains("Ambulance has been dispatched to your location"));
//        assertTrue(capturedMessages.contains("Your ambulance has been dispatched to Lat: " + requestDTO.getLatitude() + " and Long: " + requestDTO.getLongitude()));
//
//        // Verify service history and ambulance dispatch interactions
//        ServiceHistoryDTO expectedServiceHistoryDTO = new ServiceHistoryDTO();
//        expectedServiceHistoryDTO.setRequestId(savedRequestEntity.getId());
//        expectedServiceHistoryDTO.setDetails("Request created and ambulance dispatched.");
//        verify(serviceHistoryService).logEvent(eq(expectedServiceHistoryDTO));
//        verify(ambulanceService).dispatchAmbulance(eq(savedRequestEntity));
//    }




    @Test
    void createRequest_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> requestService.createRequest(requestDTO));
    }

    @Test
    void createRequest_shouldThrowException_whenAmbulanceNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(ambulanceRepository.findFirstByAvailabilityStatusIsTrue()).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> requestService.createRequest(requestDTO));
    }

    @Test
    void getAllRequests_shouldReturnListOfRequestDTOs() {
        when(requestRepository.findAll()).thenReturn(Arrays.asList(requestEntity));

        List<RequestDTO> result = requestService.getAllRequests();

        assertEquals(1, result.size());
        assertEquals(requestDTO.getDescription(), result.get(0).getDescription());
    }

    @Test
    void getUserRequests_shouldReturnListOfRequestDTOs() {
        when(requestRepository.findByUserUserId(userId)).thenReturn(Arrays.asList(requestEntity));

        List<RequestDTO> result = requestService.getUserRequests(userId);

        assertEquals(1, result.size());
        assertEquals(requestDTO.getDescription(), result.get(0).getDescription());
    }

    @Test
    void getUserRequests_shouldThrowException_whenNoRequestsFound() {
        when(requestRepository.findByUserUserId(userId)).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> requestService.getUserRequests(userId));
    }

//    @Test
//    void getDispatcher_shouldReturnUserEntity() {
//        when(userService.getUserByPhoneNumber(anyString())).thenReturn(Optional.of(UserMapper.toDTO(userEntity)));
//
//        UserEntity result = requestService.getDispatcher("9876543210");
//        result.setName("test name");
//
//        assertEquals(userEntity, result);
//    }

}