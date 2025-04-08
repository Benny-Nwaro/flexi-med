package com.example.flexiMed.repository;

import com.example.flexiMed.model.PatientRecordsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link PatientRecordsEntity} objects in the database.
 * Extends {@link JpaRepository} to provide standard CRUD operations and custom query methods.
 */
@Repository
public interface PatientRecordsRepository extends JpaRepository<PatientRecordsEntity, UUID> {

    /**
     * Finds a single PatientRecordsEntity object associated with a specific patient ID.
     * It is assumed that each patient ID will have at most one primary patient record.
     *
     * @param patientId The UUID of the patient to search for.
     * @return An Optional containing the PatientRecordsEntity associated with the given patient ID,
     * or an empty Optional if no such record exists.
     */
    Optional<PatientRecordsEntity> findByPatientId(UUID patientId);
}