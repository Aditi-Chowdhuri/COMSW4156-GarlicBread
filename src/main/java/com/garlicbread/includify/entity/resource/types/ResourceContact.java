package com.garlicbread.includify.entity.resource.types;

import com.garlicbread.includify.entity.resource.Resource;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

/**
 * Represents the contact information associated with a resource.
 * This entity is mapped to the 'resource_contact' table in the database.
 */
@Entity
@Table(name = "resource_contact")
public class ResourceContact {

  @Id
  private String id;

  // one resource_contact can be mapped to only one resource
  @OneToOne
  @MapsId
  @JoinColumn(name = "id")
  private Resource resource;

  @Column(nullable = false)
  @NotBlank(message = "Contact name is required")
  private String name;

  @Column(nullable = false)
  @NotBlank(message = "Contact latitude is required")
  private String latitude;

  @Column(nullable = false)
  @NotBlank(message = "Contact longitude is required")
  private String longitude;

  @Column(nullable = false)
  @NotBlank(message = "Contact address is required")
  private String address;

  @Column
  private String phone;

  @Column(nullable = false)
  @DecimalMin(value = "0.1", message = "Distance should be at least 0.1 miles")
  private double distance; // distance in miles

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Resource getResource() {
    return resource;
  }

  public void setResource(Resource resource) {
    this.resource = resource;
  }

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

  public double getDistance() {
    return distance;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }

}

