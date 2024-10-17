package com.garlicbread.includify.service.resource;

import com.garlicbread.includify.entity.resource.ResourceType;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.repository.resource.ResourceTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class ResourceTypeService {

    private final ResourceTypeRepository resourceTypeRepository;

    public ResourceTypeService(ResourceTypeRepository resourceTypeRepository) {
        this.resourceTypeRepository = resourceTypeRepository;
    }

    public ResourceType createResourceType(ResourceType resourceType) {
        return resourceTypeRepository.save(resourceType);
    }

    public ResourceType getById(String id) {
        return resourceTypeRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Resource not found with id " + id)
        );
    }

}
