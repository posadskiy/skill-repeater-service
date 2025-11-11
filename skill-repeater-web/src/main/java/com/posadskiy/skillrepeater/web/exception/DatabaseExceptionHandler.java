package com.posadskiy.skillrepeater.web.exception;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Error;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

@Singleton
@Requires(classes = {SQLException.class})
public class DatabaseExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseExceptionHandler.class);

    @Error(global = true)
    public HttpResponse<ErrorResponse> handleSQLException(SQLException ex, HttpRequest request) {
        logger.error("Database error: {}", ex.getMessage(), ex);

        ErrorResponse error = new ErrorResponse(
            "Database operation failed",
            "DATABASE_ERROR",
            request.getPath()
        );

        return HttpResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
} 
