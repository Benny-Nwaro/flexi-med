package com.example.flexiMed.service;

import com.example.flexiMed.dto.AmbulanceDTO;
import com.example.flexiMed.dto.RequestDTO;
import com.example.flexiMed.enums.RequestStatus;
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
     * Constructs an AmbulanceService with the specified repositories.
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
     * Retrieves an ambulance by its ID.
     *
     * @param id The unique identifier of the ambulance.
     * @return The AmbulanceDTO containing the details of the ambulance.
     * @throws RuntimeException If the ambulance is not found.
     */
    public AmbulanceDTO getAmbulanceById(UUID id) {
        return ambulanceRepository.findById(id)
                .map(AmbulanceMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Ambulance not found with ID: " + id));
    }

    /**
     * Updates an ambulance's availability, driver name, driver contact, and driver ID.
     *
     * @param id               The unique identifier of the ambulance.
     * @param availabilityStatus The updated availability status.
     * @param driverName       The updated driver name.
     * @param driverContact    The updated driver contact information.
     * @param driverId         The updated driver ID.
     * @return The updated AmbulanceDTO.
     * @throws RuntimeException If the ambulance is not found.
     */
    @Transactional
    public AmbulanceDTO updateAmbulance(UUID id, boolean availabilityStatus, String driverName, String driverContact, UUID driverId) {
        return ambulanceRepository.findById(id)
                .map(ambulance -> {
                    ambulance.setAvailabilityStatus(availabilityStatus);
                    ambulance.setDriverName(driverName);
                    ambulance.setDriverContact(driverContact);
                    ambulance.setDriverId(driverId); // Set the driver ID
                    ambulance.setLastUpdatedAt(LocalDateTime.now());
                    return AmbulanceMapper.toDTO(ambulanceRepository.save(ambulance));
                })
                .orElseThrow(() -> new RuntimeException("Ambulance not found with ID: " + id));
    }

    /**
     * Deletes an ambulance with the specified ID, but only if there are no
     * incomplete requests associated with that ambulance.
     */
    @Transactional
    public void deleteAmbulance(UUID id) {
        // Input validation: Ensure the provided ambulance ID is not null.
        if (id == null) {
            throw new IllegalArgumentException("Ambulance ID cannot be null");
        }
        // Query the request repository to count the number of requests associated with
        // the given ambulance ID that are in a status other than 'COMPLETED'.
        long count = requestRepository.countByAmbulance_IdAndRequestStatusNot(id, RequestStatus.COMPLETED);

        // Conditional deletion: If no incomplete requests are found for the ambulance,
        // proceed with deleting the ambulance from the ambulance repository.
        if (count > 0) {
            ambulanceRepository.deleteById(id);
        }
    }

    @Transactional
    public AmbulanceDTO updateLocation(UUID id, double latitude, double longitude) {
        int maxRetries = 3;

        while (maxRetries-- > 0) {
            try {
                return ambulanceRepository.findById(id)
                        .map(ambulance -> {
                            ambulance.setLatitude(latitude);
                            ambulance.setLongitude(longitude);
                            ambulance.setLastUpdatedAt(LocalDateTime.now());
                            return AmbulanceMapper.toDTO(ambulanceRepository.save(ambulance));
                        })
                        .orElseThrow(() -> new RuntimeException("Ambulance not found with ID: " + id));

            } catch (ObjectOptimisticLockingFailureException e) {
                if (maxRetries == 0) throw e; // give up after max attempts
                try {
                    Thread.sleep(50); // small delay before retry
                } catch (InterruptedException ignored) {}
            }
        }

        throw new RuntimeException("Failed to update ambulance location after retries.");
    }

    /**
     * Retrieves the location (latitude and longitude) of an ambulance by its ID.
     *
     * @param ambulanceId The unique identifier of the ambulance.
     * @return An Optional containing an AmbulanceDTO with only the latitude and longitude,
     * or an empty Optional if the ambulance is not found.
     */
    public Optional<AmbulanceDTO> findAmbulanceLocationById(UUID ambulanceId) {
        return ambulanceRepository.findById(ambulanceId)
                .map(ambulance -> {
                    AmbulanceDTO dto = new AmbulanceDTO();
                    dto.setLatitude(ambulance.getLatitude());
                    dto.setLongitude(ambulance.getLongitude());
                    return dto;
                });
    }



    /**
     * Finds an ambulance assigned to a specific driver.
     *
     * @param driverId The ID of the driver.
     * @return The AmbulanceDTO containing the details of the assigned ambulance, or an exception if not found.
     * @throws RuntimeException If no ambulance is found for the given driverId.
     */
    public AmbulanceDTO findAmbulanceByDriverId(UUID driverId) {
        Optional<AmbulanceEntity> ambulance = ambulanceRepository.findByDriverId(driverId);

        return ambulance
                .map(AmbulanceMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("No ambulance found for the given driver ID: " + driverId));
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

        Optional<AmbulanceEntity> closestAmbulance = availableAmbulances.stream()
                .min(Comparator.comparingDouble(a ->
                        LocationUtils.calculateDistance(userLatitude, userLongitude, a.getLatitude(), a.getLongitude())));

        return closestAmbulance
                .map(AmbulanceMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Could not find a suitable ambulance"));
    }

    /**
     * Dispatches an ambulance to handle a request.
     * Updates the request status and the ambulance availability.
     * Calculates and sets the estimated time of arrival (ETA) based on geographic location.
     *
     * @param request The RequestEntity containing request details.
     * @return The updated RequestDTO with arrival time.
     * @throws IllegalArgumentException If the closest ambulance is not found.
     */
    @Transactional
    public RequestDTO dispatchAmbulance(RequestEntity request) {
        // Find the closest available ambulance
        AmbulanceDTO ambulanceDTO = findClosestAmbulance(request.getLatitude(), request.getLongitude());
        AmbulanceEntity ambulance = ambulanceRepository.findById(ambulanceDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Ambulance not found"));

        // Assign the ambulance to the request and update request status
        request.setAmbulance(ambulance);
        request.setRequestStatus(RequestStatus.DISPATCHED);
        request.setDispatchTime(LocalDateTime.now());

        // Update ambulance availability to false
        ambulance.setAvailabilityStatus(false);
        ambulanceRepository.save(ambulance);

        // Calculate estimated time of arrival (ETA)
        double userLatitude = request.getLatitude();
        double userLongitude = request.getLongitude();
        double ambulanceLatitude = ambulance.getLatitude();
        double ambulanceLongitude = ambulance.getLongitude();

        long etaInMinutes = GeoUtils.calculateETA(ambulanceLatitude, ambulanceLongitude, userLatitude, userLongitude);

        // Save the estimated arrival time in the request
        request.setArrivalTime(LocalDateTime.now().plusMinutes(etaInMinutes));
        request.setDescription(request.getDescription().split(",")[0]);
        requestRepository.save(request);

        // Convert the updated request to DTO and include the arrival time in the response
        RequestDTO responseDTO = RequestMapper.toDTO(request);
        responseDTO.setArrivalTime(request.getArrivalTime());
        return responseDTO;
    }
}
