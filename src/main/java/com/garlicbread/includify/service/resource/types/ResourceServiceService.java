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

  private final ResourceServiceRepository resourceServiceRepository;

  public ResourceServiceService(ResourceServiceRepository resourceServiceRepository) {
    this.resourceServiceRepository = resourceServiceRepository;
  }

  public Optional<ResourceService> getResourceServiceById(String id) {
    return resourceServiceRepository.findById(id);
  }

  public void addResourceService(ResourceService resourceService) {
    resourceServiceRepository.save(resourceService);
  }

  public void deleteResourceService(String id) {
    resourceServiceRepository.deleteById(id);
  }

}
