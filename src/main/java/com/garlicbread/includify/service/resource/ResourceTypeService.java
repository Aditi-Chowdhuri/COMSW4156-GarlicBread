package com.garlicbread.includify.service.resource;

import com.garlicbread.includify.entity.resource.ResourceType;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.repository.resource.ResourceTypeRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for managing resource types.
 * This class provides methods to create and retrieve resource types
 * from the underlying data store.
 */
@Service
public class ResourceTypeService {

  private final ResourceTypeRepository resourceTypeRepository;

  public ResourceTypeService(ResourceTypeRepository resourceTypeRepository) {
    this.resourceTypeRepository = resourceTypeRepository;
  }

  public ResourceType createResourceType(ResourceType resourceType) {
    return resourceTypeRepository.save(resourceType);
  }

  /**
   * Retrieves a {@link ResourceType} entity by its unique identifier.
   *
   * @param id the unique identifier of the {@link ResourceType} to retrieve
   * @return the {@link ResourceType} entity if found
   * @throws ResourceNotFoundException if no {@link ResourceType} is found with the given id
   */
  public ResourceType getById(String id) {
    return resourceTypeRepository.findById(id)
        .orElseThrow(() ->
            new ResourceNotFoundException("Resource type not found with " + "id " + id));
  }

  public void deleteResourceTypeById(String id) {
    resourceTypeRepository.deleteById(id);
  }

}
