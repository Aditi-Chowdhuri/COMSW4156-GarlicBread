/**
 * This package contains service classes for managing organisation-related operations
 * in the Includify application.
 */

package com.garlicbread.includify.service.organisation;

import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.repository.organisation.OrganisationRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Service class for managing Organisation entities.
 * This class provides methods for CRUD operations on organisations.
 */
@Service
public class OrganisationService {

  private final OrganisationRepository organisationRepository;

  public OrganisationService(OrganisationRepository organisationRepository) {
    this.organisationRepository = organisationRepository;
  }

  public List<Organisation> getAllOrganisations() {
    return organisationRepository.findAll();
  }

  public Optional<Organisation> getOrganisationById(String id) {
    return organisationRepository.findById(id);
  }

  public Organisation createOrganisation(Organisation organisation) {
    return organisationRepository.save(organisation);
  }

  /**
   * Updates an existing Organisation with the given details.
   * If the Organisation is not found, throws a ResourceNotFoundException.
   *
   * @param id the unique ID of the Organisation to be updated
   * @param organisationDetails the details to update in the Organisation
   * @return the updated Organisation entity
   * @throws ResourceNotFoundException if no Organisation is found with the given ID
   */
  public Organisation updateOrganisation(String id, Organisation organisationDetails) {
    return organisationRepository.findById(id).map(organisation -> {
      organisation.setName(organisationDetails.getName());
      organisation.setEmail(organisationDetails.getEmail());
      organisation.setPasswordWithoutHash(organisationDetails.getPassword());
      organisation.setDescription(organisationDetails.getDescription());
      organisation.setLatitude(organisationDetails.getLatitude());
      organisation.setLongitude(organisationDetails.getLongitude());
      organisation.setAddress(organisationDetails.getAddress());
      return organisationRepository.save(organisation);
    }).orElseThrow(() -> new ResourceNotFoundException("Organisation not found with id " + id));
  }

  public void deleteOrganisation(String id) {
    organisationRepository.deleteById(id);
  }


  // add the required methods

}
