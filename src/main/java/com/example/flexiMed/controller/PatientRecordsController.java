package com.example.flexiMed.controller;

import com.example.flexiMed.dto.PatientRecordsDTO;
import com.example.flexiMed.service.PatientRecordsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing patient records in the FlexiMed application.
 * It provides endpoints for adding, viewing, updating, and deleting patient records.
 */
@RestController
@RequestMapping("/api/v1/patients")
public class PatientRecordsController {
    private final PatientRecordsService patientRecordsService;

    /**
     * Constructor to initialize the PatientRecordsController with the given PatientRecordsService.
     *
     * @param patientRecordsService The service responsible for managing patient records.
     */
    public PatientRecordsController(PatientRecordsService patientRecordsService) {
        this.patientRecordsService = patientRecordsService;
    }

    /**
     * Endpoint to add a new patient record.
     *
     * @param patientDTO The PatientRecordsDTO object containing the patient record details.
     * @return A ResponseEntity containing the created PatientRecordsDTO.
     */
    @PostMapping
    public ResponseEntity<PatientRecordsDTO> addPatient(@RequestBody PatientRecordsDTO patientDTO) {
        return ResponseEntity.ok(patientRecordsService.addPatientRecord(patientDTO));
    }

    /**
     * Endpoint to retrieve all patient records for a specific patient.
     *
     * @param patientId The unique ID of the patient whose records are to be retrieved.
     * @return A ResponseEntity containing a list of PatientRecordsDTOs for the given patient.
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PatientRecordsDTO>> getPatientRecordsByPatientId(@PathVariable UUID patientId) {
        List<PatientRecordsDTO> records = patientRecordsService.getPatientRecordsByPatientId(patientId);
        return ResponseEntity.ok(records);
    }

    /**
     * Endpoint to retrieve a specific patient record by ID.
     *
     * @param id The unique ID of the patient record to retrieve.
     * @return A ResponseEntity containing the PatientRecordsDTO for the given record ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatientRecordsDTO> getPatientRecordById(@PathVariable UUID id) {
        PatientRecordsDTO record = patientRecordsService.getPatientRecordById(id);
        return ResponseEntity.ok(record);
    }

    /**
     * Endpoint to update an existing patient record.
     *
     * @param id The ID of the patient record to update.
     * @param updatedRecord The updated PatientRecordsDTO object.
     * @return A ResponseEntity containing the updated PatientRecordsDTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PatientRecordsDTO> updatePatientRecord(
            @PathVariable UUID id,
            @RequestBody PatientRecordsDTO updatedRecord) {

        PatientRecordsDTO updated = patientRecordsService.updatePatientRecord(id, updatedRecord);
        return ResponseEntity.ok(updated);
    }

    /**
     * Endpoint to delete a patient record by ID.
     *
     * @param id The ID of the patient record to delete.
     * @return A ResponseEntity containing a success message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatientRecord(@PathVariable UUID id) {
        patientRecordsService.deletePatientRecord(id);
        return ResponseEntity.ok("Patient record deleted successfully.");
    }
}
