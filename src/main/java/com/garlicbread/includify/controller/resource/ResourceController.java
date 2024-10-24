package com.garlicbread.includify.controller.resource;

import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.entity.resource.ResourceType;
import com.garlicbread.includify.entity.user.UserCategory;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.model.resource.ResourceRequest;
import com.garlicbread.includify.profile.organisation.OrganisationDetails;
import com.garlicbread.includify.service.organisation.OrganisationService;
import com.garlicbread.includify.service.resource.ResourceService;
import com.garlicbread.includify.service.resource.ResourceTypeService;
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
                            UserCategoryService userCategoryService) {
    this.resourceService = resourceService;
    this.resourceTypeService = resourceTypeService;
    this.organisationService = organisationService;
    this.userCategoryService = userCategoryService;
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
  public ResponseEntity<String> deleteResourceType(
      @PathVariable String id) {
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
  public ResponseEntity<Resource> getResourceById(@PathVariable String id) {
    Optional<Resource> resource = resourceService.getResourceById(id);
    return resource.map(ResponseEntity::ok)
        .orElseThrow(() -> new ResourceNotFoundException("Resource " + "not found with id: " + id));
  }

  /**
   * Adds a new resource.
   *
   * @param resourceRequest the request body containing details of the resource to add
   * @return ResponseEntity containing the added resource and HTTP status
   */
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('ORGANISATION')")
  public ResponseEntity<Resource> addResource(@Valid @RequestBody ResourceRequest resourceRequest,
                                              Authentication authentication) {
    String authenticatedOrganisationId = ((OrganisationDetails) authentication.getPrincipal())
            .getId();
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

    Resource resource =
        ResourceMapper.mapToResource(resourceRequest, organisation.get(), resourceTypes,
            userCategories);

    Resource addedResource = resourceService.addResource(resource);
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
    Optional<Organisation> organisation =
        organisationService.getOrganisationById(authenticatedOrganisationId);
    if (organisation.isPresent()) {
      List<String> resourceIds =
          organisation.get().getResources().stream().map(Resource::getId).toList();
      if (!resourceIds.contains(id)) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      }
    }

    Optional<Resource> resource = resourceService.getResourceById(id);
    if (resource.isPresent()) {
      resourceService.deleteResource(id);
      return new ResponseEntity<>("Resource deleted successfully", HttpStatus.NO_CONTENT);
    } else {
      throw new ResourceNotFoundException("Resource not found with id: " + id);
    }
  }
}
