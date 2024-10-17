package com.garlicbread.includify.controller.organisation;

import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.service.organisation.OrganisationService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<List<Organisation>> getAllOrganisations() {
        List<Organisation> organisations = organisationService.getAllOrganisations();
        if (organisations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(organisations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PermitAll
    public ResponseEntity<Organisation> getOrganisationById(@PathVariable String id) {
        Optional<Organisation> organisation = organisationService.getOrganisationById(id);
        return organisation
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("Organisation not found with id: " + id));
    }

    @PostMapping("/create")
    @PermitAll
    public ResponseEntity<Organisation> createOrganisation(@Valid @RequestBody Organisation organisation) {
        Organisation createdOrganisation = organisationService.createOrganisation(organisation);
        return new ResponseEntity<>(createdOrganisation, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ORGANISATION')")
    public ResponseEntity<Organisation> updateOrganisation(@PathVariable String id, @Valid @RequestBody Organisation organisationDetails) {
        Organisation updatedOrganisation = organisationService.updateOrganisation(id, organisationDetails);
        return new ResponseEntity<>(updatedOrganisation, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ORGANISATION')")
    public ResponseEntity<String> deleteOrganisation(@PathVariable String id) {
        Optional<Organisation> organisation = organisationService.getOrganisationById(id);
        if (organisation.isPresent()) {
            organisationService.deleteOrganisation(id);
            return new ResponseEntity<>("Organisation deleted successfully", HttpStatus.NO_CONTENT);
        } else {
            throw new ResourceNotFoundException("Organisation not found with id: " + id);
        }
    }

}
