package com.example.flexiMed.controller;

import com.example.flexiMed.dto.RequestDTO;
import com.example.flexiMed.enums.RequestStatus;
import com.example.flexiMed.service.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing service requests in the FlexiMed application.
 * It provides endpoints to create, view, and manage service requests.
 */
@RestController
@RequestMapping("/api/v1/requests")
public class RequestController {
    private final RequestService requestService;

    /**
     * Constructor to initialize the RequestController with the given RequestService.
     *
     * @param requestService The service responsible for managing requests.
     */
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    /**
     * Endpoint to create a new service request.
     *
     * @param request The RequestDTO object containing the request details.
     * @return A ResponseEntity containing the created RequestDTO.
     */
    @PostMapping
    public ResponseEntity<RequestDTO> createRequest(@RequestBody RequestDTO request) {
        return ResponseEntity.ok(requestService.createRequest(request));
    }

    /**
     * Endpoint to retrieve all service requests for a specific user.
     *
     * @param userId The ID of the user whose service requests are to be retrieved.
     * @return A ResponseEntity containing a list of RequestDTOs for the given user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RequestDTO>> getUserRequests(@PathVariable UUID userId) {
        List<RequestDTO> requests = requestService.getUserRequests(userId);
        return ResponseEntity.ok(requests);
    }

    /**
     * Endpoint to retrieve all service requests.
     *
     * @return A ResponseEntity containing a list of all RequestDTOs.
     */
    @GetMapping
    public ResponseEntity<List<RequestDTO>> getAllRequests() {
        List<RequestDTO> requests = requestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    /**
     * Endpoint to retrieve all service requests filtered by request status.
     *
     * @param status The request status to filter by.
     * @return A ResponseEntity containing a list of RequestDTOs.
     */
    @GetMapping("/status")
    public ResponseEntity<List<RequestDTO>> getRequestsByStatus(
            @RequestParam RequestStatus status) {

        List<RequestDTO> requests = requestService.getRequestsByStatus(status);
        return ResponseEntity.ok(requests);
    }

    /**
     * Endpoint to retrieve all service requests for a specific ambulance ID and with a specific request status.
     *
     * @param ambulanceId   The UUID of the ambulance to search for.
     * @param status        The {@link RequestStatus} to filter by.
     * @return A ResponseEntity containing a list of RequestDTOs matching the criteria.
     */
    @GetMapping("/ambulance/{ambulanceId}")
    public ResponseEntity<List<RequestDTO>> getRequestsByAmbulanceIdAndStatus(
            @PathVariable UUID ambulanceId,
            @RequestParam RequestStatus status) {
        List<RequestDTO> requests = requestService.getRequestsByAmbulanceIdAndStatus(ambulanceId, status);
        return ResponseEntity.ok(requests);
    }

    /**
     * Endpoint to update a service request by ID to COMPLETED and set the associated ambulance to available.
     *
     * @param id The UUID of the request to complete.
     * @return A ResponseEntity containing the updated RequestDTO.
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<RequestDTO> completeRequest(@PathVariable UUID id) {
        RequestDTO updatedRequest = requestService.completeRequest(id);
        return ResponseEntity.ok(updatedRequest);
    }
}
