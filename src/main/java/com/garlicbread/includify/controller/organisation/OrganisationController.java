package com.garlicbread.includify.controller.organisation;

import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.service.organisation.OrganisationService;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/organisation")
public class OrganisationController {

    private final OrganisationService organisationService;

    public OrganisationController(OrganisationService organisationService) {
        this.organisationService = organisationService;
    }

    @GetMapping("/all")
    @PermitAll
    public List<Organisation> getAllOrganisations() {
        return organisationService.getAllOrganisations();
    }

    @GetMapping("/{id}")
    @PermitAll
    public Optional<Organisation> getOrganisationById(@PathVariable String id) {
        return organisationService.getOrganisationById(id);
    }

    @PostMapping("/create")
    @PermitAll
    public Organisation createOrganisation(@RequestBody Organisation organisation) {
        return organisationService.createOrganisation(organisation);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ORGANISATION')")
    public Organisation updateOrganisation(@PathVariable String id, @RequestBody Organisation organisationDetails) {
        return organisationService.updateOrganisation(id, organisationDetails);
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ORGANISATION')")
    public Organisation updatePartialOrganisation(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        return organisationService.updatePartialOrganisation(id, updates);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ORGANISATION')")
    public void deleteOrganisation(@PathVariable String id) {
        organisationService.deleteOrganisation(id);
    }

}
