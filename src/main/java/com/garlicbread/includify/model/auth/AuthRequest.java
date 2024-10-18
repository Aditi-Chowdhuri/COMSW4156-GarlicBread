package com.garlicbread.includify.model.auth;

import jakarta.validation.constraints.NotBlank;

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
}