package dev.posadskiy.skillrepeat;

import dev.posadskiy.skillrepeat.dto.RestException;
import dev.posadskiy.skillrepeat.exception.SessionDoesNotExistException;
import dev.posadskiy.skillrepeat.exception.SessionExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(SessionDoesNotExistException.class)
	public ResponseEntity<?> sessionDoesNotExistExceptionHandler() {
		RestException restException = new RestException("Request error", 1, "Session does not exist. Please, log in again");
		return new ResponseEntity<>(restException, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(SessionExpiredException.class)
	public ResponseEntity<?> sessionExpiredExceptionHandler() {
		RestException restException = new RestException("Request error", 2, "Session expired. Please, log in");
		return new ResponseEntity<>(restException, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globalExceptionHandler() {
		RestException restException = new RestException("Undefined exception", 0, "Try to repeat your action later");
		return new ResponseEntity<>(restException, HttpStatus.BAD_REQUEST);
	}
}