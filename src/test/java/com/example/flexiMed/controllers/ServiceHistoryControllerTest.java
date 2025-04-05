package com.example.flexiMed.controllers;

import com.example.flexiMed.controller.ServiceHistoryController;
import com.example.flexiMed.dto.ServiceHistoryDTO;
import com.example.flexiMed.service.ServiceHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the ServiceHistoryController.
 * This class tests the functionality of the ServiceHistoryController, ensuring that it correctly
 * interacts with the ServiceHistoryService and handles HTTP requests for retrieving service history.
 */
@ExtendWith(MockitoExtension.class)
public class ServiceHistoryControllerTest {

    @Mock
    private ServiceHistoryService serviceHistoryService;

    @InjectMocks
    private ServiceHistoryController serviceHistoryController;

    private UUID requestId;
    private ServiceHistoryDTO serviceHistoryDTO;

    /**
     * Sets up the test environment before each test method.
     * Initializes the necessary objects, including a request ID and a service history DTO.
     */
    @BeforeEach
    void setUp() {
        requestId = UUID.randomUUID();
        serviceHistoryDTO = new ServiceHistoryDTO();
        serviceHistoryDTO.setId(UUID.randomUUID());
        serviceHistoryDTO.setRequestId(requestId);
        serviceHistoryDTO.setDetails("Test service history details");
    }

    /**
     * Tests retrieving service history by request ID.
     * Verifies that the controller returns a list of service history DTOs with HTTP status OK.
     */
    @Test
    void getHistoryByRequest_shouldReturnListOfServiceHistoryDTOs() {
        List<ServiceHistoryDTO> serviceHistoryDTOList = Arrays.asList(serviceHistoryDTO);
        when(serviceHistoryService.getHistoryByRequestId(requestId)).thenReturn(serviceHistoryDTOList);

        ResponseEntity<List<ServiceHistoryDTO>> response = serviceHistoryController.getHistoryByRequest(requestId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(serviceHistoryDTOList, response.getBody());
    }
}