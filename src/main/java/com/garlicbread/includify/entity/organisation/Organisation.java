package com.garlicbread.includify.entity.organisation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.util.Utils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * Represents an organization entity in the system.
 * An organization is associated with resources.
 */
@Entity
@Table(name = "organisation", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class Organisation {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(nullable = false)
  @NotBlank(message = "Name is required")
  private String name;

  @Column(nullable = false, unique = true)
  @Email(message = "Invalid email format")
  @NotBlank(message = "Email is required")
  private String email;

  @Column(nullable = false)
  @NotBlank(message = "Password is required")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  private String description;

  @Column(nullable = false)
  @NotBlank(message = "Latitude is required")
  private String latitude;

  @Column(nullable = false)
  @NotBlank(message = "Longitude is required")
  private String longitude;

  @Column(nullable = false)
  @NotBlank(message = "Address is required")
  private String address;

  // one organisation can have many resources
  @OneToMany(mappedBy = "organisation", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private List<Resource> resources;

  public void setId(String id) {
    this.id = id;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String plainPassword) {
    this.password = Utils.hashPassword(plainPassword);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public List<Resource> getResources() {
    return resources;
  }

  public void setPasswordWithoutHash(String password) {
    this.password = password;
  }
}
