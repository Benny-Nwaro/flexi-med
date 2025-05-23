package com.example.flexiMed.service;

import com.example.flexiMed.dto.PatientRecordsDTO;
import com.example.flexiMed.dto.RequestDTO;
import com.example.flexiMed.dto.ServiceHistoryDTO;
import com.example.flexiMed.enums.RequestStatus;
import com.example.flexiMed.mapper.RequestMapper;
import com.example.flexiMed.model.AmbulanceEntity;
import com.example.flexiMed.model.RequestEntity;
import com.example.flexiMed.model.UserEntity;
import com.example.flexiMed.notifications.email.EmailService;
import com.example.flexiMed.repository.AmbulanceRepository;
import com.example.flexiMed.repository.RequestRepository;
import com.example.flexiMed.repository.UserRepository;
import com.example.flexiMed.utils.GeoUtils;
import com.example.flexiMed.utils.TimeUtils;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for handling ambulance requests.
 * This class provides functionalities such as creating a request,
 * retrieving all requests, retrieving user-specific requests,
 * and managing service history.
 */
@Service
public class RequestService {
    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);


    private final RequestRepository requestRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final AmbulanceService ambulanceService;
    private final UserRepository userRepository;
    private final PatientRecordsService patientRecordsService;
    private final ServiceHistoryService serviceHistoryService;
    private final NotificationService notificationService;
    private final EmailService emailService;

    /**
     * Constructor to initialize dependencies.
     *
     * @param requestRepository        Repository for accessing request data.
     * @param ambulanceRepository      Repository for accessing ambulance data.
     * @param userRepository           Repository for accessing user data.
     * @param patientRecordsService    Service for managing patient records.
     * @param ambulanceService         Service for managing ambulance operations.
     * @param serviceHistoryService    Service for managing service history logs.
     * @param notificationService      Service for sending notifications.
     */
    public RequestService(RequestRepository requestRepository, AmbulanceRepository ambulanceRepository,
                          UserRepository userRepository,
                          PatientRecordsService patientRecordsService,
                          AmbulanceService ambulanceService,
                          ServiceHistoryService serviceHistoryService,
                          NotificationService notificationService,
                          EmailService emailService) {
        this.requestRepository = requestRepository;
        this.ambulanceRepository = ambulanceRepository;
        this.ambulanceService = ambulanceService;
        this.userRepository = userRepository;
        this.patientRecordsService = patientRecordsService;
        this.serviceHistoryService = serviceHistoryService;
        this.notificationService = notificationService;
        this.emailService = emailService;
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
        String userContact = (user.getPhoneNumber() != null) ? user.getPhoneNumber() : "08080000000";

        // Create and save a patient record associated with the request.
        PatientRecordsDTO patientRecord = new PatientRecordsDTO(
                null,
                savedRequest.getId(),
                user.getUserId(),
                userContact,
                savedRequest.getDescription().split(",")[1]
        );
        patientRecordsService.addPatientRecord(patientRecord);

        // Calculate the estimated time of arrival (ETA).
        long etaInMinutes = GeoUtils.calculateETA(ambulance.getLatitude(), ambulance.getLongitude(),
                request.getLatitude(), request.getLongitude());

        // Send real-time notifications to the user and the ambulance dispatcher.
        System.out.println("Sending to topic: /topic/ambulance/" + user.getUserId());
        System.out.println("Payload: " );

        notificationService.sendUserNotifications("Ambulance has been dispatched to your location", user,
                ambulance, TimeUtils.formatTime(etaInMinutes));
        // Send email notification to the requesting user
        sendRequestCreatedEmail(user, RequestMapper.toDTO(savedRequest), TimeUtils.formatTime(etaInMinutes));


        // Record the service history of the request.
        recordServiceHistory(savedRequest);

        // Dispatch the ambulance and return the updated request DTO.
        return ambulanceService.dispatchAmbulance(savedRequest);
    }


    /**
     * Sends an email notification to the user confirming the creation of their ambulance request.
     * The email includes details such as the request ID, location, estimated time of arrival (ETA),
     * and the assigned ambulance ID. It utilizes Thymeleaf for email templating.
     *
     * @param user      The {@link UserEntity} object of the user who created the request.
     * This is used to personalize the email with the user's name and retrieve their email address.
     * @param requestDTO The {@link RequestDTO} object containing details of the created request,
     * such as the request ID, latitude, longitude, and the assigned ambulance ID.
     * @param eta       A {@link String} representing the estimated time of arrival of the ambulance.
     */
    private void sendRequestCreatedEmail(UserEntity user, RequestDTO requestDTO, String eta) {
        try {
            // Create a Thymeleaf context to hold the data for the email template.
            Context context = new Context();

            // Set variables that will be accessible within the 'ambulance-dispatch' email template.
            context.setVariable("name", user.getName()); // User's name for personalization.
            context.setVariable("requestId", requestDTO.getId()); // Unique identifier of the created request.
            context.setVariable("location", String.format("%.6f, %.6f", requestDTO.getLatitude(), requestDTO.getLongitude())); // User's location as latitude and longitude.
            context.setVariable("eta", eta); // Estimated time of arrival of the ambulance.
            context.setVariable("ambulanceId", requestDTO.getAmbulanceId()); // Identifier of the ambulance assigned to the request.

            // Use the emailService to send the email.
            // - user.getEmail(): Recipient's email address.
            // - "Ambulance Request Created": Subject line of the email.
            // - "ambulance-dispatch": Name of the Thymeleaf template to be used for the email body.
            // - context: The data to be rendered within the template.
            emailService.sendEmail(user.getEmail(), "Ambulance Request Created",
                    "ambulance-dispatch", context);

            // Log a successful email notification.
            logger.info("Email notification sent to user: {} ({}) for Request ID: {}", user.getName(), user.getEmail(), requestDTO.getId());

        } catch (org.springframework.mail.MailSendException e) {
            // Catch specific exception for email sending failures.
            logger.error("MailSendException: Could not send email to user: {} ({}) - {}", user.getName(), user.getEmail(), e.getMessage());
        } catch (MessagingException e) {
            // Catch specific exception related to messaging (email) failures.
            logger.error("MessagingException: Failed to send email to user: {} ({}) - {}", user.getName(), user.getEmail(), e.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected exceptions that might occur during the email sending process.
            logger.error("Unexpected error occurred while sending email to {} ({}): {}", user.getName(), user.getEmail(), e.getMessage(), e);
            logger.debug("Full exception stack trace:", e); // Log the full stack trace for detailed debugging.
        }
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
     * Retrieves all ambulance requests with a specific request status.
     *
     * @param requestStatus The status of the requests to filter by.
     * @return A list of RequestDTOs matching the given status.
     * @throws EntityNotFoundException If no matching requests are found.
     */
    public List<RequestDTO> getRequestsByStatus(RequestStatus requestStatus) {
        List<RequestEntity> requests = requestRepository.findByRequestStatus(requestStatus);
        if (requests.isEmpty()) {
            throw new EntityNotFoundException("No requests found with status " + requestStatus);
        }
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
                request.getDescription().split(",")[0]);
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
     * Retrieves all ambulance requests for a specific ambulance ID and with a specific request status.
     *
     * @param ambulanceId   The UUID of the ambulance to search for.
     * @param requestStatus The {@link RequestStatus} to filter by.
     * @return A list of RequestDTOs associated with the given ambulance ID and having the specified request status.
     */
    public List<RequestDTO> getRequestsByAmbulanceIdAndStatus(UUID ambulanceId, RequestStatus requestStatus) {
        List<RequestEntity> requests = requestRepository.findByAmbulanceIdAndRequestStatus(ambulanceId, requestStatus);
        return requests.stream().map(RequestMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Updates a request's status to COMPLETED and sets the associated ambulance's availability to true.
     *
     * @param requestId The UUID of the request to update.
     * @return The updated RequestDTO.
     * @throws EntityNotFoundException If the request or the associated ambulance is not found.
     */
    @Transactional
    public RequestDTO completeRequest(UUID requestId) {
        RequestEntity requestEntity = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found with ID: " + requestId));

        requestEntity.setRequestStatus(RequestStatus.COMPLETED);
        RequestEntity updatedRequest = requestRepository.save(requestEntity);

        UUID ambulanceId = requestEntity.getAmbulance().getId();
        AmbulanceEntity ambulanceEntity = ambulanceRepository.findById(ambulanceId)
                .orElseThrow(() -> new EntityNotFoundException("Ambulance not found with ID: " + ambulanceId));

        ambulanceEntity.setAvailabilityStatus(true);
        ambulanceRepository.save(ambulanceEntity);

        recordServiceHistory(updatedRequest); // Optionally record the completion

        return RequestMapper.toDTO(updatedRequest);
    }

}