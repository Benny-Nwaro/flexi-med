package com.example.flexiMed.services;

import com.example.flexiMed.dto.PatientRecordsDTO;
import com.example.flexiMed.exceptions.ErrorResponse;
import com.example.flexiMed.mapper.PatientRecordsMapper;
import com.example.flexiMed.model.PatientRecordsEntity;
import com.example.flexiMed.repository.PatientRecordsRepository;
import com.example.flexiMed.service.PatientRecordsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the PatientRecordsService.
 * This class tests the functionality of the PatientRecordsService, ensuring that it interacts
 * correctly with the PatientRecordsRepository and handles various scenarios, including
 * adding, updating, retrieving, and deleting patient records.
 */
@ExtendWith(MockitoExtension.class)
public class PatientRecordsServiceTest {

    @Mock
    private PatientRecordsRepository patientRecordsRepository;

    @InjectMocks
    private PatientRecordsService patientRecordsService;

    private PatientRecordsEntity patientRecordsEntity;
    private PatientRecordsDTO patientRecordsDTO;
    private UUID recordId;
    private UUID patientId;

    /**
     * Sets up the test environment before each test method.
     * Initializes the necessary objects, including patient records entities and DTOs.
     */
    @BeforeEach
    void setUp() {
        recordId = UUID.randomUUID();
        patientId = UUID.randomUUID();
        patientRecordsEntity = new PatientRecordsEntity(recordId, UUID.randomUUID(), patientId, "contact", "medical notes");
        patientRecordsDTO = PatientRecordsMapper.toDTO(patientRecordsEntity);
    }

    /**
     * Tests the scenario where a new patient record is added when no existing record exists.
     * Verifies that the record is saved to the repository and the returned DTO matches the input DTO.
     */
    @Test
    void addPatientRecord_shouldSaveNewRecord_whenNoExistingRecord() {
        when(patientRecordsRepository.findByPatientId(patientId)).thenReturn(List.of());
        when(patientRecordsRepository.save(any(PatientRecordsEntity.class))).thenReturn(patientRecordsEntity);

        PatientRecordsDTO result = patientRecordsService.addPatientRecord(patientRecordsDTO);

        // Field-by-field comparison to ensure equality
        assertEquals(patientRecordsDTO.getId(), result.getId());
        assertEquals(patientRecordsDTO.getPatientId(), result.getPatientId());
        assertEquals(patientRecordsDTO.getContact(), result.getContact());
        assertEquals(patientRecordsDTO.getMedicalNotes(), result.getMedicalNotes());
        verify(patientRecordsRepository, times(1)).save(any(PatientRecordsEntity.class));
    }

    /**
     * Tests the scenario where an existing patient record is updated.
     * Verifies that the record is updated in the repository and the returned DTO matches the input DTO.
     */
    @Test
    void addPatientRecord_shouldUpdateExistingRecord_whenRecordExists() {
        PatientRecordsEntity existingEntity = new PatientRecordsEntity(recordId, UUID.randomUUID(), patientId, "old contact", "old notes");
        when(patientRecordsRepository.findByPatientId(patientId)).thenReturn(List.of(existingEntity));
        when(patientRecordsRepository.findById(recordId)).thenReturn(Optional.of(existingEntity));
        when(patientRecordsRepository.save(any(PatientRecordsEntity.class))).thenReturn(patientRecordsEntity);

        PatientRecordsDTO result = patientRecordsService.addPatientRecord(patientRecordsDTO);

        // Field-by-field comparison to ensure equality
        assertEquals(patientRecordsDTO.getId(), result.getId());
        assertEquals(patientRecordsDTO.getPatientId(), result.getPatientId());
        assertEquals(patientRecordsDTO.getContact(), result.getContact());
        assertEquals(patientRecordsDTO.getMedicalNotes(), result.getMedicalNotes());
        verify(patientRecordsRepository, times(1)).save(any(PatientRecordsEntity.class));
    }

    /**
     * Tests the scenario where saving a patient record fails.
     * Verifies that a PatientRecordSaveFailedException is thrown.
     */
    @Test
    void addPatientRecord_shouldThrowException_whenSaveFails() {
        when(patientRecordsRepository.findByPatientId(patientId)).thenReturn(List.of());
        when(patientRecordsRepository.save(any(PatientRecordsEntity.class))).thenThrow(new RuntimeException("Save failed"));

        assertThrows(ErrorResponse.PatientRecordSaveFailedException.class, () -> patientRecordsService.addPatientRecord(patientRecordsDTO));
    }

    /**
     * Tests the scenario where a patient record is retrieved by ID and exists.
     * Verifies that the correct record is returned as a DTO.
     */
    @Test
    void getPatientRecordById_shouldReturnRecord_whenRecordExists() {
        when(patientRecordsRepository.findById(recordId)).thenReturn(Optional.of(patientRecordsEntity));

        PatientRecordsDTO result = patientRecordsService.getPatientRecordById(recordId);

        // Field-by-field comparison to ensure equality
        assertEquals(patientRecordsDTO.getId(), result.getId());
        assertEquals(patientRecordsDTO.getPatientId(), result.getPatientId());
        assertEquals(patientRecordsDTO.getContact(), result.getContact());
        assertEquals(patientRecordsDTO.getMedicalNotes(), result.getMedicalNotes());
    }

