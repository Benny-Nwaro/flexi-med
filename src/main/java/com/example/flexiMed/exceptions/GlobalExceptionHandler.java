package com.example.flexiMed.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler is a centralized exception handling mechanism for the FlexiMed API.
 * It catches exceptions thrown by controllers and returns a structured error response to the client.
 * This class uses the @RestControllerAdvice annotation, which is a convenience for
 * @ControllerAdvice and @ResponseBody, to handle exceptions globally and send JSON responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Checks whether the incoming request is for the Swagger UI or API docs.
     *
     * @param request the HttpServletRequest object representing the incoming request.
     * @return true if the request is for Swagger API documentation, false otherwise.
     */
    private boolean isSwaggerRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/v3/api-docs") || uri.startsWith("/swagger-ui");
    }

    /**
     * Handles the exception thrown when a user does not exist in the system.
     * It returns a 404 Not Found status with an appropriate error message.
     *
     * @param ex the exception to handle.
     * @return a ResponseEntity containing the error response.
     */
    @ExceptionHandler(ErrorResponse.NoSuchUserExistsException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchUserExistsException(ErrorResponse.NoSuchUserExistsException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles the exception thrown when a user already exists in the system.
     * It returns a 409 Conflict status with an appropriate error message.
     *
     * @param ex the exception to handle.
     * @return a ResponseEntity containing the error response.
     */
    @ExceptionHandler(ErrorResponse.UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(ErrorResponse.UserAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Handles generic exceptions that do not match specific exception handlers.
     * It returns a 500 Internal Server Error status with a default error message.
     * If the request is for Swagger, it includes "Swagger Error" in the message.
     *
     * @param ex the exception to handle.
     * @param request the HttpServletRequest object representing the incoming request.
     * @return a ResponseEntity containing the error response.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        if (isSwaggerRequest(request)) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Swagger Error: " + ex.getMessage());
        }
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
    }

    /**
     * Handles the exception thrown when the request is invalid.
     * It returns a 400 Bad Request status with an appropriate error message.
     *
     * @param ex the exception to handle.
     * @return a ResponseEntity containing the error response.
     */
    @ExceptionHandler(ErrorResponse.InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(ErrorResponse.InvalidRequestException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // New exception handlers for ambulance-related errors

    /**
     * Handles the exception thrown when no such ambulance exists.
     * It returns a 404 Not Found status with an appropriate error message.
     *
     * @param ex the exception to handle.
     * @return a ResponseEntity containing the error response.
     */
    @ExceptionHandler(ErrorResponse.NoSuchAmbulanceExistsException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchAmbulanceExistsException(ErrorResponse.NoSuchAmbulanceExistsException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles the exception thrown when there is an optimistic lock error related to ambulances.
     * It returns a 409 Conflict status with an appropriate error message.
     *
     * @param ex the exception to handle.
     * @return a ResponseEntity containing the error response.
     */
    @ExceptionHandler(ErrorResponse.AmbulanceOptimisticLockException.class)
    public ResponseEntity<ErrorResponse> handleAmbulanceOptimisticLockException(ErrorResponse.AmbulanceOptimisticLockException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Handles the exception thrown when no available ambulance is found.
     * It returns a 404 Not Found status with an appropriate error message.
     *
     * @param ex the exception to handle.
     * @return a ResponseEntity containing the error response.
     */
    @ExceptionHandler(ErrorResponse.NoAvailableAmbulanceException.class)
    public ResponseEntity<ErrorResponse> handleNoAvailableAmbulanceException(ErrorResponse.NoAvailableAmbulanceException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles the exception thrown when an invalid ambulance ID is provided.
     * It returns a 400 Bad Request status with an appropriate error message.
     *
     * @param ex the exception to handle.
     * @return a ResponseEntity containing the error response.
     */
    @ExceptionHandler(ErrorResponse.InvalidAmbulanceIdException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAmbulanceIdException(ErrorResponse.InvalidAmbulanceIdException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Handles the exception thrown when notification fails.
     * It returns a 500 Internal Server Error status with an appropriate error message.
     *
     * @param ex the exception to handle.
     * @return a ResponseEntity containing the error response.
     */
    @ExceptionHandler(ErrorResponse.NotificationFailedException.class)
    public ResponseEntity<ErrorResponse> handleNotificationException(ErrorResponse.NotificationFailedException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    /**
     * Handles the exception thrown when a request is not found.
     * It returns a 404 Not Found status with an appropriate error message.
     *
     * @param ex the exception to handle.
     * @return a ResponseEntity containing the error response.
     */
    @ExceptionHandler(ErrorResponse.RequestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRequestNotFoundException(ErrorResponse.RequestNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles the exception thrown when an ambulance is not available.
     * It returns a 404 Not Found status with an appropriate error message.
     *
     * @param ex the exception to handle.
     * @return a ResponseEntity containing the error response.
     */
    @ExceptionHandler(ErrorResponse.AmbulanceNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleAmbulanceNotAvailableException(ErrorResponse.AmbulanceNotAvailableException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles the exception thrown when service history is not found.
     * It returns a 404 Not Found status with an appropriate error message.
     *
     * @param ex the exception to handle.
     * @return a ResponseEntity containing the error response.
     */
    @ExceptionHandler(ErrorResponse.ServiceHistoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleServiceHistoryNotFoundException(ErrorResponse.ServiceHistoryNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles the exception thrown when file storage fails.
     * It returns a 400 Bad Request status with an appropriate error message.
     *
     * @param ex the exception to handle.
     * @return a ResponseEntity containing the error response.
     */
    @ExceptionHandler(ErrorResponse.FileStorageException.class)
    public ResponseEntity<ErrorResponse> handleFileStorageException(ErrorResponse.FileStorageException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * A helper method to construct the error response.
     *
     * @param status the HTTP status code to return.
     * @param message the error message to include in the response.
     * @return a ResponseEntity containing the error response.
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message);
        return ResponseEntity.status(status).body(errorResponse);
    }
}
