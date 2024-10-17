package com.garlicbread.includify.controller.resource;

import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.service.resource.ResourceService;
import com.garlicbread.includify.service.resource.ResourceTypeService;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/resource")
public class ResourceController {

    private final ResourceService resourceService;
    private final ResourceTypeService resourceTypeService;

    public ResourceController(ResourceService resourceService, ResourceTypeService resourceTypeService) {
        this.resourceService = resourceService;
        this.resourceTypeService = resourceTypeService;
    }

    @GetMapping("/all")
    @PermitAll
    public ResponseEntity<List<Resource>> getAllResources() {
        List<Resource> resources = resourceService.getAllResources();
        if (resources.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PermitAll
    public ResponseEntity<Resource> getResourceById(@PathVariable String id) {
        Optional<Resource> resource = resourceService.getResourceById(id);
        return resource
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ORGANISATION')")
    public ResponseEntity<Resource> addResource(@RequestBody Resource resource) {
        Resource createdResource = resourceService.addResource(resource);
        return new ResponseEntity<>(createdResource, HttpStatus.CREATED);    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ORGANISATION')")
    public ResponseEntity<String> deleteResource(@PathVariable String id) {
        Optional<Resource> resource = resourceService.getResourceById(id);
        if (resource.isPresent()) {
            resourceService.deleteResource(id);
            return new ResponseEntity<>("Resource deleted successfully", HttpStatus.NO_CONTENT);
        } else {
            throw new ResourceNotFoundException("Resource not found with id: " + id);
        }
    }
}
