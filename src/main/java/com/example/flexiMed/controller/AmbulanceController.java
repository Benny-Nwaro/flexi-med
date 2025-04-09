package com.example.flexiMed.controller;

import com.example.flexiMed.dto.AmbulanceDTO;
import com.example.flexiMed.service.AmbulanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controller class responsible for handling ambulance-related API requests.
 * This includes operations like retrieving all ambulances, creating new ambulances,
 * updating ambulance information, and deleting ambulances.
 * It also handles the functionality for updating ambulance locations and notifying users via WebSocket.
 */
@RestController
@RequestMapping("/api/v1/ambulances")
public class AmbulanceController {

    private final AmbulanceService ambulanceService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Constructor to inject the required services.
     *
     * @param ambulanceService The service class that handles ambulance-related operations.
     * @param messagingTemplate The messaging template used for WebSocket communication.
     */
    public AmbulanceController(AmbulanceService ambulanceService,  SimpMessagingTemplate messagingTemplate) {
        this.ambulanceService = ambulanceService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Endpoint to retrieve a list of all ambulances.
     *
     * @return A ResponseEntity containing a list of all AmbulanceDTOs.
     */
    @GetMapping
    public ResponseEntity<List<AmbulanceDTO>> getAllAmbulances() {
        // Fetch all ambulances from the service and return as a response
        return ResponseEntity.ok(ambulanceService.getAllAmbulances());
    }

    /**
     * Endpoint to create a new ambulance.
     *
     * @param ambulanceDTO The data transfer object containing ambulance details to be created.
     * @return A ResponseEntity containing the created AmbulanceDTO.
     */
    @PostMapping
    public ResponseEntity<AmbulanceDTO> createAmbulance(@RequestBody AmbulanceDTO ambulanceDTO) {
        // Create a new ambulance using the service and return the created ambulance DTO
        return ResponseEntity.ok(ambulanceService.saveAmbulance(ambulanceDTO));
    }

    /**
     * Endpoint to update the location of a specific ambulance.
     * This updates the location in the database and notifies the requesting user through WebSocket.
     *
     * @param id        The UUID of the ambulance whose location is being updated.
     * @param latitude  The latitude of the new location.
     * @param longitude The longitude of the new location.
     * @param userId    The user ID of the person requesting the update.
     * @return A ResponseEntity containing the updated AmbulanceDTO.
     */
    @PutMapping("/{id}/update-location")
    public ResponseEntity<AmbulanceDTO> updateLocation(@PathVariable UUID id,
                                                       @RequestParam double latitude,
                                                       @RequestParam double longitude,
                                                       @RequestParam String userId) {

        // Update the ambulance location in the database using the ambulance service
        AmbulanceDTO updatedAmbulance = ambulanceService.updateLocation(id, latitude, longitude);

        // Prepare the location data to send through WebSocket
        Map<String, String> locationData = new HashMap<>();
        locationData.put("ambulanceId", id.toString());
        locationData.put("lat", String.valueOf(latitude));
        locationData.put("lng", String.valueOf(longitude));

        // Send location update to the requesting user via STOMP (user destination)
        messagingTemplate.convertAndSendToUser(userId, "/queue/ambulance-locations", locationData);

        // Return the updated ambulance DTO as a response
        return ResponseEntity.ok(updatedAmbulance);
    }

    /**
     * Endpoint to update general information of an ambulance, such as availability status, driver's name, and contact.
     *
     * @param id            The UUID of the ambulance to be updated.
     * @param ambulanceDTO  The data transfer object containing updated ambulance details.
     * @return A ResponseEntity containing the updated AmbulanceDTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AmbulanceDTO> updateAmbulance(
            @PathVariable UUID id,
            @RequestBody AmbulanceDTO ambulanceDTO
    ) {
        // Update the ambulance's general information (availability, driver info) using the service
        AmbulanceDTO updatedAmbulance = ambulanceService.updateAmbulance(id, ambulanceDTO.isAvailabilityStatus(),
                ambulanceDTO.getDriverName(), ambulanceDTO.getDriverContact(), ambulanceDTO.getDriverId());

        // Return the updated ambulance DTO
        return ResponseEntity.ok(updatedAmbulance);
    }

    /**
     * Fetches ambulance details by ID.
     *
     * @param id The unique identifier of the ambulance.
     * @return A ResponseEntity containing the AmbulanceDTO with the ambulance details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AmbulanceDTO> getAmbulanceById(@PathVariable UUID id) {
        // Fetch the ambulance details from the service and return the DTO
        AmbulanceDTO ambulanceDTO = ambulanceService.getAmbulanceById(id);
        return ResponseEntity.ok(ambulanceDTO);
    }

    /**
     * Endpoint to fetch ambulance details by driver ID.
     *
     * @param driverId The UUID of the driver.
     * @return A ResponseEntity containing the AmbulanceDTO for the driver.
     */
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<AmbulanceDTO> getAmbulanceByDriverId(@PathVariable UUID driverId) {
        // Fetch the ambulance details based on the driver's ID using the service
        AmbulanceDTO ambulanceDTO = ambulanceService.findAmbulanceByDriverId(driverId);
        return ResponseEntity.ok(ambulanceDTO);
    }

    /**
     * Endpoint to delete an ambulance from the system.
     *
     * @param id The UUID of the ambulance to be deleted.
     * @return A ResponseEntity indicating successful deletion of the ambulance.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAmbulance(@PathVariable UUID id) {
        // Delete the ambulance using the service
        ambulanceService.deleteAmbulance(id);

        // Return a success message
        return ResponseEntity.ok("Ambulance deleted successfully.");
    }
}
