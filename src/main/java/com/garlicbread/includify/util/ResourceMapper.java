package com.garlicbread.includify.util;

import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.resource.ResourceType;
import com.garlicbread.includify.entity.user.UserCategory;
import com.garlicbread.includify.model.resource.ResourceRequest;

import java.util.List;

public class ResourceMapper {

    public static Resource mapToResource(ResourceRequest request, Organisation organisation,
                                         List<ResourceType> resourceTypes, List<UserCategory> userCategories) {
        Resource resource = new Resource();
        resource.setOrganisation(organisation);
        resource.setResourceType(resourceTypes);
        resource.setTargetUserCategory(userCategories);
        resource.setTitle(request.getTitle());
        resource.setDescription(request.getDescription());
        resource.setUsageInstructions(request.getUsageInstructions());
        return resource;
    }
}