    /**
     * Tests the scenario where a patient record is retrieved by ID but does not exist.
     * Verifies that a PatientRecordNotFoundException is thrown.
     */
    @Test
    void getPatientRecordById_shouldThrowException_whenRecordDoesNotExist() {
        when(patientRecordsRepository.findById(recordId)).thenReturn(Optional.empty());

        assertThrows(ErrorResponse.PatientRecordNotFoundException.class, () -> patientRecordsService.getPatientRecordById(recordId));
    }

    /**
     * Tests the scenario where patient records are retrieved by patient ID and records exist.
     * Verifies that a list of records is returned and matches the expected size and content.
     */
    @Test
    void getPatientRecordsByPatientId_shouldReturnListOfRecords_whenRecordsExist() {
        when(patientRecordsRepository.findByPatientId(patientId)).thenReturn(Arrays.asList(patientRecordsEntity));

        List<PatientRecordsDTO> result = patientRecordsService.getPatientRecordsByPatientId(patientId);

        assertEquals(1, result.size());

        // Field-by-field comparison to ensure equality
        assertEquals(patientRecordsDTO.getId(), result.get(0).getId());
        assertEquals(patientRecordsDTO.getPatientId(), result.get(0).getPatientId());
        assertEquals(patientRecordsDTO.getContact(), result.get(0).getContact());
        assertEquals(patientRecordsDTO.getMedicalNotes(), result.get(0).getMedicalNotes());
    }

    /**
     * Tests the scenario where patient records are retrieved by patient ID but no records exist.
     * Verifies that a PatientRecordNotFoundException is thrown.
     */
    @Test
    void getPatientRecordsByPatientId_shouldThrowException_whenNoRecordsExist() {
        when(patientRecordsRepository.findByPatientId(patientId)).thenReturn(List.of());

        assertThrows(ErrorResponse.PatientRecordNotFoundException.class, () -> patientRecordsService.getPatientRecordsByPatientId(patientId));
    }

    /**
     * Tests the scenario where a patient record is updated and exists.
     * Verifies that the record is updated in the repository and the returned DTO matches the input DTO.
     */
    @Test
    void updatePatientRecord_shouldUpdateRecord_whenRecordExists() {
        when(patientRecordsRepository.findById(recordId)).thenReturn(Optional.of(patientRecordsEntity));
        when(patientRecordsRepository.save(any(PatientRecordsEntity.class))).thenReturn(patientRecordsEntity);

        PatientRecordsDTO result = patientRecordsService.updatePatientRecord(recordId, patientRecordsDTO);

        // Field-by-field comparison to ensure equality
        assertEquals(patientRecordsDTO.getId(), result.getId());
        assertEquals(patientRecordsDTO.getPatientId(), result.getPatientId());
        assertEquals(patientRecordsDTO.getContact(), result.getContact());
        assertEquals(patientRecordsDTO.getMedicalNotes(), result.getMedicalNotes());
        verify(patientRecordsRepository, times(1)).save(any(PatientRecordsEntity.class));
    }

    /**
     * Tests the scenario where a patient record is updated but does not exist.
     * Verifies that a PatientRecordNotFoundException is thrown.
     */
    @Test
    void updatePatientRecord_shouldThrowException_whenRecordDoesNotExist() {
        when(patientRecordsRepository.findById(recordId)).thenReturn(Optional.empty());

        assertThrows(ErrorResponse.PatientRecordNotFoundException.class, () -> patientRecordsService.updatePatientRecord(recordId, patientRecordsDTO));
    }

    /**
     * Tests the scenario where a patient record is deleted and exists.
     * Verifies that the record is deleted from the repository.
     */
    @Test
    void deletePatientRecord_shouldDeleteRecord_whenRecordExists() {
        when(patientRecordsRepository.existsById(recordId)).thenReturn(true);
        doNothing().when(patientRecordsRepository).deleteById(recordId);

        patientRecordsService.deletePatientRecord(recordId);

        verify(patientRecordsRepository, times(1)).deleteById(recordId);
    }

    /**
     * Tests the scenario where a patient record is deleted but does not exist.
     * Verifies that a PatientRecordNotFoundException is thrown and no deletion occurs.
     */
    @Test
    void deletePatientRecord_shouldThrowException_whenRecordDoesNotExist() {
        when(patientRecordsRepository.existsById(recordId)).thenReturn(false);

        assertThrows(ErrorResponse.PatientRecordNotFoundException.class, () -> patientRecordsService.deletePatientRecord(recordId));
        verify(patientRecordsRepository, never()).deleteById(recordId);
    }
}