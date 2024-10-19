package com.garlicbread.includify.model.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents an authentication request containing user credentials.
 */
public class AuthRequest {

  @NotBlank(message = "email is required")
  private String email;

  @NotBlank(message = "password is required")
  private String password;

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public void setEmail(String email) {this.email = email;}

  public void setPassword(String password) {this.password = password;}
}