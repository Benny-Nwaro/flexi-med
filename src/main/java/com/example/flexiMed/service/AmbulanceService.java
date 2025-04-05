package com.example.flexiMed.service;

import com.example.flexiMed.dto.AmbulanceDTO;
import com.example.flexiMed.dto.RequestDTO;
import com.example.flexiMed.enums.RequestStatus;
import com.example.flexiMed.exceptions.ErrorResponse;
import com.example.flexiMed.mapper.AmbulanceMapper;
import com.example.flexiMed.mapper.RequestMapper;
import com.example.flexiMed.model.AmbulanceEntity;
import com.example.flexiMed.model.RequestEntity;
import com.example.flexiMed.repository.AmbulanceRepository;
import com.example.flexiMed.repository.RequestRepository;
import com.example.flexiMed.utils.GeoUtils;
import com.example.flexiMed.utils.LocationUtils;
import jakarta.transaction.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for handling ambulance-related operations.
 * This class provides functionalities such as creating, updating, deleting,
 * retrieving ambulances, finding the closest available ambulance, and dispatching ambulances.
 */
@Service
public class AmbulanceService {

    private final AmbulanceRepository ambulanceRepository;
    private final RequestRepository requestRepository;

    /**
     * Constructs an {@code AmbulanceService} with the specified repositories.
     *
     * @param ambulanceRepository The repository for accessing ambulance records.
     * @param requestRepository   The repository for accessing request records.
     */
    public AmbulanceService(AmbulanceRepository ambulanceRepository, RequestRepository requestRepository) {
        this.ambulanceRepository = ambulanceRepository;
        this.requestRepository = requestRepository;
    }

    /**
     * Saves a new ambulance record to the database.
     *
     * @param ambulance The AmbulanceDTO containing ambulance data to be saved.
     * @return The saved AmbulanceDTO.
     * @throws RuntimeException If a concurrent update occurs due to optimistic locking.
     */
    public AmbulanceDTO saveAmbulance(AmbulanceDTO ambulance) {
        try {
            AmbulanceEntity newAmbulance = AmbulanceMapper.toEntity(ambulance);
            return AmbulanceMapper.toDTO(ambulanceRepository.save(newAmbulance));
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new RuntimeException("Ambulance record was modified by another transaction. Please retry.", e);
        }
    }

    /**
     * Retrieves all ambulances from the database.
     *
     * @return A list of AmbulanceDTO objects representing all ambulances.
     */
    public List<AmbulanceDTO> getAllAmbulances() {
        return ambulanceRepository.findAll().stream().map(AmbulanceMapper::toDTO).toList();
    }

    /**
     * Updates an ambulance's availability, driver name, and driver contact.
     *
     * @param id               The unique identifier of the ambulance.
     * @param availabilityStatus The updated availability status.
     * @param driverName       The updated driver name.
     * @param driverContact    The updated driver contact information.
     * @return The updated AmbulanceDTO.
     * @throws RuntimeException If the ambulance is not found.
     */
    @Transactional
    public AmbulanceDTO updateAmbulance(UUID id, boolean availabilityStatus, String driverName, String driverContact) {
        return ambulanceRepository.findById(id)
                .map(ambulance -> {
                    ambulance.setAvailabilityStatus(availabilityStatus);
                    ambulance.setDriverName(driverName);
                    ambulance.setDriverContact(driverContact);
                    ambulance.setLastUpdatedAt(LocalDateTime.now());
                    return AmbulanceMapper.toDTO(ambulanceRepository.save(ambulance));
                })
                .orElseThrow(() -> new RuntimeException("Ambulance not found with ID: " + id));
    }

    /**
     * Deletes an ambulance from the database if it is not available.
     *
     * @param id The unique identifier of the ambulance to be deleted.
     * @throws ErrorResponse.InvalidAmbulanceIdException  If the provided ID is null.
     * @throws ErrorResponse.AmbulanceNotAvailableException If the ambulance is not found or is available.
     */
    @Transactional
    public void deleteAmbulance(UUID id) {
        if (id == null) {
            throw new ErrorResponse.InvalidAmbulanceIdException("Ambulance ID cannot be null.");
        }

        AmbulanceEntity ambulance = ambulanceRepository.findById(id)
                .orElseThrow(() -> new ErrorResponse.AmbulanceNotAvailableException("Ambulance not found."));

        if (!ambulance.isAvailabilityStatus()) {
            ambulanceRepository.deleteById(id);
        } else {
            throw new ErrorResponse.AmbulanceNotAvailableException("Ambulance is not available for deletion.");
        }
    }

