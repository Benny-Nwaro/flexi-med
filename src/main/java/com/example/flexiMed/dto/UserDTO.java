package com.example.flexiMed.dto;

import com.example.flexiMed.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing a user in the system.
 * It contains information such as the user's ID, name, email, role, phone number,
 * profile image URL, and password.
 */
public class UserDTO {

    /**
     * The unique identifier for the user.
     */
    private UUID userId;

    /**
     * The name of the user.
     * It must be between 2 and 100 characters long and cannot be blank.
     */
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    /**
     * The email address of the user.
     * It must be in a valid email format.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    /**
     * The role of the user.
     * This can be a value from the Role enum, such as "USER", "ADMIN", etc.
     */
    private Role role;

    /**
     * The URL of the user's profile image.
     * This field can be null if the user does not have a profile image.
     */
    private String profileImageUrl;

    /**
     * The phone number of the user.
     * It must consist of 10 to 15 digits.
     */
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    private String phoneNumber;

    /**
     * The password of the user.
     * It is marked with @JsonIgnore to ensure it is not serialized in JSON responses.
     * The password must be at least 8 characters long.
     */
    @JsonIgnore
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    /**
     * Default constructor for UserDTO.
     * This is required for frameworks that require a no-argument constructor.
     */
    public UserDTO() {
    }

    /**
     * Parameterized constructor for UserDTO.
     * This constructor allows initializing all fields of the UserDTO.
     *
     * @param userId          The unique ID of the user.
     * @param name            The name of the user.
     * @param email           The email address of the user.
     * @param role            The role of the user.
     * @param phoneNumber     The phone number of the user.
     * @param profileImageUrl The URL of the user's profile image (nullable).
     * @param password        The password of the user.
     */
    public UserDTO(UUID userId, String name, String email, Role role, String phoneNumber, String profileImageUrl, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.password = password;
    }

    // Getters & Setters

    /**
     * Gets the unique identifier for the user.
     *
     * @return The unique ID of the user.
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Sets the unique identifier for the user.
     *
     * @param userId The unique ID of the user.
     */
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    /**
     * Gets the name of the user.
     *
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     * The name must be between 2 and 100 characters.
     *
     * @param name The name of the user.
     * @throws IllegalArgumentException If the name is invalid.
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty() || name.length() < 2 || name.length() > 100) {
            throw new IllegalArgumentException("Name must be between 2 and 100 characters");
        }
        this.name = name;
    }

    /**
     * Gets the email address of the user.
     *
     * @return The email address of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     * The email must be in a valid format.
     *
     * @param email The email address of the user.
     * @throws IllegalArgumentException If the email format is invalid.
     */
    public void setEmail(String email) {
        if (email == null || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    /**
     * Gets the role of the user.
     *
     * @return The role of the user.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role The role of the user.
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Gets the phone number of the user.
     *
     * @return The phone number of the user.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the user.
     * The phone number must be between 10 and 15 digits.
     *
     * @param phoneNumber The phone number of the user.
     * @throws IllegalArgumentException If the phone number format is invalid.
     */
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.matches("^[0-9]{10,15}$")) {
            throw new IllegalArgumentException("Phone number must be 10-15 digits");
        }
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the URL of the user's profile image.
     *
     * @return The profile image URL of the user.
     */
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    /**
     * Sets the URL of the user's profile image.
     *
     * @param profileImageUrl The URL of the user's profile image.
     */
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     * The password must be at least 8 characters long.
     *
     * @param password The password of the user.
     * @throws IllegalArgumentException If the password is too short.
     */
    public void setPassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        this.password = password;
    }
}
