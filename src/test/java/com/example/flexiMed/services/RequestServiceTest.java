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
}