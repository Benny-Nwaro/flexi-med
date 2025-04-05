package com.example.flexiMed.service;

import com.example.flexiMed.dto.PatientRecordsDTO;
import com.example.flexiMed.exceptions.ErrorResponse.PatientRecordNotFoundException;
import com.example.flexiMed.exceptions.ErrorResponse.PatientRecordSaveFailedException;
import com.example.flexiMed.mapper.PatientRecordsMapper;
import com.example.flexiMed.model.PatientRecordsEntity;
import com.example.flexiMed.repository.PatientRecordsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
     *
     * @param patientRecordDTO the DTO containing patient record details
     * @return the saved or updated patient record as a DTO
     * @throws PatientRecordSaveFailedException if saving the record fails
     */
    @Transactional
    public PatientRecordsDTO addPatientRecord(PatientRecordsDTO patientRecordDTO) {
        UUID patientId = patientRecordDTO.getPatientId();
        List<PatientRecordsEntity> existingRecords = patientRecordsRepository.findByPatientId(patientId);

        if (!existingRecords.isEmpty()) {
            // Update the first matching record
            PatientRecordsEntity existingEntity = existingRecords.get(0);
            PatientRecordsDTO updatedRecord = new PatientRecordsDTO(
                    existingEntity.getId(),
                    patientRecordDTO.getRequestId(),
                    patientRecordDTO.getPatientId(),
                    patientRecordDTO.getContact(),
                    patientRecordDTO.getMedicalNotes()
            );
            return updatePatientRecord(existingEntity.getId(), updatedRecord);
        } else {
            // Create a new record
            PatientRecordsEntity entity = new PatientRecordsEntity(
                    null,
                    patientRecordDTO.getRequestId(),
                    patientRecordDTO.getPatientId(),
                    patientRecordDTO.getContact(),
                    patientRecordDTO.getMedicalNotes()
            );
            try {
                PatientRecordsEntity savedEntity = patientRecordsRepository.save(entity);
                return PatientRecordsMapper.toDTO(savedEntity);
            } catch (Exception e) {
                throw new PatientRecordSaveFailedException("Failed to save patient record", e);
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
     * Retrieves all patient records associated with a specific patient ID.
     *
     * @param patientId the unique identifier of the patient
     * @return a list of patient record DTOs
     * @throws PatientRecordNotFoundException if no records are found
     */
    public List<PatientRecordsDTO> getPatientRecordsByPatientId(UUID patientId) {
        List<PatientRecordsEntity> records = patientRecordsRepository.findByPatientId(patientId);

        if (records.isEmpty()) {
            throw new PatientRecordNotFoundException("No patient records found for patientId: " + patientId);
        }

        return records.stream().map(PatientRecordsMapper::toDTO).collect(Collectors.toList());
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
