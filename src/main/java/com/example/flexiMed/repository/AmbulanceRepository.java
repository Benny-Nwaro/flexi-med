package com.example.flexiMed.repository;

import com.example.flexiMed.model.AmbulanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link AmbulanceEntity} objects in the database.
 * Extends {@link JpaRepository} to provide standard CRUD operations and custom query methods.
 */
@Repository
public interface AmbulanceRepository extends JpaRepository<AmbulanceEntity, UUID> {

    /**
     * Finds the first AmbulanceEntity with availability status set to true.
     *
     * @return An Optional containing the first available AmbulanceEntity, or an empty Optional if none are found.
     */
    Optional<AmbulanceEntity> findFirstByAvailabilityStatusIsTrue();

    /**
     * Finds all AmbulanceEntity objects with availability status set to true.
     *
     * @return A List of AmbulanceEntity objects that are currently available.
     */
    List<AmbulanceEntity> findByAvailabilityStatusIsTrue();
}