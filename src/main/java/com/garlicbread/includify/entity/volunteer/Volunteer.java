package com.garlicbread.includify.entity.volunteer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.garlicbread.includify.util.Utils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Represents a volunteer entity in the system.
 */
@Entity
@Table(name = "volunteer", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class Volunteer {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(nullable = false)
  @NotBlank(message = "Name is required")
  private String name;

  @Column(nullable = false, unique = true)
  @Email(message = "Please provide email with valid format")
  @NotBlank(message = "Email is required")
  private String email;

  @Column(nullable = false)
  @NotBlank(message = "Password is required")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @Column(nullable = false)
  @Min(value = 15, message = "Age must be at least 15")
  private int age;

  @Column(nullable = false)
  @NotBlank(message = "Address is required")
  private String address;

  @Column(nullable = false)
  @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Please provide a " + "valid phone number")
  @NotBlank(message = "Phone number is required")
  private String phone;

  // Getters
  public String getEmail() {
    return email;
  }

  // Setters
  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String plainPassword) {
    this.password = Utils.hashPassword(plainPassword);
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
}
