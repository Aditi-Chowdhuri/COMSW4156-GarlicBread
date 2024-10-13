package com.garlicbread.includify.controller.resource;

import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.service.resource.ResourceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/resource")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
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
