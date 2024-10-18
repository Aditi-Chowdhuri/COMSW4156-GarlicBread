package com.garlicbread.includify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * The entry point of the Includify application.
 *
 * <p>This class contains the main method which serves as the starting point
 * for the Spring Boot application.
 * It also enables method-level security using Spring Security.</p>
 *
 * @author Garlic Bread
 * @version 1.0
 */
@SpringBootApplication
@EnableMethodSecurity
public class IncludifyApplication {

  /**
   * The main method that starts the Includify application.
   *
   * @param args Command-line arguments passed to the application.
   */
  public static void main(final String[] args) {
    SpringApplication.run(IncludifyApplication.class, args);
  }

}
