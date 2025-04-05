package com.example.flexiMed.exceptions;

/**
 * ErrorResponse is a utility class used to represent an error response in the application.
 * It contains the status code and the error message.
 */
public class ErrorResponse {

    private int status;
    private String message;

    /**
     * Constructs an ErrorResponse with the specified status code and message.
     *
     * @param status the HTTP status code (e.g., 404, 500)
     * @param message the error message to be returned in the response
     */
    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }

    /**
     * Exception for when a user does not exist in the system.
     */
    public static class NoSuchUserExistsException extends RuntimeException {
        private String message;

        public NoSuchUserExistsException(String msg) {
            super(msg);
            this.message = msg;
        }

        @Override
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    /**
     * Exception for when a user already exists in the system.
     */
    public static class UserAlreadyExistsException extends RuntimeException {
        private String message;

        public UserAlreadyExistsException(String msg) {
            super(msg);
            this.message = msg;
        }

        @Override
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    /**
     * Exception for invalid requests, typically due to bad input or missing parameters.
     */
    public static class InvalidRequestException extends RuntimeException {
        public InvalidRequestException(String message) {
            super(message);
        }
    }

    /**
     * Exception for when an ambulance is not found in the system.
     */
    public static class NoSuchAmbulanceExistsException extends RuntimeException {
        public NoSuchAmbulanceExistsException(String message) {
            super(message);
        }
    }

    /**
     * Exception for optimistic locking failure when updating ambulance records.
     */
    public static class AmbulanceOptimisticLockException extends RuntimeException {
        public AmbulanceOptimisticLockException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Exception for when no available ambulances are found.
     */
    public static class NoAvailableAmbulanceException extends RuntimeException {
        public NoAvailableAmbulanceException(String message) {
            super(message);
        }
    }

    /**
     * Exception for when an invalid ambulance ID is provided.
     */
    public static class InvalidAmbulanceIdException extends RuntimeException {
        public InvalidAmbulanceIdException(String message) {
            super(message);
        }
    }

    /**
     * Exception for failure in sending notifications, like SMS or email.
     */
    public static class NotificationFailedException extends RuntimeException {
        public NotificationFailedException(String message) {
            super(message);
        }

        public NotificationFailedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Exception for when a patient record is not found in the system.
     */
    public static class PatientRecordNotFoundException extends RuntimeException {
        public PatientRecordNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Exception for when saving a patient record fails.
     */
    public static class PatientRecordSaveFailedException extends RuntimeException {
        public PatientRecordSaveFailedException(String message) {
            super(message);
        }

        public PatientRecordSaveFailedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Exception for when a request is not found in the system.
     */
    public static class RequestNotFoundException extends RuntimeException {
        public RequestNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Exception for when an ambulance is not available for service.
     */
    public static class AmbulanceNotAvailableException extends RuntimeException {
        public AmbulanceNotAvailableException(String message) {
            super(message);
        }
    }

    /**
     * Exception for when service history is not found in the system.
     */
    public static class ServiceHistoryNotFoundException extends RuntimeException {
        public ServiceHistoryNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Exception for failures related to AI API requests (e.g., AI model inference).
     */
    public static class AiApiRequestException extends RuntimeException {
        public AiApiRequestException(String message) {
            super(message);
        }
    }

    /**
     * Exception for errors related to file storage, such as saving or retrieving files.
     */
    public static class FileStorageException extends RuntimeException {
        public FileStorageException(String message) {
            super(message);
        }

        public FileStorageException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Exception for OAuth2 authentication errors.
     */
    public static class OAuth2AuthenticationException extends RuntimeException {
        public OAuth2AuthenticationException(String message) {
            super(message);
        }

        public OAuth2AuthenticationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
