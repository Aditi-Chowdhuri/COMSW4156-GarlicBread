package com.garlicbread.includify.service.resource.types;

import com.garlicbread.includify.entity.resource.types.ResourceTool;
import com.garlicbread.includify.repository.resource.types.ResourceToolRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Service class for managing resources.
 * This class provides methods to perform CRUD operations
 * on resources, leveraging the ResourceContactRepository.
 */
@Service
public class ResourceToolService {

  private final ResourceToolRepository resourceToolRepository;

  public ResourceToolService(ResourceToolRepository resourceToolRepository) {
    this.resourceToolRepository = resourceToolRepository;
  }

  public Optional<ResourceTool> getResourceToolById(String id) {
    return resourceToolRepository.findById(id);
  }

  public void addResourceTool(ResourceTool resourceTool) {
    resourceToolRepository.save(resourceTool);
  }

  public void deleteResourceTool(String id) {
    resourceToolRepository.deleteById(id);
  }

}
