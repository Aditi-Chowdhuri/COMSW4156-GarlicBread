package com.garlicbread.includify.service.auth;

import com.garlicbread.includify.util.Profile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Service selector for user profiles.
 * This service is responsible for selecting the appropriate
 * UserDetailsService implementation based on the profile type.
 */
@Service
public class ProfileServiceSelector {

  private final VolunteerDetailsService volunteerDetailsService;

  private final OrganisationDetailsService organisationDetailsService;

  private final UserDetailsService userDetailsService;

  /**
   * Constructs a new ProfileServiceSelector with the specified services.
   *
   * @param volunteerDetailsService    the service for volunteer profile handling
   * @param organisationDetailsService the service for organisation profile handling
   * @param userDetailsService         the service for general user profile handling
   */
  public ProfileServiceSelector(VolunteerDetailsService volunteerDetailsService,
                                OrganisationDetailsService organisationDetailsService,
                                UserDetailsService userDetailsService) {
    this.volunteerDetailsService = volunteerDetailsService;
    this.organisationDetailsService = organisationDetailsService;
    this.userDetailsService = userDetailsService;
  }

  /**
   * Selects the appropriate UserDetailsService implementation based on the given profile type.
   *
   * @param profileType the profile type for which the service is to be selected
   * @return the UserDetailsService corresponding to the specified profile type
   */
  public UserDetailsService selectService(Profile profileType) {
    return switch (profileType) {
      case VOLUNTEER -> volunteerDetailsService;
      case ORGANISATION -> organisationDetailsService;
      case USER -> userDetailsService;
    };
  }
}
