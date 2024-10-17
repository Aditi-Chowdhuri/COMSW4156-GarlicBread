package com.garlicbread.includify.service.auth;

import com.garlicbread.includify.util.Profile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceSelector {

    private final VolunteerDetailsService volunteerDetailsService;

    private final OrganisationDetailsService organisationDetailsService;

    private final UserDetailsService userDetailsService;

    public ProfileServiceSelector(
        VolunteerDetailsService volunteerDetailsService,
        OrganisationDetailsService organisationDetailsService,
        UserDetailsService userDetailsService
    ) {
        this.volunteerDetailsService = volunteerDetailsService;
        this.organisationDetailsService = organisationDetailsService;
        this.userDetailsService = userDetailsService;
    }

    public UserDetailsService selectService(Profile profileType) {
        return switch (profileType) {
            case VOLUNTEER -> volunteerDetailsService;
            case ORGANISATION -> organisationDetailsService;
            case USER -> userDetailsService;
        };
    }
}
