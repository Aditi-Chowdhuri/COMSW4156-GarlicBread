package com.garlicbread.includify.model.resource;

import com.garlicbread.includify.entity.resource.ResourceType;
import com.garlicbread.includify.entity.resource.types.ResourceContact;
import com.garlicbread.includify.entity.resource.types.ResourceInfra;
import com.garlicbread.includify.entity.resource.types.ResourceService;
import com.garlicbread.includify.entity.resource.types.ResourceTool;
import com.garlicbread.includify.entity.user.UserCategory;
import java.util.List;

public class ResourceResponse {

  private String id;

  private String organisationId;

  private List<ResourceType> resourceType;

  private List<UserCategory> targetUserCategory;

  private String title;

  private String description;

  private String usageInstructions;

  private List<String> appointmentIds;

  private ResourceTool resourceTool;

  private ResourceService resourceService;

  private ResourceContact resourceContact;

  private ResourceInfra resourceInfra;

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

  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the list of appointment IDs associated with the resource.
   *
   * @return the list of appointment IDs
   */
  public List<String> getAppointmentIds() {
    return appointmentIds;
  }

  public void setAppointmentIds(List<String> appointmentIds) {
    this.appointmentIds = appointmentIds;
  }

  public String getOrganisationId() {
    return this.organisationId;
  }

  public void setOrganisationId(String id) {
    this.organisationId = id;
  }

  // Setter and Getter for resourceTool
  public ResourceTool getResourceTool() {
    return resourceTool;
  }

  public void setResourceTool(ResourceTool resourceTool) {
    this.resourceTool = resourceTool;
  }

  // Setter and Getter for resourceService
  public ResourceService getResourceService() {
    return resourceService;
  }

  public void setResourceService(ResourceService resourceService) {
    this.resourceService = resourceService;
  }

  // Setter and Getter for resourceContact
  public ResourceContact getResourceContact() {
    return resourceContact;
  }

  public void setResourceContact(ResourceContact resourceContact) {
    this.resourceContact = resourceContact;
  }

  // Setter and Getter for resourceInfra
  public ResourceInfra getResourceInfra() {
    return resourceInfra;
  }

  public void setResourceInfra(ResourceInfra resourceInfra) {
    this.resourceInfra = resourceInfra;
  }

}
