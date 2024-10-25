package com.garlicbread.includify.entity.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.garlicbread.includify.entity.resource.Resource;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a category of users in the application.
 * This entity is mapped to the 'user_category' table in the database.
 */
@Entity
@Table(name = "user_category", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})})
public class UserCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
  @JsonProperty("userId")
  private List<User> users;

  @ManyToMany(mappedBy = "targetUserCategory", fetch = FetchType.LAZY)
  @JsonProperty("resourceId")
  private List<Resource> resources;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @PreRemove
  private void preRemove() {
    if (users != null && !users.isEmpty()) {
      throw new IllegalStateException("Cannot delete user category with existing users.");
    }
  }

  /**
   * Gets the list of user IDs associated with this category.
   *
   * @return a list of user IDs
   */
  @JsonProperty("userId")
  public List<String> getUserIds() {
    if (users == null) {
      return new ArrayList<>();
    }
    List<String> userIds = new ArrayList<>();
    for (User user : users) {
      userIds.add(user.getId());
    }
    return userIds;
  }

  /**
   * Gets the list of resource IDs associated with this category.
   *
   * @return a list of resource IDs
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

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
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

