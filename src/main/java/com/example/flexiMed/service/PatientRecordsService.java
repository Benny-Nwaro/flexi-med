package com.example.flexiMed.service;

import com.example.flexiMed.dto.PatientRecordsDTO;
import com.example.flexiMed.exceptions.ErrorResponse.PatientRecordNotFoundException;
import com.example.flexiMed.exceptions.ErrorResponse.PatientRecordSaveFailedException;
import com.example.flexiMed.mapper.PatientRecordsMapper;
import com.example.flexiMed.model.PatientRecordsEntity;
import com.example.flexiMed.repository.PatientRecordsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing patient records.
 * Provides functionalities to add, retrieve, update, and delete patient records.
 */
@Service
public class PatientRecordsService {
    private final PatientRecordsRepository patientRecordsRepository;

    /**
     * Constructs a {@code PatientRecordsService} with the specified repository.
     *
     * @param patientRecordsRepository the repository for managing patient records
     */
    public PatientRecordsService(PatientRecordsRepository patientRecordsRepository) {
        this.patientRecordsRepository = patientRecordsRepository;
    }

    /**
     * Adds a new patient record or updates an existing one if a record already exists for the patient.
     * Assumes at most one primary patient record exists per patient ID.
     *
     * @param patientRecordDTO the DTO containing patient record details
     * @return the saved or updated patient record as a DTO
     * @throws PatientRecordSaveFailedException if saving the record fails
     */
    @Transactional
    public PatientRecordsDTO addPatientRecord(PatientRecordsDTO patientRecordDTO) {
        UUID patientId = patientRecordDTO.getPatientId();
        Optional<PatientRecordsEntity> existingRecordOptional = patientRecordsRepository.findByPatientId(patientId);

        if (existingRecordOptional.isPresent()) {
            // Update the existing record
            PatientRecordsEntity existingEntity = existingRecordOptional.get();
            PatientRecordsEntity updatedEntity = PatientRecordsMapper.toEntity(patientRecordDTO);
            updatedEntity.setId(existingEntity.getId()); // Ensure the ID of the existing entity is preserved
            try {
                PatientRecordsEntity savedEntity = patientRecordsRepository.save(updatedEntity);
                return PatientRecordsMapper.toDTO(savedEntity);
            } catch (Exception e) {
                throw new PatientRecordSaveFailedException("Failed to update patient record for patientId: " + patientId, e);
            }
        } else {
            // Create a new record
            PatientRecordsEntity entity = PatientRecordsMapper.toEntity(patientRecordDTO);
            entity.setId(null); // Ensure a new ID is generated
            try {
                PatientRecordsEntity savedEntity = patientRecordsRepository.save(entity);
                return PatientRecordsMapper.toDTO(savedEntity);
            } catch (Exception e) {
                throw new PatientRecordSaveFailedException("Failed to save new patient record for patientId: " + patientId, e);
            }
        }
    }


    /**
     * Retrieves a patient record by its unique ID.
     *
     * @param id the unique identifier of the patient record
     * @return the corresponding patient record as a DTO
     * @throws PatientRecordNotFoundException if no record is found
     */
    public PatientRecordsDTO getPatientRecordById(UUID id) {
        PatientRecordsEntity entity = patientRecordsRepository.findById(id)
                .orElseThrow(() -> new PatientRecordNotFoundException("Patient record not found"));

        return PatientRecordsMapper.toDTO(entity);
    }

    /**
     * Retrieves a single patient record associated with a specific patient ID.
     * Assumes there is at most one primary patient record per patient ID.
     *
     * @param patientId the unique identifier of the patient
     * @return the patient record DTO
     * @throws PatientRecordNotFoundException if no record is found for the patient ID
     */
    public PatientRecordsDTO getPatientRecordByPatientId(UUID patientId) {
        PatientRecordsEntity record = patientRecordsRepository.findByPatientId(patientId)
                .orElseThrow(() -> new PatientRecordNotFoundException("No patient record found for patientId: " + patientId));
        return PatientRecordsMapper.toDTO(record);
    }

    /**
     * Updates an existing patient record with new information.
     *
     * @param id             the unique identifier of the patient record
     * @param updatedRecord  the DTO containing updated patient record details
     * @return the updated patient record as a DTO
     * @throws PatientRecordNotFoundException if the record is not found
     */
    @Transactional
    public PatientRecordsDTO updatePatientRecord(UUID id, PatientRecordsDTO updatedRecord) {
        PatientRecordsEntity entity = patientRecordsRepository.findById(id)
                .orElseThrow(() -> new PatientRecordNotFoundException("Patient record not found"));

        if (updatedRecord.getPatientId() != null) entity.setPatientId(updatedRecord.getPatientId());
        if (updatedRecord.getContact() != null) entity.setContact(updatedRecord.getContact());
        if (updatedRecord.getMedicalNotes() != null) entity.setMedicalNotes(updatedRecord.getMedicalNotes());

        PatientRecordsEntity savedEntity = patientRecordsRepository.save(entity);
        return PatientRecordsMapper.toDTO(savedEntity);
    }

    /**
     * Deletes a patient record by its unique ID.
     *
     * @param id the unique identifier of the patient record
     * @throws PatientRecordNotFoundException if the record does not exist
     */
    @Transactional
    public void deletePatientRecord(UUID id) {
        if (!patientRecordsRepository.existsById(id)) {
            throw new PatientRecordNotFoundException("Patient record not found");
        }
        patientRecordsRepository.deleteById(id);
    }
}
