package com.garlicbread.includify.service.resource;

import com.garlicbread.includify.repository.resource.ResourceTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class ResourceTypeService {

    private final ResourceTypeRepository resourceTypeRepository;

    public ResourceTypeService(ResourceTypeRepository resourceTypeRepository) {
        this.resourceTypeRepository = resourceTypeRepository;
    }

    // add the required methods

}
