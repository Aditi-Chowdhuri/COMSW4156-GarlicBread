package com.garlicbread.includify.controller.resource;

import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.entity.resource.ResourceType;
import com.garlicbread.includify.entity.resource.types.ResourceContact;
import com.garlicbread.includify.entity.resource.types.ResourceInfra;
import com.garlicbread.includify.entity.resource.types.ResourceTool;
import com.garlicbread.includify.entity.user.UserCategory;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.model.resource.ResourceRequest;
import com.garlicbread.includify.model.resource.ResourceResponse;
import com.garlicbread.includify.profile.organisation.OrganisationDetails;
import com.garlicbread.includify.service.organisation.OrganisationService;
import com.garlicbread.includify.service.resource.ResourceService;
import com.garlicbread.includify.service.resource.ResourceTypeService;
import com.garlicbread.includify.service.resource.types.ResourceContactService;
import com.garlicbread.includify.service.resource.types.ResourceInfraService;
import com.garlicbread.includify.service.resource.types.ResourceServiceService;
import com.garlicbread.includify.service.resource.types.ResourceToolService;
import com.garlicbread.includify.service.user.UserCategoryService;
import com.garlicbread.includify.util.ResourceMapper;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing resources in the application.
 * Provides endpoints for creating, retrieving, and deleting resources
 * and resource types.
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {

  private final ResourceService resourceService;
  private final ResourceTypeService resourceTypeService;
  private final OrganisationService organisationService;
  private final UserCategoryService userCategoryService;
  private final ResourceContactService resourceContactService;
  private final ResourceToolService resourceToolService;
  private final ResourceInfraService resourceInfraService;
  private final ResourceServiceService resourceServiceService;

  /**
   * Constructs a ResourceController with the specified services.
   *
   * @param resourceService     the service for managing resources
   * @param resourceTypeService the service for managing resource types
   * @param organisationService the service for managing organisations
   * @param userCategoryService the service for managing user categories
   */
  public ResourceController(ResourceService resourceService,
                            ResourceTypeService resourceTypeService,
                            OrganisationService organisationService,
                            UserCategoryService userCategoryService,
                            ResourceContactService resourceContactService,
                            ResourceToolService resourceToolService,
                            ResourceInfraService resourceInfraService,
                            ResourceServiceService resourceServiceService) {
    this.resourceService = resourceService;
    this.resourceTypeService = resourceTypeService;
    this.organisationService = organisationService;
    this.userCategoryService = userCategoryService;
    this.resourceContactService = resourceContactService;
    this.resourceToolService = resourceToolService;
    this.resourceInfraService = resourceInfraService;
    this.resourceServiceService = resourceServiceService;
  }

  /**
   * Endpoint to create a new resource type.
   *
   * @param resourceType the resource type to be created
   * @return ResponseEntity containing the created resource type
   */
  @PostMapping("/createResourceType")
  @PreAuthorize("hasAuthority('ORGANISATION')")
  public ResponseEntity<ResourceType> createResourceType(
      @Valid @RequestBody ResourceType resourceType) {
    ResourceType createdResourceType = resourceTypeService.createResourceType(resourceType);
    return new ResponseEntity<>(createdResourceType, HttpStatus.CREATED);
  }

  /**
   * Endpoint to delete a resource type by its ID.
   *
   * @param id the ID of the resource type to delete
   * @return ResponseEntity with a message indicating success or failure
   */
  @DeleteMapping("/deleteResourceType/{id}")
  @PreAuthorize("hasAuthority('ORGANISATION')")
  public ResponseEntity<String> deleteResourceType(@PathVariable String id) {
    try {
      if (Integer.parseInt(id) <= 4) {
        return new ResponseEntity<>("Cannot delete a default resource type", HttpStatus.FORBIDDEN);
      }
    } catch (NumberFormatException ignored) {
      return new ResponseEntity<>("Invalid id passed", HttpStatus.BAD_REQUEST);
    }

    ResourceType resourceType = resourceTypeService.getById(id);
    if (resourceType != null) {
      resourceTypeService.deleteResourceTypeById(id);
      return new ResponseEntity<>("Resource type deleted successfully", HttpStatus.NO_CONTENT);
    } else {
      throw new ResourceNotFoundException("Resource type not found with " + "id: " + id);
    }
  }

  /**
   * Retrieves all resources.
   *
   * @return ResponseEntity containing the list of resources or a NO_CONTENT status if none exist
   */
  @GetMapping("/all")
  @PermitAll
  public ResponseEntity<List<Resource>> getAllResources() {
    List<Resource> resources = resourceService.getAllResources();
    if (resources.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(resources, HttpStatus.OK);
  }

  /**
   * Retrieves a specific resource by its ID.
   *
   * @param id the ID of the resource to retrieve
   * @return ResponseEntity containing the found resource
   */
  @GetMapping("/{id}")
  @PermitAll
  public ResponseEntity<ResourceResponse> getResourceById(@PathVariable String id) {
    Optional<Resource> resource = resourceService.getResourceById(id);

    if (resource.isEmpty()) {
      throw new ResourceNotFoundException("Resource " + "not found with id: " + id);
    }

    Object[] resourceTypeDetails = new Object[4];

    for (ResourceType resourceType : resource.get().getResourceType()) {
      if (resourceType.getId() <= 4 && resourceType.getId() >= 1) {

        if (resourceType.getId() == 1) {
          Optional<ResourceContact> resourceContact =
              resourceContactService.getResourceContactById(resource.get().getId());
          resourceContact.ifPresent(contact -> resourceTypeDetails[0] = contact);
        }

        if (resourceType.getId() == 2) {
          Optional<ResourceInfra> resourceInfra
              = resourceInfraService.getResourceInfraById(resource.get().getId());
          resourceInfra.ifPresent(infra -> resourceTypeDetails[1] = infra);
        }

        if (resourceType.getId() == 3) {
          Optional<com.garlicbread.includify.entity.resource.types.ResourceService>
              resourceServiceType = resourceServiceService
                .getResourceServiceById(resource.get().getId());
          resourceServiceType.ifPresent(service -> resourceTypeDetails[2] = service);
        }

        if (resourceType.getId() == 4) {
          Optional<ResourceTool> resourceTool =
              resourceToolService.getResourceToolById(resource.get().getId());
          resourceTool.ifPresent(tool -> resourceTypeDetails[3] = tool);
        }
      }
    }

    ResourceResponse resourceDetails = ResourceMapper.mapToResourceResponse(
        resource.get(),
        resourceTypeDetails
    );

    return ResponseEntity.status(HttpStatus.OK).body(resourceDetails);
  }

  /**
   * Adds a new resource.
   *
   * @param resourceRequest the request body containing details of the resource to add
   * @return ResponseEntity containing the added resource and HTTP status
   */
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('ORGANISATION')")
  public ResponseEntity<Object> addResource(@Valid @RequestBody ResourceRequest resourceRequest,
                                            Authentication authentication) {
    String authenticatedOrganisationId =
        ((OrganisationDetails) authentication.getPrincipal()).getId();
    if (!authenticatedOrganisationId.equals(resourceRequest.getOrganisationId())) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    Optional<Organisation> organisation =
        organisationService.getOrganisationById(resourceRequest.getOrganisationId());

    if (organisation.isEmpty()) {
      throw new ResourceNotFoundException(
          "Organisation not found with " + "id: " + resourceRequest.getOrganisationId());
    }

    List<ResourceType> resourceTypes = new ArrayList<>();
    resourceRequest.getResourceTypeIds().forEach(resourceTypeId -> {
      resourceTypes.add(resourceTypeService.getById(resourceTypeId));
    });

    List<UserCategory> userCategories = new ArrayList<>();
    resourceRequest.getTargetUserCategoryIds().forEach(targetUserCategoryId -> {
      userCategories.add(userCategoryService.getById(targetUserCategoryId));
    });

    ResourceContact resourceContact = resourceRequest.getResourceContact();
    ResourceTool resourceTool = resourceRequest.getResourceTool();
    ResourceInfra resourceInfra = resourceRequest.getResourceInfra();
    com.garlicbread.includify.entity.resource.types.ResourceService resourceServiceType =
        resourceRequest.getResourceService();

    Object[] resourceTypeEntities =
      {resourceContact, resourceInfra, resourceServiceType, resourceTool};

    for (ResourceType resourceType : resourceTypes) {
      if (resourceType.getId() <= 4 && resourceType.getId() >= 1) {
        if (resourceTypeEntities[resourceType.getId() - 1] == null) {
          return new ResponseEntity<>(
              "Resource type details not provided for type " + resourceType.getId(),
              HttpStatus.BAD_REQUEST);
        }
        if (resourceType.getId() == 3 && resourceServiceType.getDate()
            == null && resourceServiceType.getDays() == null) {
          return new ResponseEntity<>(
              "Either date or days field must be provided for resources of type "
                  + resourceType.getId(),
              HttpStatus.BAD_REQUEST);
        }
      }
    }

    Resource resource =
        ResourceMapper.mapToResource(resourceRequest, organisation.get(), resourceTypes,
            userCategories);

    Resource addedResource = resourceService.addResource(resource);

    if (resourceContact != null) {
      resourceContact.setResource(addedResource);
      resourceContactService.addResourceContact(resourceContact);
    }

    if (resourceInfra != null) {
      resourceInfra.setResource(addedResource);
      resourceInfraService.addResourceInfra(resourceInfra);
    }

    if (resourceServiceType != null) {
      resourceServiceType.setResource(addedResource);
      resourceServiceService.addResourceService(resourceServiceType);
    }

    if (resourceTool != null) {
      resourceTool.setResource(addedResource);
      resourceToolService.addResourceTool(resourceTool);
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(addedResource);
  }

  /**
   * Deletes a resource by its ID.
   *
   * @param id the ID of the resource to delete
   * @return ResponseEntity with a message indicating successful deletion
   */
  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('ORGANISATION')")
  public ResponseEntity<String> deleteResource(@PathVariable String id,
                                               Authentication authentication) {
    String authenticatedOrganisationId =
        ((OrganisationDetails) authentication.getPrincipal()).getId();

    Optional<Resource> resource = resourceService.getResourceById(id);
    if (resource.isPresent()) {
      if (resource.get().getOrganisation().equals(authenticatedOrganisationId)) {
        resourceContactService.deleteResourceContact(id);
        resourceToolService.deleteResourceTool(id);
        resourceServiceService.deleteResourceService(id);
        resourceInfraService.deleteResourceInfra(id);
        resource.get().getResourceType().clear();
        resource.get().getTargetUserCategory().clear();
        resourceService.deleteResource(id);
        return new ResponseEntity<>("Resource deleted successfully", HttpStatus.NO_CONTENT);
      } else {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      }
    } else {
      throw new ResourceNotFoundException("Resource not found with id: " + id);
    }
  }
}
