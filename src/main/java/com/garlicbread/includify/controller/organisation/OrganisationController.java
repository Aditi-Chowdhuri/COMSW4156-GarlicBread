package com.garlicbread.includify.controller.organisation;

import com.garlicbread.includify.service.organisation.OrganisationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organisation")
public class OrganisationController {

    private final OrganisationService organisationService;

    public OrganisationController(OrganisationService organisationService) {
        this.organisationService = organisationService;
    }

    // add the required methods
}
