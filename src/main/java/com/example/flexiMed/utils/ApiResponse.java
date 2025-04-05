package com.example.flexiMed.utils;

/**
 * A generic response object used to encapsulate data and messages for API responses.
 * This class provides a structured way to return data along with a descriptive message.
 *
 * @param <T> The type of data to be included in the response.
 */
public class ApiResponse<T> {

    /**
     * The data to be included in the response.
     */
    private T data;

    /**
     * A descriptive message related to the response.
     */
    private String message;

    /**
     * Constructs an ApiResponse object with the given data and message.
     *
     * @param data    The data to be included in the response.
     * @param message A descriptive message related to the response.
     */
    public ApiResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }

    /**
     * Gets the data included in the response.
     *
     * @return The data.
     */
    public T getData() {
        return data;
    }

    /**
     * Sets the data to be included in the response.
     *
     * @param data The data to set.
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Gets the message related to the response.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message related to the response.
     *
     * @param message The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}