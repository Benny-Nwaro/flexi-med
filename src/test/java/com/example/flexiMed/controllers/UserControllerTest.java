package com.example.flexiMed.controllers;

import com.example.flexiMed.controller.UserController;
import com.example.flexiMed.dto.UserDTO;
import com.example.flexiMed.enums.Role;
import com.example.flexiMed.mapper.UserMapper;
import com.example.flexiMed.model.UserEntity;
import com.example.flexiMed.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the UserController.
 * This class tests the functionality of the UserController, ensuring that it correctly
 * interacts with the UserService and handles various user-related HTTP requests.
 */
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UUID userId;
    private UserDTO userDTO;
    private UserEntity userEntity;
    private MultipartFile profileImage;

    /**
     * Sets up the test environment before each test method.
     * Initializes the necessary objects, including user DTOs and entities, and a mock profile image.
     */
    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setEmail("test@example.com");
        userDTO.setName("test name");
        userDTO.setRole(Role.USER);

        userEntity = UserMapper.toEntity(userDTO);

        profileImage = new MockMultipartFile("profileImage", "image.jpg", "image/jpeg", "image".getBytes());
    }

    /**
     * Tests retrieving all users.
     * Verifies that the controller returns a list of users with HTTP status OK.
     */
    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        List<UserDTO> userDTOList = Arrays.asList(userDTO);
        when(userService.getAllUsers()).thenReturn(userDTOList);

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTOList, response.getBody());
    }

    /**
     * Tests retrieving a user by ID.
     * Verifies that the controller returns the user with HTTP status OK.
     */
    @Test
    void getUserById_shouldReturnUser() {
        when(userService.getUserById(userId)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    /**
     * Tests retrieving a user by email.
     * Verifies that the controller returns the user with HTTP status OK.
     */
    @Test
    void getUserByEmail_shouldReturnUser() {
        when(userService.getUserByEmail("test@example.com")).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.getUserByEmail("test@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    /**
     * Tests retrieving users by role.
     * Verifies that the controller returns a list of users with the specified role.
     */
    @Test
    void getUsersByRole_shouldReturnListOfUsers() {
        List<UserDTO> userDTOList = Arrays.asList(userDTO);
        when(userService.getUsersByRole(Role.USER)).thenReturn(userDTOList);

        List<UserDTO> result = userController.getUsersByRole(Role.USER);

        assertEquals(userDTOList, result);
    }

    /**
     * Tests deleting a user.
     * Verifies that the controller returns HTTP status NO_CONTENT and calls the userService's deleteUser method.
     */
    @Test
    void deleteUser_shouldReturnNoContent() {
        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(userId);
    }

    /**
     * Tests updating a user profile.
     * Verifies that the controller returns the updated user with HTTP status OK.
     */
    @Test
    void updateUserProfile_shouldReturnUpdatedUser() {
        when(userService.updateUserProfile(userId, userDTO, profileImage)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.updateUserProfile(userId, userDTO, profileImage);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    /**
     * Tests retrieving the current user.
     * Verifies that the controller returns the current user with HTTP status OK.
     */
    @Test
    void getCurrentUser_shouldReturnCurrentUser() {
        ResponseEntity<UserDTO> response = userController.getCurrentUser(userEntity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        //field by field comparison.
        assertEquals(userDTO.getUserId(), response.getBody().getUserId());
        assertEquals(userDTO.getEmail(), response.getBody().getEmail());
        assertEquals(userDTO.getName(), response.getBody().getName());
        assertEquals(userDTO.getRole(), response.getBody().getRole());
    }

    /**
     * Tests retrieving the current user when the user is null.
     * Verifies that the controller returns HTTP status BAD_REQUEST.
     */
    @Test
    void getCurrentUser_shouldReturnBadRequest_whenUserIsNull() {
        ResponseEntity<UserDTO> response = userController.getCurrentUser(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}