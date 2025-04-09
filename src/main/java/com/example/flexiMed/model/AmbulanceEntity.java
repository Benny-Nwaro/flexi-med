package com.example.flexiMed.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing an ambulance in the system.
 * This class includes data such as the ambulance's plate number, driver details, location,
 * availability status, driver reference, and timestamps.
 */
@Entity
@Table(name = "ambulances")
public class AmbulanceEntity {

    /**
     * Primary key: Unique identifier for the ambulance.
     */
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Unique plate number of the ambulance.
     * Cannot be blank and must not exceed 15 characters.
     */
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Plate number cannot be empty")
    @Size(max = 15, message = "Plate number must not exceed 15 characters")
    private String plateNumber;

    /**
     * Latitude of the ambulance's location.
     * Must be within the range -90 to 90 and not null.
     */
    @Column(nullable = false)
    @NotNull(message = "Latitude cannot be null")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private Double latitude;

    /**
     * Longitude of the ambulance's location.
     * Must be within the range -180 to 180 and not null.
     */
    @Column(nullable = false)
    @NotNull(message = "Longitude cannot be null")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private Double longitude;

    /**
     * Indicates whether the ambulance is currently available.
     */
    private boolean availabilityStatus;

    /**
     * Optional reference to a driver's unique identifier.
     * Can be null, allowing an ambulance to be unassigned initially.
     */
    @Column(name = "driver_id", nullable = true)
    private UUID driverId;

    /**
     * Full name of the ambulance driver.
     * Cannot be blank and must not exceed 100 characters.
     */
    @NotBlank(message = "Driver name cannot be empty")
    @Size(max = 100, message = "Driver name must not exceed 100 characters")
    private String driverName;

    /**
     * Contact number of the ambulance driver.
     * Must match a valid phone number pattern and cannot be blank.
     */
    @NotBlank(message = "Driver contact cannot be empty")
    @Pattern(regexp = "^[0-9+\\-() ]{7,15}$", message = "Invalid contact number format")
    private String driverContact;

    /**
     * Timestamp for the last time this entity was updated.
     * Must be in the past or present.
     */
    @PastOrPresent(message = "Last updated time cannot be in the future")
    private LocalDateTime lastUpdatedAt;

    /**
     * Version field for optimistic locking.
     * Automatically managed by JPA to prevent concurrent update conflicts.
     */
    @Version
    private Long version;

    /**
     * Default constructor required by JPA.
     */
    public AmbulanceEntity() {}

    /**
     * Full constructor for manually instantiating the entity.
     */
    public AmbulanceEntity(UUID id, String plateNumber, Double latitude, Double longitude,
                           boolean availabilityStatus, UUID driverId, String driverName,
                           String driverContact, LocalDateTime lastUpdatedAt) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.availabilityStatus = availabilityStatus;
        this.driverId = driverId;
        this.driverName = driverName;
        this.driverContact = driverContact;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    /**
     * Automatically sets or updates the last updated timestamp.
     * Invoked on both entity persist and update events.
     */
    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdatedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(boolean availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public UUID getDriverId() {
        return driverId;
    }

    public void setDriverId(UUID driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
