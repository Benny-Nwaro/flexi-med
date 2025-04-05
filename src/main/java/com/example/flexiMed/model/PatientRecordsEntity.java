package com.example.flexiMed.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Entity representing a patient's medical record in the system.
 * This entity links to a specific request and contains the patient's contact information and medical notes.
 */
@Entity
@Table(name = "patient_records")
public class PatientRecordsEntity {

    /**
     * The unique identifier for the patient record.
     * This field is the primary key for the patient's record entity.
     */
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * The ID of the request that is linked to this patient record.
     * This field stores only the UUID of the request and cannot be null.
     */
    @Column(name = "request_id", nullable = false) // Store only the UUID
    @NotNull(message = "Request ID cannot be null")
    private UUID requestId;

    /**
     * The ID of the patient associated with this record.
     * This field stores only the UUID of the patient and links to the Users table.
     * It cannot be null.
     */
    @Column(name = "patient_id", nullable = false) // Store only the UUID
    @NotNull(message = "patient_id cannot be null") // Links to Users table
    private UUID patientId;

    /**
     * The contact number of the patient.
     * This field cannot be empty and must match a valid phone number format.
     * The valid format includes numbers, spaces, parentheses, hyphens, and plus sign, with a length between 7 and 15 characters.
     */
    @NotBlank(message = "Contact cannot be empty")
    @Pattern(regexp = "^[0-9+\\-() ]{7,15}$", message = "Invalid contact number format")
    private String contact;

    /**
     * Medical notes related to the patient's condition.
     * This field is optional but if provided, it cannot exceed 1000 characters.
     */
    @Size(max = 1000, message = "Medical notes must not exceed 1000 characters")
    private String medicalNotes;

    /**
     * Default constructor for the PatientRecordsEntity.
     * Required by JPA for entity management.
     */
    public PatientRecordsEntity() {}

    /**
     * Constructor to initialize a PatientRecordsEntity with all fields.
     *
     * @param id            The unique identifier for the patient record.
     * @param requestId     The ID of the request linked to the patient record.
     * @param patientId     The ID of the patient associated with the record.
     * @param contact       The contact number of the patient.
     * @param medicalNotes  The medical notes related to the patient's condition.
     */
    public PatientRecordsEntity(UUID id, UUID requestId, UUID patientId, String contact, String medicalNotes) {
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
     * Gets the ID of the request linked to this patient record.
     *
     * @return The UUID of the request linked to the record.
     */
    public UUID getRequestId() {
        return requestId;
    }

    /**
     * Sets the ID of the request linked to this patient record.
     *
     * @param requestId The UUID of the request linked to the record.
     */
    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    /**
     * Gets the ID of the patient associated with this record.
     *
     * @return The UUID of the patient linked to the record.
     */
    public UUID getPatientId() {
        return patientId;
    }

    /**
     * Sets the ID of the patient associated with this record.
     *
     * @param patientId The UUID of the patient linked to the record.
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
     * Gets the medical notes related to the patient's condition.
     *
     * @return The medical notes of the patient.
     */
    public String getMedicalNotes() {
        return medicalNotes;
    }

    /**
     * Sets the medical notes related to the patient's condition.
     *
     * @param medicalNotes The medical notes of the patient.
     */
    public void setMedicalNotes(String medicalNotes) {
        this.medicalNotes = medicalNotes;
    }
}
