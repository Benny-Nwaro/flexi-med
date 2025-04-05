package com.example.flexiMed.controller;

import com.example.flexiMed.enums.Role;
import com.example.flexiMed.dto.UserDTO;
import com.example.flexiMed.model.UserEntity;
import com.example.flexiMed.mapper.UserMapper;
import com.example.flexiMed.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing users within the FlexiMed application.
 * Provides endpoints for user-related operations such as retrieving, updating, and deleting user profiles.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructor to initialize UserController with the given UserService.
     *
     * @param userService The service responsible for handling user-related operations.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint to get all users.
     *
     * @return A list of all users.
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Endpoint to get a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user with the specified ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Endpoint to get a user by their email.
     *
     * @param email The email address of the user to retrieve.
     * @return The user with the specified email address.
     */
    @GetMapping("/by-email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    /**
     * Endpoint to get users by their role.
     *
     * @param role The role to filter users by.
     * @return A list of users who have the specified role.
     */
    @GetMapping("/role/{role}")
    public List<UserDTO> getUsersByRole(@PathVariable Role role) {
        return userService.getUsersByRole(role);
    }

    /**
     * Endpoint to delete a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @return HTTP 204 No Content if deletion is successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to update a user's profile.
     * Allows for updating the user's details and optionally uploading a new profile image.
     *
     * @param userId The ID of the user to update.
     * @param updatedUser The updated user details.
     * @param profileImage The new profile image (optional).
     * @return The updated user profile.
     */
    @PutMapping(value = "/{userId}", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<UserDTO> updateUserProfile(
            @PathVariable UUID userId,
            @RequestPart("user") UserDTO updatedUser,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {

        UserDTO userDTO = userService.updateUserProfile(userId, updatedUser, profileImage);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * Endpoint to get the currently authenticated user's profile.
     *
     * @param user The currently authenticated user.
     * @return The current user's profile details.
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal UserEntity user) {
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

}
