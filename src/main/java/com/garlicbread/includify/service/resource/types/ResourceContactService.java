package com.garlicbread.includify.service.resource.types;

import com.garlicbread.includify.entity.resource.types.ResourceContact;
import com.garlicbread.includify.repository.resource.types.ResourceContactRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Service class for managing resources.
 * This class provides methods to perform CRUD operations
 * on resources, leveraging the ResourceContactRepository.
 */
@Service
public class ResourceContactService {

  private final ResourceContactRepository resourceContactRepository;

  public ResourceContactService(ResourceContactRepository resourceContactRepository) {
    this.resourceContactRepository = resourceContactRepository;
  }

  public Optional<ResourceContact> getResourceContactById(String id) {
    return resourceContactRepository.findById(id);
  }

  public void addResourceContact(ResourceContact resourceContact) {
    resourceContactRepository.save(resourceContact);
  }

  public void deleteResourceContact(String id) {
    resourceContactRepository.deleteById(id);
  }

}
