package com.garlicbread.includify.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

/**
 * Centralized exception handler for the application.
 * This class handles various exceptions thrown within the application
 * and provides appropriate HTTP responses.
 */
@ControllerAdvice
public class ProjectExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                           WebRequest request) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  /**
   * Handles validation errors that occur when method arguments are invalid.
   *
   * @param ex the thrown MethodArgumentNotValidException
   * @return ResponseEntity containing a map of field errors and HTTP status
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles HttpRequestMethodNotSupportedException and returns a 405 METHOD NOT ALLOWED response.
   *
   * @param ex      the thrown HttpRequestMethodNotSupportedException
   * @param request the current web request
   * @return ResponseEntity containing ApiError and HTTP status
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public ResponseEntity<ApiError> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {

    StringBuilder message = new StringBuilder();
    message.append(ex.getMethod());
    message.append(" method is not supported for this endpoint. Supported" + " methods are: ");

    Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
    if (supportedMethods != null) {
      supportedMethods.forEach(method -> message.append(method).append(" "));
    }

    ApiError apiError =
        new ApiError(HttpStatus.METHOD_NOT_ALLOWED, message.toString(), request.getRequestURI());
    return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<String> handleAuthorizationDeniedException(
      AuthorizationDeniedException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(IllegalStateException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(DataAccessException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<String> handleDataAccessException(DataAccessException ex) {
    return new ResponseEntity<>("Database error occurred !!!", HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(SQLException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<String> handleSqlException(SQLException ex) {
    return new ResponseEntity<>("SQL error occurred !!!", HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<String> handleUnreadableMessageException(
      HttpMessageNotReadableException ex) {
    return new ResponseEntity<>("Message not readable !!!", HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
    return new ResponseEntity<>("Internal Server Error !!!", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
