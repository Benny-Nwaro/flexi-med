package com.example.flexiMed.service;

import com.example.flexiMed.dto.PatientRecordsDTO;
import com.example.flexiMed.exceptions.ErrorResponse.PatientRecordNotFoundException;
import com.example.flexiMed.exceptions.ErrorResponse.PatientRecordSaveFailedException;
import com.example.flexiMed.mapper.PatientRecordsMapper;
import com.example.flexiMed.model.PatientRecordsEntity;
import com.example.flexiMed.repository.PatientRecordsRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing patient records.
 * Provides functionalities to add, retrieve, update, and delete patient records.
 */
@Service
public class PatientRecordsService {

    private static final Logger logger = LoggerFactory.getLogger(PatientRecordsService.class);
    private final PatientRecordsRepository patientRecordsRepository;

    public PatientRecordsService(PatientRecordsRepository patientRecordsRepository) {
        this.patientRecordsRepository = patientRecordsRepository;
    }

    /**
     * Adds a new patient record or updates an existing one if a record already exists for the patient.
     * Assumes at most one primary patient record exists per patient ID.
     *
     * @param patientRecordDTO the DTO containing patient record details
     * @return the saved or updated patient record as a DTO
     */
    @Transactional
    public PatientRecordsDTO addPatientRecord(PatientRecordsDTO patientRecordDTO) {
        UUID patientId = patientRecordDTO.getPatientId();
        Optional<PatientRecordsEntity> existingRecordOptional = patientRecordsRepository.findByPatientId(patientId);

        try {
            if (existingRecordOptional.isPresent()) {
                // Update existing record
                PatientRecordsEntity existingEntity = existingRecordOptional.get();
                PatientRecordsEntity updatedEntity = PatientRecordsMapper.toEntity(patientRecordDTO);
                updatedEntity.setId(existingEntity.getId());

                logger.info("Updating patient record for patientId: {}", patientId);
                PatientRecordsEntity savedEntity = patientRecordsRepository.save(updatedEntity);
                return PatientRecordsMapper.toDTO(savedEntity);
            } else {
                // Create new record
                PatientRecordsEntity entity = PatientRecordsMapper.toEntity(patientRecordDTO);
                logger.info("Creating new patient record for patientId: {}", patientId);
                PatientRecordsEntity savedEntity = patientRecordsRepository.save(entity);
                return PatientRecordsMapper.toDTO(savedEntity);
            }
        } catch (DataAccessException e) {
            logger.error("Failed to save patient record for patientId: {}", patientId, e);
            throw new PatientRecordSaveFailedException("Failed to save patient record for patientId: " + patientId, e);
        }
    }

    /**
     * Retrieves a patient record by its unique ID.
     *
     * @param id the unique identifier of the patient record
     * @return the corresponding patient record as a DTO
     */
    public PatientRecordsDTO getPatientRecordById(UUID id) {
        logger.info("Retrieving patient record by id: {}", id);
        PatientRecordsEntity entity = patientRecordsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Patient record not found for id: {}", id);
                    return new PatientRecordNotFoundException("Patient record not found");
                });

        return PatientRecordsMapper.toDTO(entity);
    }

    /**
     * Retrieves a single patient record associated with a specific patient ID.
     *
     * @param patientId the unique identifier of the patient
     * @return the patient record DTO
     */
    public PatientRecordsDTO getPatientRecordByPatientId(UUID patientId) {
        logger.info("Retrieving patient record by patientId: {}", patientId);
        PatientRecordsEntity record = patientRecordsRepository.findByPatientId(patientId)
                .orElseThrow(() -> {
                    logger.warn("No patient record found for patientId: {}", patientId);
                    return new PatientRecordNotFoundException("No patient record found for patientId: " + patientId);
                });

        return PatientRecordsMapper.toDTO(record);
    }

    /**
     * Updates an existing patient record with new information.
     *
     * @param id            the unique identifier of the patient record
     * @param updatedRecord the DTO containing updated patient record details
     * @return the updated patient record as a DTO
     */
    @Transactional
    public PatientRecordsDTO updatePatientRecord(UUID id, PatientRecordsDTO updatedRecord) {
        logger.info("Updating patient record with id: {}", id);
        PatientRecordsEntity entity = patientRecordsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Patient record not found for update: {}", id);
                    return new PatientRecordNotFoundException("Patient record not found");
                });

        applyUpdates(entity, updatedRecord);

        PatientRecordsEntity savedEntity = patientRecordsRepository.save(entity);
        return PatientRecordsMapper.toDTO(savedEntity);
    }

    /**
     * Deletes a patient record by its unique ID.
     *
     * @param id the unique identifier of the patient record
     */
    @Transactional
    public void deletePatientRecord(UUID id) {
        logger.info("Deleting patient record with id: {}", id);
        if (!patientRecordsRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent patient record with id: {}", id);
            throw new PatientRecordNotFoundException("Patient record not found");
        }
        patientRecordsRepository.deleteById(id);
    }

    /**
     * Applies field updates from the DTO to the entity.
     *
     * @param entity the existing entity to be updated
     * @param dto    the DTO containing the updated fields
     */
    private void applyUpdates(PatientRecordsEntity entity, PatientRecordsDTO dto) {
        if (dto.getPatientId() != null) entity.setPatientId(dto.getPatientId());
        if (dto.getContact() != null) entity.setContact(dto.getContact());
        if (dto.getMedicalNotes() != null) entity.setMedicalNotes(dto.getMedicalNotes());
        // Add more fields as needed
    }
}
