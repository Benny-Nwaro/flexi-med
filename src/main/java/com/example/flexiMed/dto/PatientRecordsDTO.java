package com.example.flexiMed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for representing the medical records of a patient associated
 * with a particular ambulance request. This class holds key information such as the patient's
 * contact number and medical notes related to the request.
 */
public class PatientRecordsDTO {

    /**
     * The unique identifier for the patient record.
     */
    private UUID id;

    /**
     * The unique identifier for the associated request.
     */
    @NotNull(message = "Request ID cannot be null")
    private UUID requestId;

    /**
     * The unique identifier for the patient.
     */
    @NotNull(message = "Patient ID cannot be null")
    private UUID patientId;

    /**
     * The contact number of the patient.
     * The contact number must be in a valid format (e.g., +1234567890, 123-456-7890, etc.).
     */
    @NotBlank(message = "Contact cannot be empty")
    @Pattern(regexp = "^[0-9+\\-() ]{7,15}$", message = "Invalid contact number format")
    private String contact;

    /**
     * Medical notes related to the patient's condition.
     * The notes should not exceed 1000 characters.
     */
    @Size(max = 1000, message = "Medical notes must not exceed 1000 characters")
    private String medicalNotes;

    /**
     * Default constructor for PatientRecordsDTO.
     * This constructor is required for frameworks like Jackson to map request data.
     */
    public PatientRecordsDTO() {}

    /**
     * Parameterized constructor for PatientRecordsDTO.
     * Initializes all fields of the patient record with specified values.
     *
     * @param id            The unique identifier for the patient record.
     * @param requestId     The unique identifier for the associated request.
     * @param patientId     The unique identifier for the patient.
     * @param contact       The contact number of the patient.
     * @param medicalNotes  Medical notes related to the patient.
     */
    public PatientRecordsDTO(UUID id, UUID requestId,
                             UUID patientId, String contact,
                             String medicalNotes) {
        this.id = id;
        this.requestId = requestId;
        this.patientId = patientId;
        this.contact = contact;
        this.medicalNotes = medicalNotes;
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the patient record.
     *
     * @return The unique ID of the patient record.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the patient record.
     *
     * @param id The unique ID of the patient record.
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
     * Gets the unique identifier for the patient.
     *
     * @return The unique ID of the patient.
     */
    public UUID getPatientId () {
        return patientId;
    }

    /**
     * Sets the unique identifier for the patient.
     *
     * @param patientId The unique ID of the patient.
     */
    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    /**
     * Gets the contact number of the patient.
     *
     * @return The contact number of the patient.
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets the contact number of the patient.
     *
     * @param contact The contact number of the patient.
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Gets the medical notes related to the patient.
     *
     * @return The medical notes of the patient.
     */
    public String getMedicalNotes() {
        return medicalNotes;
    }

    /**
     * Sets the medical notes related to the patient.
     *
     * @param medicalNotes The medical notes related to the patient.
     */
    public void setMedicalNotes(String medicalNotes) {
        this.medicalNotes = medicalNotes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientId, medicalNotes);
    }
}
