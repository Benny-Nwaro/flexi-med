package com.example.flexiMed.utils;

/**
 * Utility class for file-related operations.
 * Provides methods for sanitizing filenames to ensure they are safe for file systems.
 */
public class FileUtils {

    /**
     * Sanitizes a filename by replacing or removing potentially dangerous characters.
     * This method replaces characters that are not alphanumeric, periods, or hyphens with underscores.
     *
     * @param filename The filename to sanitize.
     * @return The sanitized filename.
     */
    public static String sanitizeFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9.\\-]", "_");
    }
}