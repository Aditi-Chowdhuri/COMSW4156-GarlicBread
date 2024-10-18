package com.garlicbread.includify.controller.organisation;

import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.service.organisation.OrganisationService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/organisation")
public class OrganisationController {

  private final OrganisationService organisationService;

  public OrganisationController(OrganisationService organisationService) {
    this.organisationService = organisationService;
  }

  @GetMapping("/all")
  public List<Organisation> getAllOrganisations() {
    return organisationService.getAllOrganisations();
  }

  @GetMapping("/{id}")
  public Optional<Organisation> getOrganisationById(@PathVariable String id) {
    return organisationService.getOrganisationById(id);
  }

  @PostMapping("/create")
  public Organisation createOrganisation(@RequestBody Organisation organisation) {
    return organisationService.createOrganisation(organisation);
  }

  @PutMapping("/update/{id}")
  public Organisation updateOrganisation(@PathVariable String id, 
      @RequestBody Organisation organisationDetails) {
    return organisationService.updateOrganisation(id, organisationDetails);
  }

  @PatchMapping("/{id}")
  public Organisation updatePartialOrganisation(@PathVariable String id, 
      @RequestBody Map<String, Object> updates) {
    return organisationService.updatePartialOrganisation(id, updates);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteOrganisation(@PathVariable String id) {
    organisationService.deleteOrganisation(id);
  }
}