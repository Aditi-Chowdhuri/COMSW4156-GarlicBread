package com.garlicbread.includify.entity.resource.types;

import com.garlicbread.includify.entity.resource.Resource;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;

/**
 * Represents a tool resource in the system, associated with a specific resource.
 * Each ResourceTool entity can be mapped to only one Resource.
 */
@Entity
@Table(name = "resource_tool")
public class ResourceTool {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  // one resource_tool can be mapped to only one resource
  @OneToOne
  @MapsId
  @JoinColumn(name = "id")
  private Resource resource;

  @Column(nullable = false)
  @Min(value = 1, message = "Resource count should be more than one")
  private int count;

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

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}

