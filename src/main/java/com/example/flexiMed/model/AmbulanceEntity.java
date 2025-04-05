package com.example.flexiMed.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing an ambulance in the system.
 * It contains details such as plate number, driver information, location (latitude and longitude),
 * availability status, and a timestamp of the last update.
 */
@Entity
@Table(name = "ambulances")
public class AmbulanceEntity {

    /**
     * The unique identifier for the ambulance.
     * This is the primary key for the ambulance entity.
     */
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * The plate number of the ambulance.
     * This field is unique and cannot be empty. It must not exceed 15 characters.
     */
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Plate number cannot be empty")
    @Size(max = 15, message = "Plate number must not exceed 15 characters")
    private String plateNumber;

    /**
     * The latitude of the ambulance's current location.
     * It must be within the valid range of -90 to 90 degrees.
     */
    @Column(nullable = false)
    @NotNull(message = "Latitude cannot be null")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private Double latitude;

    /**
     * The longitude of the ambulance's current location.
     * It must be within the valid range of -180 to 180 degrees.
     */
    @Column(nullable = false)
    @NotNull(message = "Longitude cannot be null")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private Double longitude;

    /**
     * The availability status of the ambulance.
     * If true, the ambulance is available; otherwise, it is not.
     */
    private boolean availabilityStatus;

    /**
     * The name of the ambulance driver.
     * This field cannot be empty and must not exceed 100 characters.
     */
    @NotBlank(message = "Driver name cannot be empty")
    @Size(max = 100, message = "Driver name must not exceed 100 characters")
    private String driverName;

    /**
     * The contact number of the ambulance driver.
     * This field must match a valid phone number format, allowing numbers, spaces, parentheses, hyphens, and plus sign,
     * with a length between 7 and 15 characters.
     */
    @NotBlank(message = "Driver contact cannot be empty")
    @Pattern(regexp = "^[0-9+\\-() ]{7,15}$", message = "Invalid contact number format")
    private String driverContact;

    /**
     * The timestamp of the last time the ambulance information was updated.
     * This field must not be in the future.
     */
    @PastOrPresent(message = "Last updated time cannot be in the future")
    private LocalDateTime lastUpdatedAt;

    /**
     * Optimistic locking version to handle concurrent updates.
     */
    @Version // Enables optimistic locking
    private Long version;

    /**
     * Default constructor for AmbulanceEntity.
     * Required by JPA for entity management.
     */
    public AmbulanceEntity() {
    }

    /**
     * Constructor to initialize an AmbulanceEntity with all fields.
     *
     * @param id              The unique identifier for the ambulance.
     * @param plateNumber     The plate number of the ambulance.
     * @param latitude        The latitude of the ambulance's location.
     * @param longitude       The longitude of the ambulance's location.
     * @param availabilityStatus The availability status of the ambulance.
     * @param driverName      The name of the ambulance driver.
     * @param driverContact   The contact number of the ambulance driver.
     * @param lastUpdatedAt   The timestamp of the last update.
     */
    public AmbulanceEntity(UUID id, String plateNumber, Double latitude, Double longitude,
                           boolean availabilityStatus, String driverName,
                           String driverContact, LocalDateTime lastUpdatedAt) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.availabilityStatus = availabilityStatus;
        this.driverName = driverName;
        this.driverContact = driverContact;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    /**
     * This method is called before a new ambulance entity is persisted or updated.
     * It ensures the last updated timestamp is set to the current time.
     */
    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdatedAt = LocalDateTime.now();
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the ambulance.
     *
     * @return The unique ID of the ambulance.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the ambulance.
     *
     * @param id The unique ID of the ambulance.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the plate number of the ambulance.
     *
     * @return The plate number of the ambulance.
     */
    public String getPlateNumber() {
        return plateNumber;
    }

    /**
     * Sets the plate number of the ambulance.
     *
     * @param plateNumber The plate number of the ambulance.
     */
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    /**
     * Gets the latitude of the ambulance's location.
     *
     * @return The latitude of the ambulance's location.
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the ambulance's location.
     *
     * @param latitude The latitude of the ambulance's location.
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude of the ambulance's location.
     *
     * @return The longitude of the ambulance's location.
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the ambulance's location.
     *
     * @param longitude The longitude of the ambulance's location.
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the availability status of the ambulance.
     *
     * @return True if the ambulance is available, otherwise false.
     */
    public boolean isAvailabilityStatus() {
        return availabilityStatus;
    }

    /**
     * Sets the availability status of the ambulance.
     *
     * @param availabilityStatus The availability status of the ambulance.
     */
    public void setAvailabilityStatus(boolean availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    /**
     * Gets the name of the ambulance driver.
     *
     * @return The name of the ambulance driver.
     */
    public String getDriverName() {
        return driverName;
    }

    /**
     * Sets the name of the ambulance driver.
     *
     * @param driverName The name of the ambulance driver.
     */
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    /**
     * Gets the contact number of the ambulance driver.
     *
     * @return The contact number of the ambulance driver.
     */
    public String getDriverContact() {
        return driverContact;
    }

    /**
     * Sets the contact number of the ambulance driver.
     *
     * @param driverContact The contact number of the ambulance driver.
     */
    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }

    /**
     * Gets the timestamp of the last update for the ambulance.
     *
     * @return The last updated timestamp for the ambulance.
     */
    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    /**
     * Sets the timestamp of the last update for the ambulance.
     *
     * @param lastUpdatedAt The last updated timestamp for the ambulance.
     */
    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    /**
     * Gets the version number of the entity.
     * This version number is used for optimistic locking to prevent concurrent updates from overwriting each other.
     *
     * @return The current version number of the entity.
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the version number of the entity.
     * This method is generally used internally by the persistence provider (e.g., Hibernate)
     * and should not be called directly by application code unless you are explicitly
     * managing optimistic locking.
     *
     * @param version The new version number to set.
     */
    public void setVersion(Long version) {
        this.version = version;
    }
}
