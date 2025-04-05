package com.example.flexiMed.services;

import com.example.flexiMed.dto.ServiceHistoryDTO;
import com.example.flexiMed.enums.EventType;
import com.example.flexiMed.exceptions.ErrorResponse.RequestNotFoundException;
import com.example.flexiMed.exceptions.ErrorResponse.ServiceHistoryNotFoundException;
import com.example.flexiMed.mapper.ServiceHistoryMapper;
import com.example.flexiMed.model.RequestEntity;
import com.example.flexiMed.model.ServiceHistoryEntity;
import com.example.flexiMed.repository.RequestRepository;
import com.example.flexiMed.repository.ServiceHistoryRepository;
import com.example.flexiMed.service.ServiceHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ServiceHistoryService.
 * This class tests the functionality of the ServiceHistoryService, ensuring that it correctly
 * interacts with the ServiceHistoryRepository and RequestRepository, and handles various
 * scenarios related to logging and retrieving service history records.
 */
@ExtendWith(MockitoExtension.class)
public class ServiceHistoryServiceTest {

    @Mock
    private ServiceHistoryRepository serviceHistoryRepository;

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private ServiceHistoryService serviceHistoryService;

    private UUID requestId;
    private ServiceHistoryDTO serviceHistoryDTO;
    private RequestEntity requestEntity;
    private ServiceHistoryEntity serviceHistoryEntity;

    /**
     * Sets up the test environment before each test method.
     * Initializes the necessary objects, including service history DTOs and entities,
     * and request entities.
     */
    @BeforeEach
    void setUp() {
        requestId = UUID.randomUUID();
        serviceHistoryDTO = new ServiceHistoryDTO();
        serviceHistoryDTO.setRequestId(requestId);
        serviceHistoryDTO.setDetails("Test service history");
        serviceHistoryDTO.setEventType(EventType.DISPATCHED.name());

        requestEntity = new RequestEntity();
        requestEntity.setId(requestId);

        serviceHistoryEntity = ServiceHistoryMapper.toEntity(serviceHistoryDTO, requestEntity);
    }

    /**
     * Tests the scenario where a service history event is logged successfully when the request exists.
     * Verifies that the service history is saved to the repository.
     */
    @Test
    void logEvent_shouldSaveServiceHistory_whenRequestExists() {
        when(requestRepository.findById(requestId)).thenReturn(Optional.of(requestEntity));

        serviceHistoryService.logEvent(serviceHistoryDTO);

        verify(serviceHistoryRepository, times(1)).save(any(ServiceHistoryEntity.class));
    }

    /**
     * Tests the scenario where a service history event is logged but the request does not exist.
     * Verifies that a RequestNotFoundException is thrown.
     */
    @Test
    void logEvent_shouldThrowRequestNotFoundException_whenRequestDoesNotExist() {
        when(requestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(RequestNotFoundException.class, () -> serviceHistoryService.logEvent(serviceHistoryDTO));

        verify(serviceHistoryRepository, never()).save(any(ServiceHistoryEntity.class));
    }

    /**
     * Tests retrieving service history records by request ID when history records exist.
     * Verifies that the correct list of service history DTOs is returned.
     */
    @Test
    void getHistoryByRequestId_shouldReturnListOfServiceHistoryDTOs_whenHistoryExists() {
        List<ServiceHistoryEntity> historyEntities = Arrays.asList(serviceHistoryEntity);
        when(serviceHistoryRepository.findByRequest_Id(requestId)).thenReturn(historyEntities);

        List<ServiceHistoryDTO> result = serviceHistoryService.getHistoryByRequestId(requestId);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(serviceHistoryDTO.getDetails(), result.get(0).getDetails());
    }

    /**
     * Tests retrieving service history records by request ID when no history records exist.
     * Verifies that a ServiceHistoryNotFoundException is thrown.
     */
    @Test
    void getHistoryByRequestId_shouldThrowServiceHistoryNotFoundException_whenHistoryDoesNotExist() {
        when(serviceHistoryRepository.findByRequest_Id(requestId)).thenReturn(Arrays.asList());

        assertThrows(ServiceHistoryNotFoundException.class, () -> serviceHistoryService.getHistoryByRequestId(requestId));
    }
}