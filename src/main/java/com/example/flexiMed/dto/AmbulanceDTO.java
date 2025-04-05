package com.example.flexiMed.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for representing ambulance information.
 * This class contains details about the ambulance, such as its plate number, location,
 * driver information, and availability status.
 */
public class AmbulanceDTO {

    /**
     * Unique identifier for the ambulance.
     */
    private UUID id;

    /**
     * The plate number of the ambulance.
     * This field cannot be empty and should not exceed 15 characters.
     */
    @NotBlank(message = "Plate number cannot be empty")
    @Size(max = 15, message = "Plate number must not exceed 15 characters")
    private String plateNumber;

    /**
     * The latitude of the ambulance's current location.
     * Latitude must be between -90 and 90 degrees.
     */
    @NotNull(message = "Latitude cannot be null")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private Double latitude;

    /**
     * The longitude of the ambulance's current location.
     * Longitude must be between -180 and 180 degrees.
     */
    @NotNull(message = "Longitude cannot be null")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private Double longitude;

    /**
     * Availability status of the ambulance.
     * A boolean indicating whether the ambulance is available or not.
     */
    private boolean availabilityStatus;

    /**
     * The name of the ambulance driver.
     * This field cannot be empty and should not exceed 100 characters.
     */
    @NotBlank(message = "Driver name cannot be empty")
    @Size(max = 100, message = "Driver name must not exceed 100 characters")
    private String driverName;

    /**
     * The contact number of the ambulance driver.
     * This field must be a valid contact number with a pattern of 7 to 15 characters.
     */
    @NotBlank(message = "Driver contact cannot be empty")
    @Pattern(regexp = "^[0-9+\\-() ]{7,15}$", message = "Invalid contact number format")
    private String driverContact;

    /**
     * The last updated timestamp of the ambulance information.
     * This is used to track when the information was last modified.
     */
    private LocalDateTime lastUpdatedAt;

    // Default Constructor
    public AmbulanceDTO() {
    }

    // Constructor with all fields
    public AmbulanceDTO(UUID id, String plateNumber, Double latitude, Double longitude,
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
}
