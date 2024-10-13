package com.garlicbread.includify.service.organisation;

import com.garlicbread.includify.repository.organisation.OrganisationRepository;
import org.springframework.stereotype.Service;

@Service
public class OrganisationService {

    private final OrganisationRepository organisationRepository;

    public OrganisationService(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    // add the required methods

}
