package com.example.flexiMed.service;

import com.example.flexiMed.exceptions.ErrorResponse.FileStorageException;
import com.example.flexiMed.utils.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadLocation;

    public FileStorageService() {
        // Dynamically resolve the project root and construct the uploads path
        String projectRoot = System.getProperty("user.dir");
        this.uploadLocation = Paths.get(projectRoot, "uploads").toAbsolutePath();

        try {
            if (!Files.exists(this.uploadLocation)) {
                Files.createDirectories(this.uploadLocation);
            }
            if (!Files.isWritable(this.uploadLocation)) {
                throw new FileStorageException("Upload directory is not writable.");
            }
        } catch (IOException e) {
            throw new FileStorageException("Could not create or validate upload directory.", e);
        }
    }

    public String saveFile(MultipartFile file, UUID userId) {
        try {
            if (file.isEmpty()) {
                throw new FileStorageException("Failed to store empty file.");
            }

            String fileName = "user_" + userId + "_" + FileUtils.sanitizeFilename(file.getOriginalFilename());
            Path destinationFile = this.uploadLocation.resolve(Paths.get(fileName)).normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.uploadLocation)) {
                throw new FileStorageException("Cannot store file outside current directory.");
            }

            Files.copy(file.getInputStream(), destinationFile);

            return "/uploads/" + fileName;

        } catch (IOException e) {
            throw new FileStorageException("Failed to store file.", e);
        }
    }
}
