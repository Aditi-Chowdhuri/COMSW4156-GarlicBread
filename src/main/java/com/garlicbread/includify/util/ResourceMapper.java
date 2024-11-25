package com.garlicbread.includify.util;

import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.entity.resource.ResourceType;
import com.garlicbread.includify.entity.resource.types.ResourceContact;
import com.garlicbread.includify.entity.resource.types.ResourceInfra;
import com.garlicbread.includify.entity.resource.types.ResourceService;
import com.garlicbread.includify.entity.resource.types.ResourceTool;
import com.garlicbread.includify.entity.user.UserCategory;
import com.garlicbread.includify.model.resource.ResourceRequest;
import com.garlicbread.includify.model.resource.ResourceResponse;
import java.util.List;

/**
 * Mapper class to map Resource Request to Resource Entity.
 */
public class ResourceMapper {

  /**
   * Maps a {@link ResourceRequest} object to a {@link Resource} entity,
   * setting the associated {@link Organisation}, {@link ResourceType}s, and
   * {@link UserCategory}s.
   * The method also populates the title, description, and usage
   * instructions from the request.
   *
   * @param request        the request object containing details to create
   *                       a new resource
   * @param organisation   the {@link Organisation} to which the resource
   *                       belongs
   * @param resourceTypes  a list of {@link ResourceType}s associated with
   *                       the resource
   * @param userCategories a list of {@link UserCategory}s that the
   *                       resource targets
   * @return a {@link Resource} entity populated with the provided data
   */
  public static Resource mapToResource(final ResourceRequest request,
                                       final Organisation organisation,
                                       final List<ResourceType> resourceTypes,
                                       final List<UserCategory> userCategories) {
    Resource resource = new Resource();
    resource.setOrganisation(organisation);
    resource.setResourceType(resourceTypes);
    resource.setTargetUserCategory(userCategories);
    resource.setTitle(request.getTitle());
    resource.setDescription(request.getDescription());
    resource.setUsageInstructions(request.getUsageInstructions());
    return resource;
  }

  public static ResourceResponse mapToResourceResponse(final Resource resource,
                                                       final Object[] resourceTypeDetails) {
    ResourceResponse resourceResponse = new ResourceResponse();
    resourceResponse.setId(resource.getId());
    resourceResponse.setTitle(resource.getTitle());
    resourceResponse.setDescription(resource.getDescription());
    resourceResponse.setUsageInstructions(resource.getUsageInstructions());
    resourceResponse.setAppointmentIds(resource.getAppointments());
    resourceResponse.setResourceType(resource.getResourceType());
    resourceResponse.setTargetUserCategory(resource.getTargetUserCategory());
    resourceResponse.setOrganisationId(resource.getOrganisation());

    if (resourceTypeDetails[0] != null) {
      resourceResponse.setResourceContact((ResourceContact) resourceTypeDetails[0]);
    }

    if (resourceTypeDetails[1] != null) {
      resourceResponse.setResourceInfra((ResourceInfra) resourceTypeDetails[1]);
    }

    if (resourceTypeDetails[2] != null) {
      resourceResponse.setResourceService((ResourceService) resourceTypeDetails[2]);
    }

    if (resourceTypeDetails[3] != null) {
      resourceResponse.setResourceTool((ResourceTool) resourceTypeDetails[3]);
    }

    return resourceResponse;
  }
}
