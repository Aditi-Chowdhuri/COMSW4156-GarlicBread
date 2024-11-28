package com.garlicbread.includify.controller.organisation;

import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.profile.organisation.OrganisationDetails;
import com.garlicbread.includify.service.organisation.OrganisationService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing Organisations.
 * This class provides endpoints for creating, retrieving, updating, and deleting organisations.
 */
@RestController
@RequestMapping("/organisation")
public class OrganisationController {

  private final OrganisationService organisationService;

  public OrganisationController(OrganisationService organisationService) {
    this.organisationService = organisationService;
  }

  /**
   * Get all organisations.
   *
   * @return a list of all organisations, or NO_CONTENT if none are found
   */
  @GetMapping("/all")
  @PermitAll
  public ResponseEntity<List<Organisation>> getAllOrganisations() {
    List<Organisation> organisations = organisationService.getAllOrganisations();
    if (organisations.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(organisations, HttpStatus.OK);
  }

  /**
   * Get an organisation by its ID.
   *
   * @param id the ID of the organisation
   * @return the organisation if found, or throws ResourceNotFoundException if not found
   */
  @GetMapping("/{id}")
  @PermitAll
  public ResponseEntity<Organisation> getOrganisationById(@PathVariable String id) {
    Optional<Organisation> organisation = organisationService.getOrganisationById(id);
    return organisation.map(ResponseEntity::ok)
        .orElseThrow(() -> new ResourceNotFoundException("Organisation not found with id: " + id));
  }

  /**
   * Create a new organisation.
   *
   * @param organisation the organisation object to create
   * @return the created organisation with HTTP status CREATED
   */

  @PostMapping("/create")
  @PermitAll
  public ResponseEntity<Organisation> createOrganisation(
      @Valid @RequestBody Organisation organisation) {
    Organisation createdOrganisation = organisationService.createOrganisation(organisation);
    return new ResponseEntity<>(createdOrganisation, HttpStatus.CREATED);
  }

  /**
   * Update an existing organisation.
   * Requires the 'ORGANISATION' authority to execute this operation.
   *
   * @param id                  the ID of the organisation to update
   * @param organisationDetails the new organisation details
   * @return the updated organisation with HTTP status OK
   */
  @PutMapping("/update/{id}")
  @PreAuthorize("hasAuthority('ORGANISATION')")
  public ResponseEntity<Organisation> updateOrganisation(@PathVariable String id,
                                                         @Valid @RequestBody
                                                         Organisation organisationDetails,
                                                         Authentication authentication) {
    String authenticatedOrganisationId = ((OrganisationDetails) authentication.getPrincipal())
            .getId();
    if (!authenticatedOrganisationId.equals(id)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    Organisation updatedOrganisation =
        organisationService.updateOrganisation(id, organisationDetails);
    return new ResponseEntity<>(updatedOrganisation, HttpStatus.OK);
  }

  /**
   * Delete an organisation by its ID.
   * Requires the 'ORGANISATION' authority to execute this operation.
   *
   * @param id the ID of the organisation to delete
   * @return a message confirming deletion with HTTP status NO_CONTENT,
   */
  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('ORGANISATION')")
  public ResponseEntity<String> deleteOrganisation(@PathVariable String id,
                                                   Authentication authentication) {
    String authenticatedOrganisationId = ((OrganisationDetails) authentication.getPrincipal())
            .getId();
    if (!authenticatedOrganisationId.equals(id)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    Optional<Organisation> organisation = organisationService.getOrganisationById(id);
    if (organisation.isPresent()) {
      organisationService.deleteOrganisation(id);
      return new ResponseEntity<>("Organisation deleted successfully", HttpStatus.NO_CONTENT);
    } else {
      throw new ResourceNotFoundException("Organisation not found with " + "id: " + id);
    }
  }

}
