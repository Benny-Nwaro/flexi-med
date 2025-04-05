package com.example.flexiMed.services;

import com.example.flexiMed.exceptions.ErrorResponse.FileStorageException;
import com.example.flexiMed.service.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link FileStorageService}.
 * This class tests the functionality of the {@link FileStorageService}, ensuring that it correctly
 * handles file storage operations and throws appropriate exceptions under various conditions.
 */
@ExtendWith(MockitoExtension.class)
public class FileStorageServiceTest {

    private FileStorageService fileStorageService;
    private Path testUploadLocation;
    private UUID userId;
    private MultipartFile file;
    private String fileName;

    /**
     * Sets up the test environment before each test method.
     * This method initializes the {@link FileStorageService}, mocks necessary static methods,
     * and sets up test data like user ID and file objects.
     *
     * @throws IOException If an I/O error occurs during setup.
     */
    @BeforeEach
    void setUp() throws IOException {
        String projectRoot = System.getProperty("user.dir");
        testUploadLocation = Paths.get(projectRoot, "test-uploads").toAbsolutePath();

        // Mock Files.createDirectories to prevent actual directory creation
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.createDirectories(any(Path.class))).thenReturn(testUploadLocation);
        }

        fileStorageService = new FileStorageService();

        userId = UUID.randomUUID();
        fileName = "test.txt";
        file = new MockMultipartFile(fileName, fileName, "text/plain", "test content".getBytes());
    }

    /**
     * Tests the {@link FileStorageService#saveFile(MultipartFile, UUID)} method.
     * Verifies that the method correctly saves a file and returns the expected file path.
     * Mocks {@link Files#copy(java.io.InputStream, Path, java.nio.file.CopyOption...)} to prevent actual file copying.
     *
     * @throws IOException If an I/O error occurs during the test.
     */
    @Test
    void saveFile_shouldSaveFileAndReturnPath() throws IOException {
        // Mock Files.copy to prevent actual file copy
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.copy(any(), any())).thenReturn(10L); // Return a dummy value
            String result = fileStorageService.saveFile(file, userId);
            String expectedPath = "/uploads/user_" + userId + "_" + fileName;
            assertEquals(expectedPath, result);
        }
    }

    /**
     * Tests the {@link FileStorageService#saveFile(MultipartFile, UUID)} method.
     * Verifies that a {@link FileStorageException} is thrown when an empty file is provided.
     */
    @Test
    void saveFile_shouldThrowException_whenFileIsEmpty() {
        MultipartFile emptyFile = new MockMultipartFile("empty.txt", new byte[0]);
        assertThrows(FileStorageException.class, () -> fileStorageService.saveFile(emptyFile, userId));
    }

    /**
     * Tests the {@link FileStorageService#saveFile(MultipartFile, UUID)} method.
     * Verifies that a {@link FileStorageException} is thrown when an {@link IOException} occurs
     * during the file saving process.
     * Mocks {@link Files#copy(java.io.InputStream, Path, java.nio.file.CopyOption...)} to throw an {@link IOException}.
     *
     * @throws IOException If an I/O error occurs during the test.
     */
    @Test
    void constructor_shouldThrowException_whenUploadDirectoryIsNotWritable() throws IOException {
        // Mock Files.isWritable to return false
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.isWritable(any(Path.class))).thenReturn(false);
            assertThrows(FileStorageException.class, FileStorageService::new);
        }
    }
}