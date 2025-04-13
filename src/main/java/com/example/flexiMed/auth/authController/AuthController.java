package com.example.flexiMed.auth.authController;

import com.example.flexiMed.auth.authDTO.LoginRequest;
import com.example.flexiMed.auth.authDTO.RegistrationRequestDTO;
import com.example.flexiMed.model.UserEntity;
import com.example.flexiMed.security.JwtUtil;
import com.example.flexiMed.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling authentication-related requests,
 * such as user login and registration.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    /**
     * Constructor to initialize AuthController with necessary dependencies.
     *
     * @param authenticationManager Manages the authentication process.
     * @param jwtUtil             Provides utility methods for JWT manipulation.
     * @param userService         Service for handling user-related operations.
     */
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * Endpoint for user login. Authenticates the user based on the provided
     * email and password and returns a JWT upon successful authentication.
     *
     * @param request The {@link LoginRequest} object containing the user's email and password.
     * The `@Valid` annotation ensures that the request body adheres to the
     * validation rules defined in the {@link LoginRequest} class.
     * @return A {@link ResponseEntity} containing an {@link AuthResponse} with the JWT
     * on successful authentication, or an error response with an unauthorized
     * status code and error message on failure.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            // Authenticate the user using the provided email and password.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // If authentication is successful, set the authentication in the SecurityContextHolder.
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate a JWT for the authenticated user.
            String jwtToken = jwtUtil.generateToken(authentication);

            // Return the JWT in the response.
            return ResponseEntity.ok(new AuthResponse(jwtToken));

        } catch (Exception e) {
            // If authentication fails, return an unauthorized status with an error message.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }
    }

    /**
     * Endpoint for user registration. Creates a new user based on the provided
     * registration details and then automatically logs the user in, returning a JWT.
     *
     * @param registrationRequestDTO The {@link RegistrationRequestDTO} object containing the
     * user's registration details (name, email, password, role, phone number).
     * The `@Valid` annotation ensures that the request body adheres to the
     * validation rules defined in the {@link RegistrationRequestDTO} class.
     * @return A {@link ResponseEntity} containing an {@link AuthResponse} with the JWT
     * upon successful registration and automatic login, or an error response with an
     * internal server error status code and error message on failure.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequestDTO registrationRequestDTO) {
        try {
            // Create a new UserEntity from the registration details.
            UserEntity newUser = new UserEntity();
            newUser.setName(registrationRequestDTO.getName());
            newUser.setEmail(registrationRequestDTO.getEmail());
            newUser.setPassword(registrationRequestDTO.getPassword()); // Password will be hashed in userService.saveUser()
            newUser.setRole(registrationRequestDTO.getRole());       // Set the role from the DTO
            newUser.setPhoneNumber(registrationRequestDTO.getPhoneNumber()); // Set the phone number from the DTO

            // Save the new user to the database using the UserService.
            userService.saveUser(newUser);

            // Automatically log in the newly registered user by creating a LoginRequest.
            LoginRequest loginRequest = new LoginRequest(registrationRequestDTO.getEmail(), registrationRequestDTO.getPassword());

            // Call the login method to authenticate the new user and return the JWT.
            return login(loginRequest);

        } catch (Exception e) {
            // If an error occurs during registration, return an internal server error status
            // with the error message. Log the exception for debugging purposes.
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}