    /**
     * Updates the geographic location of an ambulance.
     *
     * @param id        The unique identifier of the ambulance.
     * @param latitude  The updated latitude.
     * @param longitude The updated longitude.
     * @return The updated AmbulanceDTO.
     * @throws RuntimeException If the ambulance is not found.
     */
    @Transactional
    public AmbulanceDTO updateLocation(UUID id, double latitude, double longitude) {
        return ambulanceRepository.findById(id)
                .map(ambulance -> {
                    ambulance.setLatitude(latitude);
                    ambulance.setLongitude(longitude);
                    ambulance.setLastUpdatedAt(LocalDateTime.now());
                    return AmbulanceMapper.toDTO(ambulanceRepository.save(ambulance));
                })
                .orElseThrow(() -> new RuntimeException("Ambulance not found with ID: " + id));
    }

    /**
     * Finds the closest available ambulance to a given user's location.
     *
     * @param userLatitude  The latitude of the user.
     * @param userLongitude The longitude of the user.
     * @return The closest available AmbulanceDTO.
     * @throws RuntimeException If no available ambulances are found or a suitable ambulance cannot be found.
     */
    public AmbulanceDTO findClosestAmbulance(double userLatitude, double userLongitude) {
        List<AmbulanceEntity> availableAmbulances = ambulanceRepository.findByAvailabilityStatusIsTrue();

        if (availableAmbulances.isEmpty()) {
            throw new RuntimeException("No available ambulances at the moment");
        }

        // Find the closest ambulance based on location.
        Optional<AmbulanceEntity> closestAmbulance = availableAmbulances.stream()
                .min(Comparator.comparingDouble(a -> LocationUtils.calculateDistance(userLatitude, userLongitude, a.getLatitude(), a.getLongitude())));

        return closestAmbulance
                .map(AmbulanceMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Could not find a suitable ambulance"));
    }

    /**
     * Dispatches an ambulance to handle a request.
     *
     * @param request The RequestEntity containing request details.
     * @return The updated RequestDTO with arrival time.
     * @throws IllegalArgumentException If the closest ambulance is not found.
     */
    @Transactional
    public RequestDTO dispatchAmbulance(RequestEntity request) {
        // Find the closest available ambulance.
        AmbulanceDTO ambulanceDTO = findClosestAmbulance(request.getLatitude(), request.getLongitude());
        AmbulanceEntity ambulance = ambulanceRepository.findById(ambulanceDTO.getId()).orElseThrow(() ->
                new IllegalArgumentException(("Ambulance not found")));

        // Assign the ambulance to the request and update request status.
        request.setAmbulance(ambulance);
        request.setRequestStatus(RequestStatus.DISPATCHED);
        request.setDispatchTime(LocalDateTime.now());

        // Update ambulance availability to false.
        ambulance.setAvailabilityStatus(false);
        ambulanceRepository.save(ambulance);

        // Calculate estimated time of arrival (ETA).
        double userLatitude = request.getLatitude();
        double userLongitude = request.getLongitude();
        double ambulanceLatitude = ambulance.getLatitude();
        double ambulanceLongitude = ambulance.getLongitude();

        long etaInMinutes = GeoUtils.calculateETA(ambulanceLatitude, ambulanceLongitude, userLatitude, userLongitude);

        // Save the estimated arrival time in the request.
        request.setArrivalTime(LocalDateTime.now().plusMinutes(etaInMinutes));
        requestRepository.save(request);

        // Convert the updated request to DTO and include the arrival time in the response.
        RequestDTO responseDTO = RequestMapper.toDTO(request);
        responseDTO.setArrivalTime(request.getArrivalTime());
        return responseDTO;
    }
}