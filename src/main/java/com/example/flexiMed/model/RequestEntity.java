package com.example.flexiMed.model;

import com.example.flexiMed.enums.RequestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a request for ambulance services in the system.
 * This entity captures the request details, status, timestamps, and geographical data.
 */
@Entity
@Table(name = "requests")
public class RequestEntity {

    /**
     * The unique identifier for the request.
     * This field is the primary key for the request entity.
     */
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * The user who created the request.
     * This field is a foreign key to the {@link UserEntity} and cannot be null.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User cannot be null")
    private UserEntity user;

    /**
     * The ambulance assigned to the request.
     * This field is a foreign key to the {@link AmbulanceEntity}.
     */
    @ManyToOne
    @JoinColumn(name = "ambulance_id")
    private AmbulanceEntity ambulance;

    /**
     * The status of the request.
     * This field uses an enumeration of {@link RequestStatus} to define the current state of the request.
     * It cannot be null.
     */
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Request status cannot be null")
    private RequestStatus requestStatus;

    /**
     * The time when the request was made.
     * This field cannot be null and defaults to the current time when the entity is created.
     */
    @NotNull(message = "Request time cannot be null")
    private LocalDateTime requestTime;

    /**
     * The time when the ambulance was dispatched.
     * This field can be null if the dispatch time is not yet recorded.
     */
    private LocalDateTime dispatchTime;

    /**
     * The time when the ambulance arrived at the destination.
     * This field can be null if the arrival time is not yet recorded.
     */
    private LocalDateTime arrivalTime;

    /**
     * The latitude of the location where the ambulance is needed.
     */
    private double latitude;

    /**
     * The longitude of the location where the ambulance is needed.
     */
    private double longitude;

    /**
     * A description of the request or additional notes.
     */
    private String description;

    /**
     * Default constructor for the RequestEntity.
     * This sets the request time to the current time.
     */
    public RequestEntity() {
        this.requestTime = LocalDateTime.now(); // Default request time to now
    }

    /**
     * Constructor to initialize a RequestEntity with all fields.
     *
     * @param id           The unique identifier for the request.
     * @param user         The user who created the request.
     * @param ambulance    The ambulance assigned to the request.
     * @param requestStatus The status of the request.
     * @param requestTime  The time when the request was made.
     * @param dispatchTime The time when the ambulance was dispatched.
     * @param arrivalTime  The time when the ambulance arrived at the destination.
     * @param latitude     The latitude of the location where the ambulance is needed.
     * @param longitude    The longitude of the location where the ambulance is needed.
     * @param description  A description or additional notes for the request.
     */
    public RequestEntity(UUID id, UserEntity user, AmbulanceEntity ambulance, RequestStatus requestStatus,
                         LocalDateTime requestTime, LocalDateTime dispatchTime, LocalDateTime arrivalTime,
                         double latitude, double longitude, String description) {
        this.id = id;
        this.user = user;
        this.ambulance = ambulance;
        this.requestStatus = requestStatus;
        this.requestTime = (requestTime != null) ? requestTime : LocalDateTime.now();
        this.dispatchTime = dispatchTime;
        this.arrivalTime = arrivalTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the request.
     *
     * @return The unique ID of the request.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the request.
     *
     * @param id The unique ID of the request.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the user who created the request.
     *
     * @return The {@link UserEntity} associated with the request.
     */
    public UserEntity getUser() {
        return user;
    }

    /**
     * Sets the user who created the request.
     *
     * @param user The {@link UserEntity} associated with the request.
     */
    public void setUser(UserEntity user) {
        this.user = user;
    }

    /**
     * Gets the ambulance assigned to the request.
     *
     * @return The {@link AmbulanceEntity} assigned to the request.
     */
    public AmbulanceEntity getAmbulance() {
        return ambulance;
    }

    /**
     * Sets the ambulance assigned to the request.
     *
     * @param ambulance The {@link AmbulanceEntity} assigned to the request.
     */
    public void setAmbulance(AmbulanceEntity ambulance) {
        this.ambulance = ambulance;
    }

    /**
     * Gets the status of the request.
     *
     * @return The {@link RequestStatus} representing the current status of the request.
     */
    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    /**
     * Sets the status of the request.
     *
     * @param requestStatus The {@link RequestStatus} representing the new status.
     */
    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    /**
     * Gets the time when the request was made.
     *
     * @return The time the request was created.
     */
    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    /**
     * Sets the time when the request was made.
     *
     * @param requestTime The time the request was created.
     */
    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    /**
     * Gets the time when the ambulance was dispatched.
     *
     * @return The dispatch time of the ambulance.
     */
    public LocalDateTime getDispatchTime() {
        return dispatchTime;
    }

    /**
     * Sets the time when the ambulance was dispatched.
     *
     * @param dispatchTime The dispatch time of the ambulance.
     */
    public void setDispatchTime(LocalDateTime dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    /**
     * Gets the time when the ambulance arrived at the destination.
     *
     * @return The arrival time of the ambulance.
     */
    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Sets the time when the ambulance arrived at the destination.
     *
     * @param arrivalTime The arrival time of the ambulance.
     */
    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * Gets the latitude of the location where the ambulance is needed.
     *
     * @return The latitude of the location.
     */
    public long getLatitude() {
        return (long) latitude;
    }

    /**
     * Sets the latitude of the location where the ambulance is needed.
     *
     * @param latitude The latitude of the location.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude of the location where the ambulance is needed.
     *
     * @return The longitude of the location.
     */
    public long getLongitude() {
        return (long) longitude;
    }

    /**
     * Sets the longitude of the location where the ambulance is needed.
     *
     * @param longitude The longitude of the location.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the description or additional notes for the request.
     *
     * @return The description of the request.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description or additional notes for the request.
     *
     * @param description The description of the request.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
