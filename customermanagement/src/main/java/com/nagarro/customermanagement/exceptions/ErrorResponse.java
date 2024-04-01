package com.nagarro.customermanagement.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class representing an error response to be sent in case of an exception.
 */
public class ErrorResponse {

    // Error message to describe the issue
    private final String message;

    // HTTP status code indicating the type of error
    private final int code;

    // Timestamp of when the error occurred
    private final String timestamp;

    /**
     * Constructs an ErrorResponse object with the specified HttpStatus and message.
     *
     * @param status  the HTTP status code indicating the type of error
     * @param message the detail message describing the error
     */
    public ErrorResponse(HttpStatus status, String message) {
        this.code = status.value();
        this.message = message;

        // Format the timestamp to a human-readable string
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("d'-'MMMM'-'yyyy HH:mm:ss"));
    }

    /**
     * Gets the error message.
     *
     * @return the error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the HTTP status code.
     *
     * @return the HTTP status code
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets the timestamp of when the error occurred.
     *
     * @return the timestamp in a human-readable string format
     */
    public String getTimestamp() {
        return timestamp;
    }
}
