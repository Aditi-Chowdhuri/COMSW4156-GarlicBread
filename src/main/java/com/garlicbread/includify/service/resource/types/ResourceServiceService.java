package com.garlicbread.includify.service.resource.types;

import com.garlicbread.includify.entity.resource.types.ResourceService;
import com.garlicbread.includify.repository.resource.types.ResourceServiceRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Service class for managing resources.
 * This class provides methods to perform CRUD operations
 * on resources, leveraging the ResourceContactRepository.
 */
@Service
public class ResourceServiceService {

  private final ResourceServiceRepository resourceServiceService;

  public ResourceServiceService(ResourceServiceRepository resourceServiceRepository) {
    this.resourceServiceService = resourceServiceRepository;
  }

  public Optional<ResourceService> getResourceServiceById(String id) {
    return resourceServiceService.findById(id);
  }

  public void addResourceService(ResourceService resourceService) {
    resourceServiceService.save(resourceService);
  }

  public void deleteResourceService(String id) {
    resourceServiceService.deleteById(id);
  }

}
