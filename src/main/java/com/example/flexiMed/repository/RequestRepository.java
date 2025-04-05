package com.example.flexiMed.repository;

import com.example.flexiMed.model.RequestEntity;
import com.example.flexiMed.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link RequestEntity} objects in the database.
 * Extends {@link JpaRepository} to provide standard CRUD operations and custom query methods.
 */
@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, UUID> {

    /**
     * Finds a list of RequestEntity objects associated with a specific user ID.
     *
     * @param userId The UUID of the user to search for.
     * @return A List of RequestEntity objects associated with the given user ID.
     */
    List<RequestEntity> findByUserUserId(UUID userId);

    /**
     * Counts the number of RequestEntity objects associated with a specific ambulance ID,
     * excluding those with a given request status.
     *
     * @param ambulanceId   The UUID of the ambulance to search for.
     * @param requestStatus The RequestStatus to exclude from the count.
     * @return The number of RequestEntity objects matching the criteria.
     */
    long countByAmbulance_IdAndRequestStatusNot(UUID ambulanceId, RequestStatus requestStatus);
}