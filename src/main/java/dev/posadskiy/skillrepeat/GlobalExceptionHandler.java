package dev.posadskiy.skillrepeat;

import dev.posadskiy.skillrepeat.dto.RestException;
import dev.posadskiy.skillrepeat.exception.*;
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

	@ExceptionHandler(UserDoesNotExistException.class)
	public ResponseEntity<?> userDoesNotExistExceptionHandler() {
		RestException restException = new RestException("Request error", 3, "User does not exist. Please, check your request");
		return new ResponseEntity<>(restException, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserPasswordDoesNotMatchException.class)
	public ResponseEntity<?> userPasswordDoesNotMatchExceptionHandler() {
		RestException restException = new RestException("Request error", 3, "Password doesn't match");
		return new ResponseEntity<>(restException, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserRolesDoesNotExistException.class)
	public ResponseEntity<?> userRolesDoesNotExistExceptionHandler() {
		RestException restException = new RestException("Request error", 4, "Your account doesn't have any roles. Please, contact to our mail");
		return new ResponseEntity<>(restException, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(PermissionIsAbsentException.class)
	public ResponseEntity<?> permissionIsAbsentExceptionHandler() {
		RestException restException = new RestException("Request error", 5, "Your haven't permission for access");
		return new ResponseEntity<>(restException, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(PermissionForGetAnotherUserIsAbsentException.class)
	public ResponseEntity<?> permissionForGetAnotherUserIsAbsentExceptionHandler() {
		RestException restException = new RestException("Request error", 6, "Your haven't permission for access to another user");
		return new ResponseEntity<>(restException, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globalExceptionHandler(Exception exception) {
		RestException restException = new RestException("Undefined exception", 0, "Try to repeat your action later");
		return new ResponseEntity<>(restException, HttpStatus.BAD_REQUEST);
	}
}