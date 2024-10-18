package com.garlicbread.includify.model.resource;

import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

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
}
