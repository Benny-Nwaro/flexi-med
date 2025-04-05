package com.example.flexiMed.services;

import com.example.flexiMed.dto.UserDTO;
import com.example.flexiMed.enums.Role;
import com.example.flexiMed.exceptions.ErrorResponse;
import com.example.flexiMed.model.UserEntity;
import com.example.flexiMed.repository.UserRepository;
import com.example.flexiMed.service.FileStorageService;
import com.example.flexiMed.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the UserService.
 * This class tests the functionality of the UserService, ensuring that it correctly
 * interacts with the UserRepository, PasswordEncoder, and FileStorageService,
 * and handles various user-related operations such as retrieving, saving, updating,
 * and deleting user profiles.
 */
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;

    /**
     * Sets up the test environment before each test method.
     * Initializes the necessary objects and mocks, including a sample UserEntity.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID());
        userEntity.setEmail("test@example.com");
        userEntity.setName("John Doe");
        userEntity.setPhoneNumber("1234567890");
        userEntity.setPassword("password");
        userEntity.setRole(Role.USER);
    }

    /**
     * Tests retrieving a user by ID when the user exists in the repository.
     * Verifies that the correct UserDTO is returned and the repository's findById method is called.
     */
    @Test
    void testGetUserById_whenUserExists() {
        when(userRepository.findById(userEntity.getUserId())).thenReturn(Optional.of(userEntity));

        UserDTO result = userService.getUserById(userEntity.getUserId());

        assertEquals(userEntity.getEmail(), result.getEmail());
        verify(userRepository).findById(userEntity.getUserId());
    }

    /**
     * Tests retrieving a user by ID when the user does not exist in the repository.
     * Verifies that a NoSuchUserExistsException is thrown.
     */
    @Test
    void testGetUserById_whenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ErrorResponse.NoSuchUserExistsException.class, () -> userService.getUserById(userId));
    }

    /**
     * Tests saving a new user to the repository.
     * Verifies that the password is encoded, the user is saved, and the saved user's details are correct.
     */
    @Test
    void testSaveUser_whenUserIsNew() {
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity savedUser = userService.saveUser(userEntity);

        assertEquals(userEntity.getEmail(), savedUser.getEmail());
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(userEntity);
    }

    /**
     * Tests saving a user when a user with the same email already exists in the repository.
     * Verifies that a UserAlreadyExistsException is thrown.
     */
    @Test
    void testSaveUser_whenUserAlreadyExists() {
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));

        assertThrows(ErrorResponse.UserAlreadyExistsException.class, () -> userService.saveUser(userEntity));
    }

    /**
     * Tests updating a user profile with an image upload.
     * Verifies that the file is saved, the user's profile image URL is updated, and the updated UserDTO is returned.
     */
    @Test
    void testUpdateUserProfile_withImageUpload() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(userRepository.findById(userEntity.getUserId())).thenReturn(Optional.of(userEntity));
        when(fileStorageService.saveFile(mockFile, userEntity.getUserId())).thenReturn("http://image.url");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDTO updatedDTO = new UserDTO();
        updatedDTO.setName("New Name");
        updatedDTO.setEmail("new@example.com");
        updatedDTO.setPhoneNumber("999999999999999");

        UserDTO result = userService.updateUserProfile(userEntity.getUserId(), updatedDTO, mockFile);

        assertEquals("New Name", result.getName());
        assertEquals("http://image.url", userEntity.getProfileImageUrl());
    }

    /**
     * Tests deleting a user when the user exists in the repository.
     * Verifies that the user is deleted and no exception is thrown.
     */
    @Test
    void testDeleteUser_whenExists() {
        when(userRepository.findById(userEntity.getUserId())).thenReturn(Optional.of(userEntity));
        doNothing().when(userRepository).deleteById(userEntity.getUserId());

        assertDoesNotThrow(() -> userService.deleteUser(userEntity.getUserId()));
        verify(userRepository).deleteById(userEntity.getUserId());
    }

    /**
     * Tests deleting a user when the user does not exist in the repository.
     * Verifies that a NoSuchUserExistsException is thrown.
     */
    @Test
    void testDeleteUser_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ErrorResponse.NoSuchUserExistsException.class, () -> userService.deleteUser(id));
    }
}