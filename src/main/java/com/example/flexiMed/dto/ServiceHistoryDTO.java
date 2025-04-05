package com.example.flexiMed.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for representing a service history record in the system.
 * It includes details about the service event associated with a request.
 */
public class ServiceHistoryDTO {

    /**
     * The unique identifier for the service history record.
     */
    private UUID id;

    /**
     * The unique identifier for the associated request.
     * This is used to link the service history record to a specific request.
     */
    @NotNull(message = "Request ID cannot be null")
    private UUID requestId;

    /**
     * The type of event that occurred in the service history.
     * For example, it might be "AMBULANCE_DISPATCHED", "REQUEST_COMPLETED", etc.
     */
    @NotNull(message = "Event type cannot be null")
    private String eventType;

    /**
     * The date and time when the event occurred.
     * The format is "yyyy-MM-dd HH:mm:ss".
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventTime;

    /**
     * Additional details about the event.
     * This field is optional but must not exceed 500 characters.
     */
    @Size(max = 500, message = "Details must not exceed 500 characters")
    private String details;

    /**
     * Default constructor for ServiceHistoryDTO.
     * This is required by frameworks such as Jackson to convert JSON to objects.
     */
    public ServiceHistoryDTO() {}

    /**
     * Parameterized constructor for ServiceHistoryDTO.
     * This constructor initializes all fields of the ServiceHistoryDTO.
     *
     * @param id        The unique identifier for the service history record.
     * @param requestId The unique identifier for the associated request.
     * @param eventType The type of event that occurred.
     * @param eventTime The date and time when the event occurred.
     * @param details   Additional details about the event (nullable).
     */
    public ServiceHistoryDTO(UUID id, UUID requestId, String eventType,
                             LocalDateTime eventTime, String details) {
        this.id = id;
        this.requestId = requestId;
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.details = details;
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the service history record.
     *
     * @return The unique ID of the service history record.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the service history record.
     *
     * @param id The unique ID of the service history record.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the unique identifier for the associated request.
     *
     * @return The unique ID of the associated request.
     */
    public UUID getRequestId() {
        return requestId;
    }

    /**
     * Sets the unique identifier for the associated request.
     *
     * @param requestId The unique ID of the associated request.
     */
    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    /**
     * Gets the type of event that occurred in the service history.
     *
     * @return The type of event.
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Sets the type of event that occurred in the service history.
     *
     * @param eventType The type of event.
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * Gets the date and time when the event occurred.
     *
     * @return The date and time of the event.
     */
    public LocalDateTime getEventTime() {
        return eventTime;
    }

    /**
     * Sets the date and time when the event occurred.
     *
     * @param eventTime The date and time of the event.
     */
    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    /**
     * Gets additional details about the event.
     *
     * @return The details of the event.
     */
    public String getDetails() {
        return details;
    }

    /**
     * Sets additional details about the event.
     * The details must not exceed 500 characters.
     *
     * @param details The additional details about the event.
     */
    public void setDetails(String details) {
        this.details = details;
    }
}
