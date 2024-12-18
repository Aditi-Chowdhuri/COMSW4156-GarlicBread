package com.garlicbread.includify.model.resource;

import com.garlicbread.includify.entity.resource.types.ResourceContact;
import com.garlicbread.includify.entity.resource.types.ResourceInfra;
import com.garlicbread.includify.entity.resource.types.ResourceService;
import com.garlicbread.includify.entity.resource.types.ResourceTool;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a request for a resource within the application.
 * This class contains the necessary fields and validations
 * required to create or update a resource request.
 */
public class ResourceRequest {

  @NotBlank(message = "Organisation id is required")
  private String organisationId;

  private List<String> resourceTypeIds;

  private List<String> targetUserCategoryIds;

  @NotBlank(message = "Title is required")
  private String title;

  @NotBlank(message = "Description is required")
  private String description;

  @NotBlank(message = "Usage instructions are required")
  private String usageInstructions;

  @Valid
  private ResourceContact resourceContact;

  @Valid
  private ResourceTool resourceTool;

  @Valid
  private ResourceInfra resourceInfra;

  @Valid
  private ResourceService resourceService;

  // Getters and Setters
  public String getOrganisationId() {
    return organisationId;
  }

  public void setOrganisationId(String organisationId) {
    this.organisationId = organisationId;
  }

  public List<String> getResourceTypeIds() {
    return resourceTypeIds != null ? new ArrayList<>(resourceTypeIds) : new ArrayList<>();
  }

  public void setResourceTypeIds(List<String> resourceTypeIds) {
    this.resourceTypeIds = resourceTypeIds;
  }

  /**
   * Gets the list of target user category IDs associated with this resource request.
   *
   * @return a list of target user category IDs
   */
  public List<String> getTargetUserCategoryIds() {
    return targetUserCategoryIds != null ? new ArrayList<>(targetUserCategoryIds) :
        new ArrayList<>();

  }

  public void setTargetUserCategoryIds(List<String> targetUserCategoryIds) {
    this.targetUserCategoryIds = targetUserCategoryIds;
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

  public ResourceContact getResourceContact() {
    return resourceContact;
  }

  public void setResourceContact(ResourceContact resourceContact) {
    this.resourceContact = resourceContact;
  }

  public ResourceTool getResourceTool() {
    return resourceTool;
  }

  public ResourceService getResourceService() {
    return resourceService;
  }

  public ResourceInfra getResourceInfra() {
    return resourceInfra;
  }
}
