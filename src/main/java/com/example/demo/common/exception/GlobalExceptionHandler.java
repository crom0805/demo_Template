package com.example.demo.common.exception;

import com.example.demo.common.api.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"com.example.demo"})
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicatedUserException.class)
	public ResponseEntity<ApiResult<?>> handleDuplicatedUserException(DuplicatedUserException e) {
		return ResponseEntity.status(e.getError().getStatus()).body(ApiResult.createError(e.getError().getMessage()));
	}

	@ExceptionHandler(EmptyTokenException.class)
	public ResponseEntity<ApiResult<?>> emptyTokenException(EmptyTokenException e) {
		return ResponseEntity.status(e.getError().getStatus()).body(ApiResult.createError(e.getError().getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResult<?>> handleValidationExceptions(BindingResult bindingResult) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResult.createFail(bindingResult));
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiResult<?>> badCredentialsExceptions() {
		return ResponseEntity.status(ExceptionEnum.FORBIDDEN.getStatus()).body(ApiResult.createError(ExceptionEnum.FORBIDDEN.getMessage()));
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiResult<?>> userNotFoundException(UserNotFoundException e) {
		return ResponseEntity.status(e.getError().getStatus()).body(ApiResult.createError(e.getError().getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResult<?>> exception(RuntimeException exception) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResult.createError(exception.getMessage()));
	}
}
