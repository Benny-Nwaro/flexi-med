package com.example.flexiMed.repository;

import com.example.flexiMed.enums.RequestStatus;
import com.example.flexiMed.model.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
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
     * @param requestStatus The RequestStatus to exclude from the count.
     * @return The number of RequestEntity objects matching the criteria.
     */


    /**
     * Finds a list of RequestEntity objects filtered by request status.
     *
     * @param requestStatus The RequestStatus to filter by.
     * @return A List of RequestEntity objects matching the given status.
     */
    List<RequestEntity> findByRequestStatus(RequestStatus requestStatus);

    /**
     * Finds a list of RequestEntity objects associated with a specific ambulance ID
     * and having a specific request status.
     *
     * @param ambulance_id The UUID of the ambulance to search for.
     * @param requestStatus The {@link RequestStatus} to filter by.
     * @return A List of RequestEntity objects associated with the given ambulance ID
     * and having the specified request status.
     */
    List<RequestEntity> findByAmbulanceIdAndRequestStatus(UUID ambulance_id, RequestStatus requestStatus);

    /**
     * Counts the number of requests associated with a specific ambulance ID
     * that have a request status other than 'COMPLETED'.
     *
     * @param ambulanceId The UUID of the ambulance.
     * @param status      The RequestStatus to exclude from the count
     * (in this case, it should be RequestStatus.COMPLETED).
     * @return The number of requests associated with the given ambulance ID
     * that do not have the specified status.
     */
    long countByAmbulance_IdAndRequestStatusNot(@Param("ambulanceId") UUID ambulanceId, @Param("status") RequestStatus status);


}