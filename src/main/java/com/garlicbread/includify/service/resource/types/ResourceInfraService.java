package com.garlicbread.includify.service.resource.types;

import com.garlicbread.includify.entity.resource.types.ResourceInfra;
import com.garlicbread.includify.repository.resource.types.ResourceInfraRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Service class for managing resources.
 * This class provides methods to perform CRUD operations
 * on resources, leveraging the ResourceContactRepository.
 */
@Service
public class ResourceInfraService {

  private final ResourceInfraRepository resourceInfraRepository;

  public ResourceInfraService(ResourceInfraRepository resourceInfraRepository) {
    this.resourceInfraRepository = resourceInfraRepository;
  }

  public Optional<ResourceInfra> getResourceInfraById(String id) {
    return resourceInfraRepository.findById(id);
  }

  public void addResourceInfra(ResourceInfra resourceInfra) {
    resourceInfraRepository.save(resourceInfra);
  }

  public void deleteResourceInfra(String id) {
    resourceInfraRepository.deleteById(id);
  }

}
