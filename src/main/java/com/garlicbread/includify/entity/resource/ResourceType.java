package com.garlicbread.includify.entity.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a type of resource in the application.
 * This class is mapped to the "resource_type" table in the database.
 */
@Entity
@Table(name = "resource_type", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})})
public class ResourceType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToMany(mappedBy = "resourceType", fetch = FetchType.LAZY)
  @JsonProperty("resourceId")
  private List<Resource> resources;

  @Column(nullable = false)
  @NotBlank(message = "Title is required")
  private String title;

  @Column(nullable = false)
  @NotBlank(message = "Description is required")
  private String description;

  @PreRemove
  private void preRemove() {
    if (resources != null && !resources.isEmpty()) {
      throw new IllegalStateException("Cannot delete resource type with existing resources.");
    }
  }

  /**
   * Gets the list of resource IDs associated with this resource type.
   *
   * @return a list of resource IDs; returns an empty list if there are no resources
   */
  @JsonProperty("resourceId")
  public List<String> getResourceIds() {
    if (resources == null) {
      return new ArrayList<>();
    }

    List<String> resourceIds = new ArrayList<>();
    for (Resource resource : resources) {
      resourceIds.add(resource.getId());
    }
    return resourceIds;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<Resource> getResources() {
    return resources;
  }

  public void setResources(List<Resource> resources) {
    this.resources = resources;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}

