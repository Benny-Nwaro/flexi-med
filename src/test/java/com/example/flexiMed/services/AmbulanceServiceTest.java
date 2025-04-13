package com.example.flexiMed.services;

import com.example.flexiMed.dto.AmbulanceDTO;
import com.example.flexiMed.dto.RequestDTO;
import com.example.flexiMed.enums.RequestStatus;
import com.example.flexiMed.mapper.AmbulanceMapper;
import com.example.flexiMed.model.AmbulanceEntity;
import com.example.flexiMed.model.RequestEntity;
import com.example.flexiMed.model.UserEntity;
import com.example.flexiMed.repository.AmbulanceRepository;
import com.example.flexiMed.repository.RequestRepository;
import com.example.flexiMed.service.AmbulanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link AmbulanceService} class.
 * These tests ensure that ambulance-related logic functions correctly and handles edge cases as expected.
 */
class AmbulanceServiceTest {

    @Mock
    private AmbulanceRepository ambulanceRepository;

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private AmbulanceService ambulanceService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests saving a new ambulance.
     * Ensures that the saved ambulance retains the correct driver name and that the repository save method is called once.
     */
    @Test
    void testSaveAmbulance() {
        AmbulanceDTO dto = new AmbulanceDTO();
        dto.setDriverName("John Doe");
        dto.setLatitude(10.0);
        dto.setLongitude(20.0);

        AmbulanceEntity entity = AmbulanceMapper.toEntity(dto);
        when(ambulanceRepository.save(any())).thenReturn(entity);

        AmbulanceDTO saved = ambulanceService.saveAmbulance(dto);

        assertEquals("John Doe", saved.getDriverName());
        verify(ambulanceRepository, times(1)).save(any());
    }

    /**
     * Tests retrieving all ambulances.
     * Verifies that the returned list size and content match the mock setup.
     */
    @Test
    void testGetAllAmbulances() {
        AmbulanceEntity entity = new AmbulanceEntity();
        entity.setDriverName("Driver A");

        when(ambulanceRepository.findAll()).thenReturn(List.of(entity));

        List<AmbulanceDTO> ambulances = ambulanceService.getAllAmbulances();

        assertEquals(1, ambulances.size());
        assertEquals("Driver A", ambulances.get(0).getDriverName());
    }

    /**
     * Tests successful dispatch of an ambulance to a valid emergency request.
     * Validates that:
     * - A closest available ambulance is found.
     * - Request status is updated to DISPATCHED.
     * - Arrival time is calculated.
     * - The returned RequestDTO contains the expected result.
     */
    @Test
    void testDispatchAmbulance_success() {
        // Setup: a dummy ambulance
        AmbulanceEntity ambulance = new AmbulanceEntity();
        UUID ambulanceId = UUID.randomUUID();
        ambulance.setId(ambulanceId);
        ambulance.setLatitude(10.0);
        ambulance.setLongitude(20.0);
        ambulance.setAvailabilityStatus(true);

        // Setup: a dummy user
        UserEntity user = new UserEntity();
        user.setUserId(UUID.randomUUID());  // Must be non-null for RequestDTO validation

        // Setup: a dummy request with required fields
        RequestEntity request = new RequestEntity();
        request.setLatitude(10.1);
        request.setLongitude(20.1);
        request.setUser(user);                      // Avoids IllegalArgumentException
        request.setDescription("Test emergency case"); // Avoids empty description error

        // Mocking repository methods
        when(ambulanceRepository.findByAvailabilityStatusIsTrue()).thenReturn(List.of(ambulance));
        when(ambulanceRepository.findById(ambulance.getId())).thenReturn(Optional.of(ambulance));
        when(ambulanceRepository.save(any())).thenReturn(ambulance);
        when(requestRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Act
        RequestDTO result = ambulanceService.dispatchAmbulance(request);

        // Assert
        assertEquals(RequestStatus.DISPATCHED, request.getRequestStatus());
        assertNotNull(result.getArrivalTime());
    }


    /**
     * Tests updating the location of an existing ambulance.
     * Validates that the new latitude and longitude are correctly updated and returned.
     */
    @Test
    void testUpdateLocation_success() {
        UUID id = UUID.randomUUID();
        AmbulanceEntity ambulance = new AmbulanceEntity();

        when(ambulanceRepository.findById(id)).thenReturn(Optional.of(ambulance));
        when(ambulanceRepository.save(any())).thenReturn(ambulance);

        AmbulanceDTO updated = ambulanceService.updateLocation(id, 12.34, 56.78);

        assertEquals(12.34, updated.getLatitude());
        assertEquals(56.78, updated.getLongitude());
    }

    /**
     * Tests finding the closest available ambulance to a given location.
     * Ensures that a non-null result is returned when available ambulances exist.
     */
    @Test
    void testFindClosestAmbulance_success() {
        AmbulanceEntity a1 = new AmbulanceEntity();
        a1.setLatitude(10.0);
        a1.setLongitude(20.0);
        a1.setAvailabilityStatus(true);

        when(ambulanceRepository.findByAvailabilityStatusIsTrue()).thenReturn(List.of(a1));

        AmbulanceDTO closest = ambulanceService.findClosestAmbulance(10.1, 20.1);

        assertNotNull(closest);
    }

}
