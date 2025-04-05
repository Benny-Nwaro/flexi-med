package com.example.flexiMed.service;

import com.example.flexiMed.dto.UserDTO;
import com.example.flexiMed.enums.Role;
import com.example.flexiMed.exceptions.ErrorResponse;
import com.example.flexiMed.mapper.UserMapper;
import com.example.flexiMed.model.UserEntity;
import com.example.flexiMed.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class that handles operations related to user management,
 * including user retrieval, creation, updating, and deletion.
 * Implements {@link UserDetailsService} for Spring Security integration.
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    /**
     * Constructor for {@link UserService} class.
     *
     * @param userRepository    the repository used to interact with the user database.
     * @param passwordEncoder   the password encoder used to encode user passwords.
     * @param fileStorageService the service used for handling user profile image uploads.
     */
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder,
                       FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a list of {@link UserDTO} objects representing all users in the system.
     */
    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the unique identifier of the user.
     * @return a {@link UserDTO} representing the user with the given ID.
     * @throws ErrorResponse.NoSuchUserExistsException if the user is not found.
     */
    public UserDTO getUserById(UUID id) {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new ErrorResponse.NoSuchUserExistsException("User does not exist"));
        return UserMapper.toDTO(user);
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the email address of the user.
     * @return a {@link UserDTO} representing the user with the given email.
     * @throws ErrorResponse.NoSuchUserExistsException if the user is not found.
     */
    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(UserMapper::toDTO).orElseThrow(
                () -> new ErrorResponse.NoSuchUserExistsException("User does not exist"));
    }

    /**
     * Retrieves a user by their phone number.
     *
     * @param phoneNumber the phone number of the user.
     * @return an {@link Optional} containing a {@link UserDTO} if the user exists with the given phone number.
     */
    public Optional<UserDTO> getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).map(UserMapper::toDTO);
    }

    /**
     * Loads a user by their username (email).
     * This method is used by Spring Security during authentication.
     *
     * @param email the email address of the user.
     * @return a {@link UserEntity} representing the user.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserEntity loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    /**
     * Retrieves a list of users by their role.
     *
     * @param role the role to filter users by (e.g., ADMIN, USER).
     * @return a list of {@link UserDTO} objects representing users with the given role.
     */
    public List<UserDTO> getUsersByRole(Role role) {
        List<UserEntity> users = userRepository.findByRole(role);
        return users.stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Saves a new user to the database.
     *
     * @param user the {@link UserEntity} object representing the user to be saved.
     * @return the saved {@link UserEntity}.
     * @throws ErrorResponse.UserAlreadyExistsException if the user with the given email already exists.
     */
    public UserEntity saveUser(UserEntity user) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new ErrorResponse.UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(""); // Empty password for OAuth users
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    /**
     * Updates a user's profile information and optionally their profile image.
     *
     * @param userId       the unique identifier of the user to be updated.
     * @param updatedUser  the updated user data as a {@link UserDTO}.
     * @param profileImage the new profile image file to be uploaded (optional).
     * @return the updated {@link UserDTO}.
     * @throws ErrorResponse.NoSuchUserExistsException if the user is not found.
     */
    @Transactional
    public UserDTO updateUserProfile(UUID userId, UserDTO updatedUser, MultipartFile profileImage) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorResponse.NoSuchUserExistsException("User not found."));

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPhoneNumber(updatedUser.getPhoneNumber());

        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = fileStorageService.saveFile(profileImage, userId);
            user.setProfileImageUrl(imageUrl);
        }

        userRepository.save(user);
        return UserMapper.toDTO(user);
    }

    /**
     * Deletes a user from the database.
     *
     * @param id the unique identifier of the user to be deleted.
     * @throws ErrorResponse.NoSuchUserExistsException if the user is not found.
     */
    public void deleteUser(UUID id) {
        userRepository.findById(id).orElseThrow(
                () -> new ErrorResponse.NoSuchUserExistsException("User does not exist"));
        userRepository.deleteById(id);
    }
}
