package com.example.flexiMed.dto;

/**
 * Data Transfer Object (DTO) for representing the login request data.
 * This class holds the email and password provided by the user for authentication.
 */
public class LoginRequestDTO {

    /**
     * The email address of the user attempting to log in.
     * This will be used to identify the user for authentication.
     */
    private String email;

    /**
     * The password associated with the user's account.
     * This is used to authenticate the user in the system.
     */
    private String password;

    /**
     * Constructor to initialize a new login request DTO with the provided email and password.
     *
     * @param email    The email address of the user.
     * @param password The password of the user.
     */
    public LoginRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Gets the email address of the user attempting to log in.
     *
     * @return The email address of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user attempting to log in.
     *
     * @param email The email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of the user attempting to log in.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user attempting to log in.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
