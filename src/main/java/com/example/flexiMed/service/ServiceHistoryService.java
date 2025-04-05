package com.example.flexiMed.service;

import com.example.flexiMed.dto.ServiceHistoryDTO;
import com.example.flexiMed.exceptions.ErrorResponse.ServiceHistoryNotFoundException;
import com.example.flexiMed.exceptions.ErrorResponse.RequestNotFoundException;
import com.example.flexiMed.mapper.ServiceHistoryMapper;
import com.example.flexiMed.model.RequestEntity;
import com.example.flexiMed.model.ServiceHistoryEntity;
import com.example.flexiMed.repository.RequestRepository;
import com.example.flexiMed.repository.ServiceHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing service history records.
 */
@Service
public class ServiceHistoryService {

    private final ServiceHistoryRepository serviceHistoryRepository;
    private final RequestRepository requestRepository;

    /**
     * Constructor to initialize the service history and request repositories.
     *
     * @param serviceHistoryRepository Repository for service history records.
     * @param requestRepository Repository for request records.
     */
    public ServiceHistoryService(ServiceHistoryRepository serviceHistoryRepository, RequestRepository requestRepository) {
        this.serviceHistoryRepository = serviceHistoryRepository;
        this.requestRepository = requestRepository;
    }

    /**
     * Logs a new service history event associated with a request.
     *
     * @param serviceHistory Data Transfer Object containing service history details.
     * @throws RequestNotFoundException if the associated request is not found.
     */
    public void logEvent(ServiceHistoryDTO serviceHistory) {
        RequestEntity requestEntity = requestRepository.findById(serviceHistory.getRequestId())
                .orElseThrow(() -> new RequestNotFoundException("Request with ID " + serviceHistory.getRequestId() + " not found"));

        ServiceHistoryEntity serviceEntity = ServiceHistoryMapper.toEntity(serviceHistory, requestEntity);
        serviceHistoryRepository.save(serviceEntity);
    }

    /**
     * Retrieves the service history for a given request ID.
     *
     * @param requestId The unique identifier of the request.
     * @return A list of service history records associated with the request.
     * @throws ServiceHistoryNotFoundException if no history records are found for the given request ID.
     */
    public List<ServiceHistoryDTO> getHistoryByRequestId(UUID requestId) {
        List<ServiceHistoryEntity> historyEntities = serviceHistoryRepository.findByRequest_Id(requestId);

        if (historyEntities.isEmpty()) {
            throw new ServiceHistoryNotFoundException("No service history found for request ID: " + requestId);
        }

        return historyEntities.stream()
                .map(ServiceHistoryMapper::toDTO)
                .collect(Collectors.toList());
    }
}
