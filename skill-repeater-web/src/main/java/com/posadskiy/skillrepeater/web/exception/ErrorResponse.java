package com.posadskiy.skillrepeater.web.exception;

import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDateTime;

/**
 * Custom error response for detailed error information
 */
@Serdeable
public class ErrorResponse {

    private final String message;
    private final String errorCode;
    private final LocalDateTime timestamp;
    private final String path;

    public ErrorResponse(String message, String errorCode, String path) {
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }
} 
