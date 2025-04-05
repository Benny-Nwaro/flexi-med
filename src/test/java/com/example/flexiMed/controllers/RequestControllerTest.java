package com.example.flexiMed.controllers;

import com.example.flexiMed.controller.RequestController;
import com.example.flexiMed.dto.RequestDTO;
import com.example.flexiMed.service.RequestService;
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
 * Unit tests for the RequestController.
 * This class tests the functionality of the RequestController, ensuring that it correctly
 * interacts with the RequestService and handles HTTP requests for creating and retrieving requests.
 */
@ExtendWith(MockitoExtension.class)
public class RequestControllerTest {

    @Mock
    private RequestService requestService;

    @InjectMocks
    private RequestController requestController;

    private UUID userId;
    private RequestDTO requestDTO;

    /**
     * Sets up the test environment before each test method.
     * Initializes the necessary objects, including a user ID and a request DTO.
     */
    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        requestDTO = new RequestDTO();
        requestDTO.setId(UUID.randomUUID());
        requestDTO.setUserId(userId);
        requestDTO.setDescription("Emergency request");
    }

    /**
     * Tests creating a new request.
     * Verifies that the controller returns the created request with HTTP status OK.
     */
    @Test
    void createRequest_shouldReturnCreatedRequest() {
        when(requestService.createRequest(requestDTO)).thenReturn(requestDTO);

        ResponseEntity<RequestDTO> response = requestController.createRequest(requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(requestDTO, response.getBody());
    }

    /**
     * Tests retrieving requests for a specific user.
     * Verifies that the controller returns a list of requests with HTTP status OK.
     */
    @Test
    void getUserRequests_shouldReturnListOfUserRequests() {
        List<RequestDTO> requestDTOList = Arrays.asList(requestDTO);
        when(requestService.getUserRequests(userId)).thenReturn(requestDTOList);

        ResponseEntity<List<RequestDTO>> response = requestController.getUserRequests(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(requestDTOList, response.getBody());
    }

    /**
     * Tests retrieving all requests.
     * Verifies that the controller returns a list of all requests with HTTP status OK.
     */
    @Test
    void getAllRequests_shouldReturnListOfAllRequests() {
        List<RequestDTO> requestDTOList = Arrays.asList(requestDTO);
        when(requestService.getAllRequests()).thenReturn(requestDTOList);

        ResponseEntity<List<RequestDTO>> response = requestController.getAllRequests();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(requestDTOList, response.getBody());
    }
}