package com.example.flexiMed.dto;

import java.util.UUID;

/**
 * DTO class to hold data about the ambulance notification.
 */
public  class AmbulanceNotificationDTO {
    private String message;
    private String ambulancePlateNumber;
    private String driverName;
    private String driverContact;
    private String eta;
    private UUID userId;
    private UUID ambulanceId;

    // Constructors, getters, and setters

    public AmbulanceNotificationDTO() {
    }

    public AmbulanceNotificationDTO(UUID userId, UUID ambulanceId, String message, String ambulancePlateNumber, String driverName, String driverContact,  String eta) {
        this.userId = userId;
        this.ambulanceId = ambulanceId;
        this.message = message;
        this.ambulancePlateNumber = ambulancePlateNumber;
        this.driverName = driverName;
        this.driverContact = driverContact;
        this.eta = eta;
    }

    public String getMessage() {
        return message;
    }

    public String getAmbulancePlateNumber() {
        return ambulancePlateNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public String getEta() {
        return eta;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getAmbulanceId() {
        return ambulanceId;
    }

    public void setAmbulanceId(UUID ambulanceId) {
        this.ambulanceId = ambulanceId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAmbulancePlateNumber(String ambulancePlateNumber) {
        this.ambulancePlateNumber = ambulancePlateNumber;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }
}
