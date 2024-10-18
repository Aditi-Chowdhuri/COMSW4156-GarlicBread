package com.garlicbread.includify.exception;

/**
 * Exception thrown when a requested resource is not found.
 * This is a runtime exception that indicates that a specific resource
 * could not be located in the system.
 */
public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(message);
  }
}

