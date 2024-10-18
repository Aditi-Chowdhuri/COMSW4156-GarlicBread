package com.garlicbread.includify.controller.resource;

import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.service.resource.ResourceService;
import com.garlicbread.includify.service.resource.ResourceTypeService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resource")
public class ResourceController {

  private final ResourceService resourceService;
  private final ResourceTypeService resourceTypeService;

  public ResourceController(ResourceService resourceService, 
      ResourceTypeService resourceTypeService) {
    this.resourceService = resourceService;
    this.resourceTypeService = resourceTypeService;
  }

  @GetMapping("/all")
  public List<Resource> getAllResources() {
    return resourceService.getAllResources();
  }

  @GetMapping("/{id}")
  public Optional<Resource> getResourceById(@PathVariable String id) {
    return resourceService.getResourceById(id);
  }

  @PostMapping("/add")
  public Resource addResource(@RequestBody Resource resource) {
    return resourceService.addResource(resource);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteResource(@PathVariable String id) {
    resourceService.deleteResource(id);
  }
}