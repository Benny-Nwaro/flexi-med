package com.example.flexiMed.model;

import com.example.flexiMed.enums.EventType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing the service history for a request in the system.
 * This entity tracks the events associated with a request, such as updates or status changes.
 */
@Entity
@Table(name = "service_history")
public class ServiceHistoryEntity {

    /**
     * The unique identifier for the service history record.
     * This field is the primary key for the service history entity.
     */
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * The request associated with the service history.
     * This field is a foreign key to the {@link RequestEntity} and cannot be null.
     */
    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    @NotNull(message = "Request cannot be null")
    private RequestEntity request;

    /**
     * The type of event that occurred.
     * This field uses an enumeration of {@link EventType} to define the nature of the event.
     * It cannot be null.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Event type cannot be null")
    private EventType eventType;

    /**
     * The time when the event occurred.
     * This field is automatically populated with the current timestamp when the entity is created.
     */
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime eventTime;

    /**
     * Additional details about the event.
     * This field allows up to 500 characters of text to describe the event in more detail.
     */
    @Column(length = 500)
    @Size(max = 500, message = "Details must not exceed 500 characters")
    private String details;

    /**
     * Default constructor for the ServiceHistoryEntity.
     */
    public ServiceHistoryEntity() {}

    /**
     * Constructor to initialize a ServiceHistoryEntity with all fields.
     *
     * @param id        The unique identifier for the service history.
     * @param request   The request associated with the service history.
     * @param eventType The type of event that occurred.
     * @param eventTime The time when the event occurred.
     * @param details   Additional details about the event.
     */
    public ServiceHistoryEntity(UUID id, RequestEntity request, EventType eventType, LocalDateTime eventTime, String details) {
        this.id = id;
        this.request = request;
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.details = details;
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the service history record.
     *
     * @return The unique ID of the service history.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the service history record.
     *
     * @param id The unique ID of the service history.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the request associated with the service history record.
     *
     * @return The associated {@link RequestEntity}.
     */
    public RequestEntity getRequest() {
        return request;
    }

    /**
     * Sets the request associated with the service history record.
     *
     * @param request The associated {@link RequestEntity}.
     */
    public void setRequest(RequestEntity request) {
        this.request = request;
    }

    /**
     * Gets the type of event that occurred.
     *
     * @return The {@link EventType} representing the event.
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Sets the type of event that occurred.
     *
     * @param eventType The {@link EventType} representing the event.
     */
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    /**
     * Gets the time when the event occurred.
     *
     * @return The timestamp of when the event occurred.
     */
    public LocalDateTime getEventTime() {
        return eventTime;
    }

    /**
     * Sets the time when the event occurred.
     *
     * @param eventTime The timestamp of when the event occurred.
     */
    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    /**
     * Gets additional details about the event.
     *
     * @return The event details.
     */
    public String getDetails() {
        return details;
    }

    /**
     * Sets additional details about the event.
     *
     * @param details The event details.
     */
    public void setDetails(String details) {
        this.details = details;
    }
}
