package com.example.flexiMed.service;

import com.example.flexiMed.dto.PatientRecordsDTO;
import com.example.flexiMed.dto.RequestDTO;
import com.example.flexiMed.dto.ServiceHistoryDTO;
import com.example.flexiMed.mapper.RequestMapper;
import com.example.flexiMed.mapper.UserMapper;
import com.example.flexiMed.model.AmbulanceEntity;
import com.example.flexiMed.model.RequestEntity;
import com.example.flexiMed.model.UserEntity;
import com.example.flexiMed.repository.AmbulanceRepository;
import com.example.flexiMed.repository.RequestRepository;
import com.example.flexiMed.repository.UserRepository;
import com.example.flexiMed.utils.GeoUtils;
import com.example.flexiMed.utils.TimeUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service class for handling ambulance requests.
 * This class provides functionalities such as creating a request,
 * retrieving all requests, retrieving user-specific requests,
 * and managing service history.
 */
@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final AmbulanceService ambulanceService;
    private final UserRepository userRepository;
    private final PatientRecordsService patientRecordsService;
    private final ServiceHistoryService serviceHistoryService;
    private final UserService userService;
    private final NotificationService notificationService;

    /**
     * Constructor to initialize dependencies.
     *
     * @param requestRepository        Repository for accessing request data.
     * @param ambulanceRepository      Repository for accessing ambulance data.
     * @param userRepository           Repository for accessing user data.
     * @param patientRecordsService    Service for managing patient records.
     * @param ambulanceService         Service for managing ambulance operations.
     * @param serviceHistoryService    Service for managing service history logs.
     * @param userService              Service for managing user operations.
     * @param notificationService      Service for sending notifications.
     */
    public RequestService(RequestRepository requestRepository, AmbulanceRepository ambulanceRepository,
                          UserRepository userRepository,
                          PatientRecordsService patientRecordsService,
                          AmbulanceService ambulanceService,
                          ServiceHistoryService serviceHistoryService,
                          UserService userService,
                          NotificationService notificationService) {
        this.requestRepository = requestRepository;
        this.ambulanceRepository = ambulanceRepository;
        this.ambulanceService = ambulanceService;
        this.userRepository = userRepository;
        this.patientRecordsService = patientRecordsService;
        this.serviceHistoryService = serviceHistoryService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    /**
     * Creates a new ambulance request.
     *
     * @param request The RequestDTO containing request details.
     * @return The created RequestDTO.
     * @throws RuntimeException If the user or an available ambulance is not found.
     */
    public RequestDTO createRequest(RequestDTO request) {
        // Find the user making the request.
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find the first available ambulance.
        AmbulanceEntity ambulance = ambulanceRepository.findFirstByAvailabilityStatusIsTrue()
                .orElseThrow(() -> new RuntimeException("No available ambulance at the moment"));

        // Create a RequestEntity from the DTO, associating it with the user and ambulance.
        RequestEntity requestEntity = RequestMapper.toEntity(request, user, ambulance);

        // Save the request and retrieve the saved entity.
        RequestEntity savedRequest = requestRepository.save(requestEntity);

        // Determine the user's contact information (phone number or email).
        String userContact = (user.getPhoneNumber() != null) ? user.getPhoneNumber() : user.getEmail();

        // Create and save a patient record associated with the request.
        PatientRecordsDTO patientRecord = new PatientRecordsDTO(
                null,
                savedRequest.getId(),
                user.getUserId(),
                userContact,
                savedRequest.getDescription()
        );
        patientRecordsService.addPatientRecord(patientRecord);

        // Calculate the estimated time of arrival (ETA).
        long etaInMinutes = GeoUtils.calculateETA(ambulance.getLatitude(), ambulance.getLongitude(),
                request.getLatitude(), request.getLongitude());

        // Send real-time notifications to the user and the ambulance dispatcher.
        notificationService.sendUserNotifications("Ambulance has been dispatched to your location", user,
                ambulance, TimeUtils.formatTime(etaInMinutes));
        notificationService.sendUserNotifications("Your ambulance has been dispatched to Lat: " + request.getLatitude()
                        + " and Long: " + request.getLongitude(), getDispatcher(ambulance.getDriverContact()), ambulance,
                TimeUtils.formatTime(etaInMinutes));

        // Record the service history of the request.
        recordServiceHistory(savedRequest);

        // Dispatch the ambulance and return the updated request DTO.
        return ambulanceService.dispatchAmbulance(savedRequest);
    }

    /**
     * Retrieves all ambulance requests.
     *
     * @return A list of RequestDTOs representing all requests.
     */
    public List<RequestDTO> getAllRequests() {
        List<RequestEntity> requests = requestRepository.findAll();
        return requests.stream().map(RequestMapper::toDTO).toList();
    }

    /**
     * Records the service history of a request.
     *
     * @param request The RequestEntity to record in the service history.
     */
    public void recordServiceHistory(RequestEntity request) {
        ServiceHistoryDTO service = new ServiceHistoryDTO(null, request.getId(),
                request.getRequestStatus().toString(),
                request.getRequestTime(),
                request.getDescription());
        serviceHistoryService.logEvent(service);
    }

    /**
     * Retrieves all ambulance requests made by a specific user.
     *
     * @param userId The unique identifier of the user.
     * @return A list of RequestDTOs representing the user's requests.
     * @throws EntityNotFoundException If no requests are found for the user.
     */
    public List<RequestDTO> getUserRequests(UUID userId) {
        List<RequestEntity> requests = requestRepository.findByUserUserId(userId);
        if (requests.isEmpty()) {
            throw new EntityNotFoundException("No requests found for user with ID: " + userId);
        }
        return requests.stream().map(RequestMapper::toDTO).toList();
    }

    /**
     * Retrieves the user entity of the ambulance dispatcher based on the driver's contact.
     *
     * @param driverContact The contact information of the driver.
     * @return The UserEntity of the dispatcher.
     * @throws EntityNotFoundException If the dispatcher is not found.
     */
    public UserEntity getDispatcher(String driverContact) {
        return userService.getUserByPhoneNumber(driverContact)
                .map(UserMapper::toEntity)
                .orElseThrow(() -> new EntityNotFoundException("Dispatcher not found for phone: " + driverContact));
    }
}