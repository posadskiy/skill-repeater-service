package com.posadskiy.skillrepeater.core.exception.handler;

import com.posadskiy.skillrepeater.core.exception.AuthException;
import com.posadskiy.skillrepeater.core.exception.ErrorMessage;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {AuthException.class, ExceptionHandler.class})
public class UserAlreadyExistsExceptionHandler implements ExceptionHandler<AuthException, HttpResponse> {

    @SuppressWarnings("rawtypes")
    @Override
    public HttpResponse handle(HttpRequest request, AuthException exception) {
        var message = new ErrorMessage(false, exception.getMessage());

        return HttpResponse
            .serverError(message)
            .status(HttpStatus.BAD_REQUEST);
    }
}
