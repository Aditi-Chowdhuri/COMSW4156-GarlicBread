package com.garlicbread.includify.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class UserRequest {

  @NotBlank(message = "Name is required")
  private String name;

  @Min(value = 1, message = "Age must be greater than 0")
  private int age;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Password is required")
  private String password;

  private List<String> categoryIds;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<String> getCategoryIds() {
    return categoryIds != null ? new ArrayList<>(categoryIds) : new ArrayList<>();
  }

  public void setCategoryIds(List<String> categoryIds) {
    this.categoryIds = categoryIds;
  }
}
