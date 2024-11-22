package com.garlicbread.includify.util;

import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.entity.resource.ResourceType;
import com.garlicbread.includify.entity.resource.types.ResourceContact;
import com.garlicbread.includify.entity.resource.types.ResourceTypeEntity;
import com.garlicbread.includify.entity.user.UserCategory;
import com.garlicbread.includify.model.resource.ResourceContactRequest;
import com.garlicbread.includify.model.resource.ResourceRequest;
import com.garlicbread.includify.model.resource.ResourceTypeRequest;
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

  public static ResourceTypeEntity getSubTypeDetails(ResourceTypeRequest resourceTypeRequest,
                                                      int resourceType) {
    switch (resourceType) {
      case 1:
        ResourceContactRequest resourceContactRequest = (ResourceContactRequest) resourceTypeRequest;
        ResourceContact resourceContact = new ResourceContact();

        resourceContact.setName(resourceContactRequest.getName());
        resourceContact.setPhone(resourceContactRequest.getPhone());
        resourceContact.setAddress(resourceContactRequest.getAddress());
        resourceContact.setLatitude(resourceContactRequest.getLatitude());
        resourceContact.setLongitude(resourceContactRequest.getLongitude());
        resourceContact.setDistance(resourceContactRequest.getDistance());

        return resourceContact;
      default:
        return null;
    }

  }
}
