package com.example.flexiMed.model;

import com.example.flexiMed.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Entity representing a user in the system.
 * This entity holds user information such as name, email, password, phone number, role, and profile image URL.
 * It also implements {@link UserDetails} for Spring Security authentication purposes.
 */
@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {

    /**
     * The unique identifier for the user.
     * This field is the primary key and is automatically generated as a UUID.
     */
    @Id
    @Column(name = "user_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    /**
     * The name of the user.
     * This field must not be blank and has a maximum length of 100 characters.
     */
    @NotBlank(message = "Name cannot be empty")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * The email address of the user.
     * This field must not be blank, must be a valid email, and must be unique.
     * The maximum length is 150 characters.
     */
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    /**
     * The password of the user.
     * This field must not be blank and must have a minimum length of 8 characters.
     */
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * The phone number of the user.
     * This field has a maximum length of 15 characters and must be unique.
     */
    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    @Column(name = "phone_number", unique = true, length = 15)
    private String phoneNumber;

    /**
     * The creation date of the user record.
     * This field is set automatically when the entity is created.
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * The role assigned to the user.
     * The role defines the permissions the user has within the application.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    /**
     * The URL of the user's profile image.
     * This field is optional and can be null.
     */
    @Column(name = "profile_image", nullable = true)
    private String profileImageUrl;

    /**
     * The version number of the user entity.
     * This field is used for optimistic locking to handle concurrent updates.
     */
    @Version
    @Column(name = "version")
    private Long version;

    /**
     * Default constructor to initialize createdAt and version.
     */
    public UserEntity() {
        this.createdAt = LocalDateTime.now();
        this.version = 0L;
    }

    /**
     * Constructor to create a new UserEntity with name, email, and password.
     * The userId is generated as a UUID, and createdAt and version are set automatically.
     *
     * @param name     The name of the user.
     * @param email    The email of the user.
     * @param password The password of the user.
     */
    public UserEntity(String name, String email, String password) {
        this.userId = UUID.randomUUID(); // Generate UUID when new user is created
        this.email = validateString(email, "Email");
        this.name = validateString(name, "Name");
        this.password = validatePassword(password);
        this.role = Role.USER; // Default role is USER
        this.createdAt = LocalDateTime.now();
        this.version = 0L;
    }

    /**
     * Constructor to create a fully initialized UserEntity with all fields.
     *
     * @param userId         The unique ID of the user.
     * @param name           The name of the user.
     * @param email          The email of the user.
     * @param password       The password of the user.
     * @param phoneNumber    The phone number of the user.
     * @param role           The role of the user.
     * @param profileImageUrl The URL of the user's profile image.
     */
    public UserEntity(UUID userId, String name, String email, String password, String phoneNumber, Role role, String profileImageUrl) {
        this.userId = userId;
        this.name = validateString(name, "Name");
        this.email = validateString(email, "Email");
        this.password = validatePassword(password);
        this.phoneNumber = phoneNumber;
        this.createdAt = LocalDateTime.now();
        this.role = role;
        this.profileImageUrl = profileImageUrl;
        this.version = 0L;
    }

    /**
     * Lifecycle callback to set the creation date before the entity is persisted.
     */
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    /**
     * Returns the authorities granted to the user.
     * The authorities are based on the user's role.
     *
     * @return A collection of granted authorities for the user.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    /**
     * Returns the password of the user.
     *
     * @return The user's password.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the username of the user (email).
     *
     * @return The email of the user.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return Always returns true (account is non-expired).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked.
     *
     * @return Always returns true (account is non-locked).
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials have expired.
     *
     * @return Always returns true (credentials are non-expired).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled.
     *
     * @return Always returns true (user is enabled).
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    // Getters and Setters

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = validateString(name, "Name");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = validateString(email, "Email");
    }

    public void setPassword(String password) {
        this.password = validatePassword(password);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Validates and trims the input string.
     * Throws an {@link IllegalArgumentException} if the string is empty or null.
     *
     * @param value     The string to validate.
     * @param fieldName The name of the field being validated.
     * @return The trimmed value.
     */
    private String validateString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }

    /**
     * Validates the user's password.
     * Throws an {@link IllegalArgumentException} if the password does not meet the minimum length requirement.
     *
     * @param password The password to validate.
     * @return The validated password.
     */
    private String validatePassword(String password) {
        // Allow OAuth2 users to have a placeholder password
        if (password == null || password.equals("GoogleAuthentication")) {
            return password; // Skip validation for OAuth users
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }

        return password;
    }

    /**
     * Returns a string representation of the user entity.
     *
     * @return A string representing the user entity.
     */
    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", createdAt=" + createdAt +
                '}';
    }
}
