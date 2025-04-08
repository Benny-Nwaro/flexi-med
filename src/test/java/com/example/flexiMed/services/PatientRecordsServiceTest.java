package com.example.flexiMed.services;

import com.example.flexiMed.dto.PatientRecordsDTO;
import com.example.flexiMed.exceptions.ErrorResponse.PatientRecordNotFoundException;
import com.example.flexiMed.exceptions.ErrorResponse.PatientRecordSaveFailedException;
import com.example.flexiMed.model.PatientRecordsEntity;
import com.example.flexiMed.repository.PatientRecordsRepository;
import com.example.flexiMed.service.PatientRecordsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientRecordsServiceTest {

    private PatientRecordsRepository repository;
    private PatientRecordsService service;

    @BeforeEach
    void setUp() {
        repository = mock(PatientRecordsRepository.class);
        service = new PatientRecordsService(repository);
    }

    @Test
    void addPatientRecord_shouldCreateNewRecord() {
        UUID patientId = UUID.randomUUID();
        PatientRecordsDTO dto = new PatientRecordsDTO();
        dto.setPatientId(patientId);

        PatientRecordsEntity entity = new PatientRecordsEntity();
        entity.setPatientId(patientId);

        when(repository.findByPatientId(patientId)).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(entity);

        PatientRecordsDTO result = service.addPatientRecord(dto);

        assertNotNull(result);
        verify(repository, times(1)).save(any(PatientRecordsEntity.class));
    }

    @Test
    void addPatientRecord_shouldUpdateExistingRecord() {
        UUID patientId = UUID.randomUUID();
        UUID recordId = UUID.randomUUID();
        PatientRecordsDTO dto = new PatientRecordsDTO();
        dto.setPatientId(patientId);

        PatientRecordsEntity existing = new PatientRecordsEntity();
        existing.setId(recordId);
        existing.setPatientId(patientId);

        when(repository.findByPatientId(patientId)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenReturn(existing);

        PatientRecordsDTO result = service.addPatientRecord(dto);

        assertNotNull(result);
        verify(repository, times(1)).save(any(PatientRecordsEntity.class));
    }

    @Test
    void addPatientRecord_shouldThrowOnSaveFailure() {
        UUID patientId = UUID.randomUUID();
        PatientRecordsDTO dto = new PatientRecordsDTO();
        dto.setPatientId(patientId);

        when(repository.findByPatientId(patientId)).thenReturn(Optional.empty());
        when(repository.save(any())).thenThrow(new DataAccessException("DB error") {});

        assertThrows(PatientRecordSaveFailedException.class, () -> service.addPatientRecord(dto));
    }

    @Test
    void getPatientRecordById_shouldReturnRecord() {
        UUID id = UUID.randomUUID();
        PatientRecordsEntity entity = new PatientRecordsEntity();
        entity.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        PatientRecordsDTO result = service.getPatientRecordById(id);

        assertNotNull(result);
    }

    @Test
    void getPatientRecordById_shouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PatientRecordNotFoundException.class, () -> service.getPatientRecordById(id));
    }

    @Test
    void getPatientRecordByPatientId_shouldReturnRecord() {
        UUID patientId = UUID.randomUUID();
        PatientRecordsEntity entity = new PatientRecordsEntity();
        entity.setPatientId(patientId);

        when(repository.findByPatientId(patientId)).thenReturn(Optional.of(entity));

        PatientRecordsDTO result = service.getPatientRecordByPatientId(patientId);

        assertNotNull(result);
    }

    @Test
    void getPatientRecordByPatientId_shouldThrowIfNotFound() {
        UUID patientId = UUID.randomUUID();
        when(repository.findByPatientId(patientId)).thenReturn(Optional.empty());

        assertThrows(PatientRecordNotFoundException.class, () -> service.getPatientRecordByPatientId(patientId));
    }

    @Test
    void updatePatientRecord_shouldUpdateSuccessfully() {
        UUID id = UUID.randomUUID();
        PatientRecordsDTO dto = new PatientRecordsDTO();
        dto.setPatientId(UUID.randomUUID());

        PatientRecordsEntity entity = new PatientRecordsEntity();
        entity.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);

        PatientRecordsDTO result = service.updatePatientRecord(id, dto);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void updatePatientRecord_shouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();
        PatientRecordsDTO dto = new PatientRecordsDTO();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PatientRecordNotFoundException.class, () -> service.updatePatientRecord(id, dto));
    }

    @Test
    void deletePatientRecord_shouldDeleteSuccessfully() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(id);

        assertDoesNotThrow(() -> service.deletePatientRecord(id));
    }

    @Test
    void deletePatientRecord_shouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(PatientRecordNotFoundException.class, () -> service.deletePatientRecord(id));
    }
}
