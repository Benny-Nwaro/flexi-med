package com.example.flexiMed.dto;

import com.example.flexiMed.enums.RequestStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for representing a request for an ambulance service.
 * This class encapsulates all the necessary information about the request such as
 * the user making the request, ambulance details, status, and timestamps for
 * the various stages of the request.
 */
public class RequestDTO {

    /**
     * The unique identifier for the request.
     */
    private UUID id;

    /**
     * The unique identifier for the user who made the request.
     */
    private UUID userId;

    /**
     * The unique identifier for the ambulance assigned to the request.
     */
    private UUID ambulanceId;

    /**
     * The current status of the request.
     * This is an enum representing various stages like "PENDING", "DISPATCHED", "COMPLETED", etc.
     */
    private RequestStatus requestStatus;

    /**
     * The timestamp indicating when the request was created.
     */
    private LocalDateTime requestTime;

    /**
     * The timestamp indicating when the ambulance was dispatched.
     * It cannot be before the request time.
     */
    private LocalDateTime dispatchTime;

    /**
     * The timestamp indicating when the ambulance arrived at the destination.
     * It cannot be before the request time.
     */
    private LocalDateTime arrivalTime;

    /**
     * The latitude of the request location.
     */
    private double latitude;

    /**
     * The longitude of the request location.
     */
    private double longitude;

    /**
     * A description of the request or any additional details.
     * This can provide more context on the nature of the request.
     */
    private String description;

    /**
     * Default constructor for RequestDTO.
     * This constructor is needed for frameworks like Jackson to map request data.
     */
    public RequestDTO() {}

    /**
     * Parameterized constructor for RequestDTO.
     * Initializes all fields of the request object with specified values.
     *
     * @param id            The unique identifier for the request.
     * @param userId        The unique identifier for the user who made the request.
     * @param ambulanceId   The unique identifier for the assigned ambulance.
     * @param requestStatus The current status of the request (e.g., "PENDING", "DISPATCHED").
     * @param requestTime   The timestamp when the request was created.
     * @param dispatchTime  The timestamp when the ambulance was dispatched.
     * @param arrivalTime   The timestamp when the ambulance arrived at the destination.
     * @param latitude      The latitude of the request location.
     * @param longitude     The longitude of the request location.
     * @param description   A description or additional details about the request.
     */
    public RequestDTO(UUID id, UUID userId, UUID ambulanceId,
                      RequestStatus requestStatus, LocalDateTime requestTime,
                      LocalDateTime dispatchTime, LocalDateTime arrivalTime,
                      double latitude, double longitude, String description) {
        this.id = id;
        this.userId = validateUUID(userId, "User ID");
        this.ambulanceId = ambulanceId;
        this.requestStatus = validateEnum(requestStatus, "Request Status");
        this.requestTime = validateNotNull(requestTime, "Request Time");
        this.dispatchTime = validateFutureOrNull(dispatchTime, requestTime, "Dispatch Time");
        this.arrivalTime = validateFutureOrNull(arrivalTime, requestTime, "Arrival Time");
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = validateString(description, "Description");
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the request.
     *
     * @return The unique ID of the request.
     */
    public UUID getId() { return id; }

    /**
     * Sets the unique identifier for the request.
     *
     * @param id The unique ID of the request.
     */
    public void setId(UUID id) { this.id = validateUUID(id, "Request ID"); }

    /**
     * Gets the unique identifier for the user who made the request.
     *
     * @return The unique ID of the user.
     */
    public UUID getUserId() { return userId; }

    /**
     * Sets the unique identifier for the user who made the request.
     *
     * @param userId The unique ID of the user.
     */
    public void setUserId(UUID userId) { this.userId = validateUUID(userId, "User ID"); }

    /**
     * Gets the unique identifier for the ambulance assigned to the request.
     *
     * @return The unique ID of the ambulance.
     */
    public UUID getAmbulanceId() { return ambulanceId; }

    /**
     * Sets the unique identifier for the ambulance assigned to the request.
     *
     * @param ambulanceId The unique ID of the ambulance.
     */
    public void setAmbulanceId(UUID ambulanceId) { this.ambulanceId = ambulanceId; }

    /**
     * Gets the current status of the request.
     *
     * @return The status of the request.
     */
    public RequestStatus getRequestStatus() { return requestStatus; }

    /**
     * Sets the current status of the request.
     *
     * @param requestStatus The status of the request (e.g., "PENDING", "DISPATCHED").
     */
    public void setRequestStatus(RequestStatus requestStatus) { this.requestStatus = validateEnum(requestStatus, "Request Status"); }

    /**
     * Gets the timestamp when the request was created.
     *
     * @return The timestamp of the request creation.
     */
    public LocalDateTime getRequestTime() { return requestTime; }

    /**
     * Sets the timestamp when the request was created.
     *
     * @param requestTime The timestamp of the request creation.
     */
    public void setRequestTime(LocalDateTime requestTime) { this.requestTime = validateNotNull(requestTime, "Request Time"); }

    /**
     * Gets the timestamp when the ambulance was dispatched.
     *
     * @return The dispatch timestamp.
     */
    public LocalDateTime getDispatchTime() { return dispatchTime; }

    /**
     * Sets the timestamp when the ambulance was dispatched.
     *
     * @param dispatchTime The dispatch timestamp.
     */
    public void setDispatchTime(LocalDateTime dispatchTime) { this.dispatchTime = validateFutureOrNull(dispatchTime, requestTime, "Dispatch Time"); }

    /**
     * Gets the timestamp when the ambulance arrived at the destination.
     *
     * @return The arrival timestamp.
     */
    public LocalDateTime getArrivalTime() { return arrivalTime; }

    /**
     * Sets the timestamp when the ambulance arrived at the destination.
     *
     * @param arrivalTime The arrival timestamp.
     */
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = validateFutureOrNull(arrivalTime, requestTime, "Arrival Time"); }

    /**
     * Gets the latitude of the request location.
     *
     * @return The latitude of the request location.
     */
    public double getLatitude() { return latitude; }

    /**
     * Sets the latitude of the request location.
     *
     * @param latitude The latitude of the request location.
     */
    public void setLatitude(double latitude) { this.latitude = latitude; }

    /**
     * Gets the longitude of the request location.
     *
     * @return The longitude of the request location.
     */
    public double getLongitude() { return longitude; }

    /**
     * Sets the longitude of the request location.
     *
     * @param longitude The longitude of the request location.
     */
    public void setLongitude(double longitude) { this.longitude = longitude; }

    /**
     * Gets the description or additional details about the request.
     *
     * @return The description of the request.
     */
    public String getDescription() { return description; }

    /**
     * Sets the description or additional details about the request.
     *
     * @param description The description of the request.
     */
    public void setDescription(String description) { this.description = validateString(description, "Description"); }

    // Validation Methods

    private UUID validateUUID(UUID value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null.");
        }
        return value;
    }

    private String validateString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }

    private LocalDateTime validateNotNull(LocalDateTime value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null.");
        }
        return value;
    }

    private LocalDateTime validateFutureOrNull(LocalDateTime value, LocalDateTime reference, String fieldName) {
        if (value != null && reference != null && value.isBefore(reference)) {
            throw new IllegalArgumentException(fieldName + " cannot be before request time.");
        }
        return value;
    }

    private RequestStatus validateEnum(RequestStatus value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null.");
        }
        return value;
    }
}
