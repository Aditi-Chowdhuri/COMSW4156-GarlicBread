package com.garlicbread.includify.service.resource;

import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.repository.resource.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    public Optional<Resource> getResourceById(String id) {
        return resourceRepository.findById(id);
    }

    public Resource addResource(Resource resource) {
        return resourceRepository.save(resource);
    }

    public void deleteResource(String id) {
        resourceRepository.deleteById(id);
    }

    public List<Resource> getResourcesByOrganisation(String organisationId) {
        return resourceRepository.findByOrganisationId(organisationId);
    }
}