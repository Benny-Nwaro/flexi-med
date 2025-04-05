package com.example.flexiMed.repository;

import com.example.flexiMed.enums.Role;
import com.example.flexiMed.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link UserEntity} objects in the database.
 * Extends {@link JpaRepository} to provide standard CRUD operations and custom query methods.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    /**
     * Finds a UserEntity by their email address.
     *
     * @param email The email address to search for.
     * @return An Optional containing the UserEntity if found, or an empty Optional if not found.
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Finds a UserEntity by their phone number.
     *
     * @param phoneNumber The phone number to search for.
     * @return An Optional containing the UserEntity if found, or an empty Optional if not found.
     */
    Optional<UserEntity> findByPhoneNumber(String phoneNumber);

    /**
     * Finds a list of UserEntities by their role.
     *
     * @param role The role to filter users by.
     * @return A List of UserEntities with the specified role.
     */
    List<UserEntity> findByRole(Role role);
}