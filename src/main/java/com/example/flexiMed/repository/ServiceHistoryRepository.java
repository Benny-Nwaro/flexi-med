package com.example.flexiMed.repository;

import com.example.flexiMed.model.ServiceHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link ServiceHistoryEntity} objects in the database.
 * Extends {@link JpaRepository} to provide standard CRUD operations and custom query methods.
 */
@Repository
public interface ServiceHistoryRepository extends JpaRepository<ServiceHistoryEntity, UUID> {

    /**
     * Finds a list of ServiceHistoryEntity objects associated with a specific request ID.
     *
     * @param requestId The UUID of the request to search for.
     * @return A List of ServiceHistoryEntity objects associated with the given request ID.
     */
    List<ServiceHistoryEntity> findByRequest_Id(UUID requestId);
}