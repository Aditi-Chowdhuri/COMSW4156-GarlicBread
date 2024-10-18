package com.garlicbread.includify.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class that provides common methods for the application.
 */
public class Utils {

  /**
   * Hashes the provided plain text password using BCrypt hashing algorithm.
   *
   * @param password the plain text password to be hashed
   * @return the hashed password as a {@link String}
   */
  public static String hashPassword(final String password) {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    return passwordEncoder.encode(password);
  }
}
