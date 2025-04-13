package com.example.flexiMed.auth.authDTO;

import com.example.flexiMed.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for user registration requests.
 * Encapsulates the information required from a user during the registration process.
 */
public class RegistrationRequestDTO {
    /**
     * The full name of the user.
     * Must not be blank.
     */
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * The email address of the user.
     * Must not be blank and must be a valid email format.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    /**
     * The password chosen by the user.
     * Must not be blank and must be at least 6 characters long.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    /**
     * The role assigned to the user during registration.
     * Defaults to {@link Role#USER} if not explicitly provided.
     */
    private Role role;

    /**
     * The phone number of the user.
     * Must not be blank and must match the specified phone number format (10-15 digits).
     */
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Invalid phone number format") // Example: allows 10-15 digits
    private String phoneNumber;

    /**
     * Default constructor for {@code RegistrationRequestDTO}.
     * Initializes the user's role to {@link Role#USER}.
     */
    public RegistrationRequestDTO() {
        this.role = Role.USER; // Default role for registration
    }

    /**
     * Parameterized constructor for {@code RegistrationRequestDTO}.
     *
     * @param name        The name of the user.
     * @param email       The email address of the user.
     * @param password    The password chosen by the user.
     * @param role        The role of the user.
     * @param phoneNumber The phone number of the user.
     */
    public RegistrationRequestDTO(String name, String email, String password, Role role, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phoneNumber = phoneNumber;
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
     *
     * @param name The name of the user.
     */
    public void setName(String name) {
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
     *
     * @param email The email address of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password chosen by the user.
     *
     * @return The password chosen by the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password chosen by the user.
     *
     * @param password The password chosen by the user.
     */
    public void setPassword(String password) {
        this.password = password;
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
     *
     * @param phoneNumber The phone number of the user.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}