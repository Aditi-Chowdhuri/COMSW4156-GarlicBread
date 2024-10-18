package com.garlicbread.includify.entity.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.garlicbread.includify.entity.appointment.Appointment;
import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.user.UserCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;

@Entity
@Table(name = "resource", uniqueConstraints = {
    // An organisation can have at most one resource with a given
    // title
    @UniqueConstraint(columnNames = {"organisation_id", "title"})})
public class Resource {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  // many resources can belong to one organisation
  @ManyToOne
  @JoinColumn(name = "organisation_id", nullable = false)
  private Organisation organisation;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "resource_types", joinColumns = @JoinColumn(name = "resource_id"),
      inverseJoinColumns = @JoinColumn(name = "type_id"))
  private List<ResourceType> resourceType;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "resource_user_categories", joinColumns = @JoinColumn(name = "resource_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id"))
  private List<UserCategory> targetUserCategory;

  @Column(nullable = false)
  private String title;

  private String description;

  private String usageInstructions;

  @ManyToMany(mappedBy = "resources")
  private List<Appointment> appointments;


  // Getters
  public String getId() {
    return id;
  }

  public List<ResourceType> getResourceType() {
    return resourceType;
  }

  public void setResourceType(List<ResourceType> resourceType) {
    this.resourceType = resourceType;
  }

  public List<UserCategory> getTargetUserCategory() {
    return targetUserCategory;
  }

  public void setTargetUserCategory(List<UserCategory> targetUserCategory) {
    this.targetUserCategory = targetUserCategory;
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

  public String getUsageInstructions() {
    return usageInstructions;
  }

  public void setUsageInstructions(String usageInstructions) {
    this.usageInstructions = usageInstructions;
  }

  public List<Appointment> getAppointments() {
    return appointments;
  }

  public void setAppointments(List<Appointment> appointments) {
    this.appointments = appointments;
  }

  @JsonProperty("organisationId")
  public String getOrganisation() {
    return this.organisation.getId();
  }

  public void setOrganisation(Organisation organisation) {
    this.organisation = organisation;
  }
}

