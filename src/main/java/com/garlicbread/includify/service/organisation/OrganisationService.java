package com.garlicbread.includify.service.organisation;

import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.repository.organisation.OrganisationRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

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
