package com.garlicbread.includify.exception;

import org.springframework.http.HttpStatus;

/**
 * Represents an API error response.
 * This class encapsulates the details of an error that occurs
 * during the execution of an API request.
 */
public class ApiError {

  private HttpStatus status;
  private String message;
  private String path;

  /**
   * Constructs a new ApiError with the specified details.
   *
   * @param status the HTTP status associated with the error
   * @param message a human-readable message providing more details about the error
   * @param path the request path that generated the error
   */
  public ApiError(HttpStatus status, String message, String path) {
    this.status = status;
    this.message = message;
    this.path = path;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public void setStatus(HttpStatus status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
