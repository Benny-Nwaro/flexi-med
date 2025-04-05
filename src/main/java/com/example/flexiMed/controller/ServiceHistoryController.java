package com.example.flexiMed.controller;

import com.example.flexiMed.dto.ServiceHistoryDTO;
import com.example.flexiMed.service.ServiceHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controller to manage service history in the FlexiMed application.
 * Provides an endpoint for retrieving service history records based on a request ID.
 */
@RestController
@RequestMapping("/api/v1/service-history")
public class ServiceHistoryController {
    private final ServiceHistoryService serviceHistoryService;

    /**
     * Constructor to initialize the ServiceHistoryController with the given ServiceHistoryService.
     *
     * @param serviceHistoryService The service responsible for managing service history records.
     */
    public ServiceHistoryController(ServiceHistoryService serviceHistoryService) {
        this.serviceHistoryService = serviceHistoryService;
    }

    /**
     * Endpoint to get service history records by the associated request ID.
     *
     * @param requestId The ID of the request for which service history is to be retrieved.
     * @return A list of service history records related to the specified request ID.
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<List<ServiceHistoryDTO>> getHistoryByRequest(@PathVariable UUID requestId) {
        return ResponseEntity.ok(serviceHistoryService.getHistoryByRequestId(requestId));
    }
}
