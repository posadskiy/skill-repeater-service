package dev.posadskiy.skillrepeat;

import dev.posadskiy.skillrepeat.dto.RestException;
import dev.posadskiy.skillrepeat.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(SessionDoesNotExistException.class)
	public ResponseEntity<?> sessionDoesNotExistExceptionHandler(SessionDoesNotExistException exception) {
		log.debug("SessionDoesNotExistException", exception);
		RestException restException = new RestException("Request error", 1, "Session does not exist. Please, log in again");
		return new ResponseEntity<>(restException, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(SessionExpiredException.class)
	public ResponseEntity<?> sessionExpiredExceptionHandler(SessionExpiredException exception) {
		log.debug("SessionExpiredException", exception);
		RestException restException = new RestException("Request error", 2, "Session expired. Please, log in");
		return new ResponseEntity<>(restException, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UserDoesNotExistException.class)
	public ResponseEntity<?> userDoesNotExistExceptionHandler(UserDoesNotExistException exception) {
		log.debug("UserDoesNotExistException", exception);
		RestException restException = new RestException("Request error", 3, "User does not exist. Please, check your request");
		return new ResponseEntity<>(restException, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UserPasswordDoesNotMatchException.class)
	public ResponseEntity<?> userPasswordDoesNotMatchExceptionHandler(UserPasswordDoesNotMatchException exception) {
		log.debug("UserPasswordDoesNotMatchException", exception);
		RestException restException = new RestException("Request error", 3, "Password doesn't match");
		return new ResponseEntity<>(restException, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UserRolesDoesNotExistException.class)
	public ResponseEntity<?> userRolesDoesNotExistExceptionHandler(UserRolesDoesNotExistException exception) {
		log.debug("UserRolesDoesNotExistException", exception);
		RestException restException = new RestException("Request error", 4, "Your account doesn't have any roles. Please, contact to our mail");
		return new ResponseEntity<>(restException, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(PermissionIsAbsentException.class)
	public ResponseEntity<?> permissionIsAbsentExceptionHandler(PermissionIsAbsentException exception) {
		log.debug("PermissionIsAbsentException", exception);
		RestException restException = new RestException("Request error", 5, "Your haven't permission for access");
		return new ResponseEntity<>(restException, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(PermissionForGetAnotherUserIsAbsentException.class)
	public ResponseEntity<?> permissionForGetAnotherUserIsAbsentExceptionHandler(PermissionForGetAnotherUserIsAbsentException exception) {
		log.debug("PermissionForGetAnotherUserIsAbsentException", exception);
		RestException restException = new RestException("Request error", 6, "Your haven't permission for access to another user");
		return new ResponseEntity<>(restException, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UserValidationException.class)
	public ResponseEntity<?> userValidationExceptionHandler(UserValidationException exception) {
		log.debug("UserValidationException", exception);
		RestException restException = new RestException("Validation error", 7, exception.getReason());
		return new ResponseEntity<>(restException, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UserAlreadyExistException.class)
	public ResponseEntity<?> userAlreadyExistExceptionHandler(Exception exception) {
		log.debug("UserAlreadyExistException", exception);
		RestException restException = new RestException("Request error", 8, "User login already exist in system. Try to use different login");
		return new ResponseEntity<>(restException, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globalExceptionHandler(Exception exception) {
		log.error("Undefined exception", exception);
		RestException restException = new RestException("Undefined exception", 0, "Try to repeat your action later");
		return new ResponseEntity<>(restException, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}