package com.example.flexiMed.mapper;

import com.example.flexiMed.dto.UserDTO;
import com.example.flexiMed.model.UserEntity;

import java.util.Objects;

/**
 * Mapper class for converting between {@link UserEntity} and {@link UserDTO} objects.
 * This class provides static methods to map data between the entity and DTO representations.
 */
public class UserMapper {

    /**
     * Converts a UserEntity object to a UserDTO object.
     * Excludes the password field for security reasons.
     *
     * @param user The UserEntity object to convert.
     * @return The corresponding UserDTO object, or throws NullPointerException if input is null.
     * @throws NullPointerException if the input UserEntity is null.
     */
    public static UserDTO toDTO(UserEntity user) {
        Objects.requireNonNull(user, "UserEntity cannot be null");

        return new UserDTO(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getPhoneNumber(),
                user.getProfileImageUrl(),
                null  // Exclude password for security
        );
    }

    /**
     * Converts a UserDTO object to a UserEntity object.
     *
     * @param userDTO The UserDTO object to convert.
     * @return The corresponding UserEntity object, or throws NullPointerException if input is null.
     * @throws NullPointerException if the input UserDTO is null.
     */
    public static UserEntity toEntity(UserDTO userDTO) {
        Objects.requireNonNull(userDTO, "UserDTO cannot be null");

        return new UserEntity(
                userDTO.getUserId(),
                userDTO.getName(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getPhoneNumber(),
                userDTO.getRole(),
                userDTO.getProfileImageUrl()
        );
    }
}