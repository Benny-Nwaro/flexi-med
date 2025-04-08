package com.example.flexiMed.controllers;

import com.example.flexiMed.controller.PatientRecordsController;
import com.example.flexiMed.dto.PatientRecordsDTO;
import com.example.flexiMed.service.PatientRecordsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the PatientRecordsController.
 * This class tests the functionality of the PatientRecordsController, ensuring that it correctly
 * interacts with the PatientRecordsService and handles HTTP requests for patient records.
 */
@ExtendWith(MockitoExtension.class)
public class PatientRecordsControllerTest {

    @Mock
    private PatientRecordsService patientRecordsService;

    @InjectMocks
    private PatientRecordsController patientRecordsController;

    private UUID patientId;
    private UUID recordId;
    private PatientRecordsDTO patientRecordsDTO;

    /**
     * Sets up the test environment before each test method.
     * Initializes the necessary objects, including patient IDs, record IDs, and patient records DTOs.
     */
    @BeforeEach
    void setUp() {
        patientId = UUID.randomUUID();
        recordId = UUID.randomUUID();
        patientRecordsDTO = new PatientRecordsDTO();
        patientRecordsDTO.setId(recordId);
        patientRecordsDTO.setPatientId(patientId);
        patientRecordsDTO.setMedicalNotes("Test details");
    }

    /**
     * Tests adding a new patient record.
     * Verifies that the controller returns the created patient record with HTTP status OK.
     */
    @Test
    void addPatient_shouldReturnCreatedPatientRecord() {
        when(patientRecordsService.addPatientRecord(patientRecordsDTO)).thenReturn(patientRecordsDTO);

        ResponseEntity<PatientRecordsDTO> response = patientRecordsController.addPatient(patientRecordsDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patientRecordsDTO, response.getBody());
    }



    /**
     * Tests retrieving a patient record by record ID.
     * Verifies that the controller returns the patient record with HTTP status OK.
     */
    @Test
    void getPatientRecordById_shouldReturnPatientRecord() {
        when(patientRecordsService.getPatientRecordById(recordId)).thenReturn(patientRecordsDTO);

        ResponseEntity<PatientRecordsDTO> response = patientRecordsController.getPatientRecordById(recordId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patientRecordsDTO, response.getBody());
    }

    /**
     * Tests updating a patient record.
     * Verifies that the controller returns the updated patient record with HTTP status OK.
     */
    @Test
    void updatePatientRecord_shouldReturnUpdatedPatientRecord() {
        when(patientRecordsService.updatePatientRecord(recordId, patientRecordsDTO)).thenReturn(patientRecordsDTO);

        ResponseEntity<PatientRecordsDTO> response = patientRecordsController.updatePatientRecord(recordId, patientRecordsDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patientRecordsDTO, response.getBody());
    }

    /**
     * Tests deleting a patient record.
     * Verifies that the controller returns a success message with HTTP status OK and calls the service's delete method.
     */
    @Test
    void deletePatientRecord_shouldReturnSuccessMessage() {
        ResponseEntity<String> response = patientRecordsController.deletePatientRecord(recordId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Patient record deleted successfully.", response.getBody());
        verify(patientRecordsService, times(1)).deletePatientRecord(recordId);
    }
}