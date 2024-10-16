package com.garlicbread.includify.service.organisation;

import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.repository.organisation.OrganisationRepository;

import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        return organisationRepository.findById(id)
                .map(organisation -> {
                    organisation.setName(organisationDetails.getName());
                    organisation.setEmail(organisationDetails.getEmail());
                    organisation.setHashedPassword(organisationDetails.getHashedPassword());
                    organisation.setDescription(organisationDetails.getDescription());
                    organisation.setLatitude(organisationDetails.getLatitude());
                    organisation.setLongitude(organisationDetails.getLongitude());
                    organisation.setAddress(organisationDetails.getAddress());
                    return organisationRepository.save(organisation);
                }).orElseThrow(() -> new RuntimeException("Organisation not found with id " + id));
    }

    public Organisation updatePartialOrganisation(String id, Map<String, Object> updates) {
        return organisationRepository.findById(id)
                .map(organisation -> {
                    updates.forEach((key, value) -> {
                        Field field = ReflectionUtils.findField(Organisation.class, key);
                        field.setAccessible(true);
                        ReflectionUtils.setField(field, organisation, value);
                    });
                    return organisationRepository.save(organisation);
                }).orElseThrow(() -> new RuntimeException("Organisation not found with id " + id));
    }

    public void deleteOrganisation(String id) {
        organisationRepository.deleteById(id);
    }


    // add the required methods

}
