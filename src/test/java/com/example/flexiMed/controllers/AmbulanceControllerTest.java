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
        ambulanceDTO.setDriverId(UUID.randomUUID());  // Ensure this is set
    }

    @Test
    void getAllAmbulances_shouldReturnListOfAmbulances() {
        List<AmbulanceDTO> ambulanceDTOList = Arrays.asList(ambulanceDTO);
        when(ambulanceService.getAllAmbulances()).thenReturn(ambulanceDTOList);

        ResponseEntity<List<AmbulanceDTO>> response = ambulanceController.getAllAmbulances();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ambulanceDTOList, response.getBody());
    }


    @Test
    void updateAmbulance_shouldReturnUpdatedAmbulance() {
        when(ambulanceService.updateAmbulance(eq(ambulanceId), eq(ambulanceDTO.isAvailabilityStatus()),
                eq(ambulanceDTO.getDriverName()), eq(ambulanceDTO.getDriverContact()), eq(ambulanceDTO.getDriverId())))
                .thenReturn(ambulanceDTO);

        ResponseEntity<AmbulanceDTO> response = ambulanceController.updateAmbulance(ambulanceId, ambulanceDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ambulanceDTO, response.getBody());
    }

    @Test
    void deleteAmbulance_shouldReturnSuccessMessage() {
        ResponseEntity<String> response = ambulanceController.deleteAmbulance(ambulanceId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ambulance deleted successfully.", response.getBody());
        verify(ambulanceService, times(1)).deleteAmbulance(ambulanceId);
    }
}
