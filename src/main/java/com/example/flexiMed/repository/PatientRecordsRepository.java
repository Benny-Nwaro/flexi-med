package com.example.flexiMed.repository;

import com.example.flexiMed.model.PatientRecordsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link PatientRecordsEntity} objects in the database.
 * Extends {@link JpaRepository} to provide standard CRUD operations and custom query methods.
 */
@Repository
public interface PatientRecordsRepository extends JpaRepository<PatientRecordsEntity, UUID> {

    /**
     * Finds a list of PatientRecordsEntity objects associated with a specific patient ID.
     *
     * @param patientId The UUID of the patient to search for.
     * @return A List of PatientRecordsEntity objects associated with the given patient ID.
     */
    List<PatientRecordsEntity> findByPatientId(UUID patientId);
}