package com.garlicbread.includify.service.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.garlicbread.includify.util.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetailsService;

class ProfileServiceSelectorTest {

  private ProfileServiceSelector profileServiceSelector;

  @Mock
  private VolunteerDetailsService mockVolunteerDetailsService;

  @Mock
  private OrganisationDetailsService mockOrganisationDetailsService;

  @Mock
  private UserDetailsService mockUserDetailsService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    profileServiceSelector = new ProfileServiceSelector(
        mockVolunteerDetailsService,
        mockOrganisationDetailsService,
        mockUserDetailsService
    );
  }

  @Test
  void testSelectServiceForVolunteerProfile() {
    UserDetailsService selectedService = profileServiceSelector.selectService(Profile.VOLUNTEER);

    assertEquals(mockVolunteerDetailsService, selectedService,
        "Expected VolunteerDetailsService for VOLUNTEER profile");
  }

  @Test
  void testSelectServiceForOrganisationProfile() {
    UserDetailsService selectedService = profileServiceSelector.selectService(Profile.ORGANISATION);

    assertEquals(mockOrganisationDetailsService, selectedService,
        "Expected OrganisationDetailsService for ORGANISATION profile");
  }

  @Test
  void testSelectServiceForUserProfile() {
    UserDetailsService selectedService = profileServiceSelector.selectService(Profile.USER);

    assertEquals(mockUserDetailsService, selectedService,
        "Expected UserDetailsService for USER profile");
  }
}
