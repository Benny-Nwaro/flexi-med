package com.example.flexiMed.mapper;

import com.example.flexiMed.dto.PatientRecordsDTO;
import com.example.flexiMed.model.PatientRecordsEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between {@link PatientRecordsEntity} and {@link PatientRecordsDTO} objects.
 * This class provides static methods to map data between the entity and DTO representations.
 */
@Component
public class PatientRecordsMapper {

    /**
     * Converts a PatientRecordsEntity object to a PatientRecordsDTO object.
     *
     * @param patient The PatientRecordsEntity object to convert.
     * @return The corresponding PatientRecordsDTO object, or null if the input is null.
     */
    public static PatientRecordsDTO toDTO(PatientRecordsEntity patient) {
        if (patient == null) {
            return null;
        }
        return new PatientRecordsDTO(
                patient.getId(),
                patient.getRequestId(),
                patient.getPatientId(),
                patient.getContact(),
                patient.getMedicalNotes()
        );
    }

    /**
     * Converts a PatientRecordsDTO object to a PatientRecordsEntity object.
     *
     * @param dto The PatientRecordsDTO object to convert.
     * @return The corresponding PatientRecordsEntity object, or null if the input is null.
     */
    public static PatientRecordsEntity toEntity(PatientRecordsDTO dto) {
        if (dto == null) {
            return null;
        }
        PatientRecordsEntity patient = new PatientRecordsEntity();
        patient.setId(dto.getId());
        patient.setRequestId(dto.getRequestId());
        patient.setPatientId(dto.getPatientId());
        patient.setContact(dto.getContact());
        patient.setMedicalNotes(dto.getMedicalNotes());
        return patient;
    }
}