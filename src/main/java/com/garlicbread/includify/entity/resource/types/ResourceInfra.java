package com.garlicbread.includify.entity.resource.types;

import com.garlicbread.includify.entity.resource.Resource;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/**
 * Entity representing the infrastructure details of a resource.
 * This class maps the infrastructure-related details such as location
 * (latitude, longitude, address) to a specific resource.
 */
@Entity
@Table(name = "resource_infra")
public class ResourceInfra {

  @Id
  private String id;

  // one resource_infra can be mapped to only one resource
  @OneToOne
  @MapsId
  @JoinColumn(name = "id")
  private Resource resource;

  @Column(nullable = false)
  @NotBlank(message = "Infra latitude is required")
  private String latitude;

  @Column(nullable = false)
  @NotBlank(message = "Infra longitude is required")
  private String longitude;

  @Column(nullable = false)
  @NotBlank(message = "Infra address is required")
  private String address;

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
}

