package com.example.flexiMed.controllers;

import com.example.flexiMed.controller.AmbulanceController;
import com.example.flexiMed.dto.AmbulanceDTO;
import com.example.flexiMed.service.AmbulanceService;
import com.example.flexiMed.websocket.AmbulanceLocationHandler;
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
import static org.mockito.Mockito.*;

/**
 * Unit tests for the AmbulanceController.
 * This class tests the functionality of the AmbulanceController, ensuring that it correctly
 * interacts with the AmbulanceService and AmbulanceLocationHandler, and handles HTTP requests
 * for ambulance-related operations.
 */
@ExtendWith(MockitoExtension.class)
public class AmbulanceControllerTest {

    @Mock
    private AmbulanceService ambulanceService;

    @Mock
    private AmbulanceLocationHandler webSocketHandler;

    @InjectMocks
    private AmbulanceController ambulanceController;

    private UUID ambulanceId;
    private AmbulanceDTO ambulanceDTO;

    /**
     * Sets up the test environment before each test method.
     * Initializes the necessary objects, including an ambulance ID and ambulance DTO.
     */
    @BeforeEach
    void setUp() {
        ambulanceId = UUID.randomUUID();
        ambulanceDTO = new AmbulanceDTO();
        ambulanceDTO.setId(ambulanceId);
        ambulanceDTO.setAvailabilityStatus(true);
        ambulanceDTO.setDriverName("John Doe");
        ambulanceDTO.setDriverContact("123-456-7890");
        ambulanceDTO.setLatitude(1.0);
        ambulanceDTO.setLongitude(2.0);
    }

    /**
     * Tests retrieving all ambulances.
     * Verifies that the controller returns a list of ambulances with HTTP status OK.
     */
    @Test
    void getAllAmbulances_shouldReturnListOfAmbulances() {
        List<AmbulanceDTO> ambulanceDTOList = Arrays.asList(ambulanceDTO);
        when(ambulanceService.getAllAmbulances()).thenReturn(ambulanceDTOList);

        ResponseEntity<List<AmbulanceDTO>> response = ambulanceController.getAllAmbulances();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ambulanceDTOList, response.getBody());
    }

    /**
     * Tests creating a new ambulance.
     * Verifies that the controller returns the created ambulance with HTTP status OK.
     */
    @Test
    void createAmbulance_shouldReturnCreatedAmbulance() {
        when(ambulanceService.saveAmbulance(ambulanceDTO)).thenReturn(ambulanceDTO);

        ResponseEntity<AmbulanceDTO> response = ambulanceController.createAmbulance(ambulanceDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ambulanceDTO, response.getBody());
    }



    /**
     * Tests updating an ambulance's details.
     * Verifies that the controller returns the updated ambulance with HTTP status OK.
     */
    @Test
    void updateAmbulance_shouldReturnUpdatedAmbulance() {
        // Use the same arguments that will be passed to the method
        when(ambulanceService.updateAmbulance(ambulanceId, ambulanceDTO.isAvailabilityStatus(), ambulanceDTO.getDriverName(), ambulanceDTO.getDriverContact())).thenReturn(ambulanceDTO);

        ResponseEntity<AmbulanceDTO> response = ambulanceController.updateAmbulance(ambulanceId, ambulanceDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ambulanceDTO, response.getBody());
    }

    /**
     * Tests deleting an ambulance.
     * Verifies that the controller returns a success message with HTTP status OK and calls the service's delete method.
     */
    @Test
    void deleteAmbulance_shouldReturnSuccessMessage() {
        ResponseEntity<String> response = ambulanceController.deleteAmbulance(ambulanceId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ambulance deleted successfully.", response.getBody());
        verify(ambulanceService, times(1)).deleteAmbulance(ambulanceId);
    }
}