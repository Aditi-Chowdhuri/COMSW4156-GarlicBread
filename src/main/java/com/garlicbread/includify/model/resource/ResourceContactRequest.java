package com.garlicbread.includify.model.resource;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ResourceContactRequest extends ResourceTypeRequest {

  @NotBlank(message = "Contact name is required")
  private String name;

  @NotBlank(message = "Latitude is required")
  private String latitude;

  @NotBlank(message = "Longitude is required")
  private String longitude;

  @NotBlank(message = "Address is required")
  private String address;

  private String phone;

  @NotNull(message = "Distance is required")
  private Double distance; // distance in miles

  // Getters and Setters
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Double getDistance() {
    return distance;
  }

  public void setDistance(Double distance) {
    this.distance = distance;
  }
}
