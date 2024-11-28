package com.garlicbread.includify.service.auth;

import com.garlicbread.includify.entity.volunteer.Volunteer;
import com.garlicbread.includify.profile.volunteer.VolunteerDetails;
import com.garlicbread.includify.repository.volunteer.VolunteerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VolunteerDetailsServiceTest {

  private VolunteerDetailsService volunteerDetailsService;

  @Mock
  private VolunteerRepository mockVolunteerRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    volunteerDetailsService = new VolunteerDetailsService(mockVolunteerRepository);
  }

  @Test
  void testLoadUserByUsername_Success() {
    String testEmail = "test@volunteer.com";
    Volunteer mockVolunteer = new Volunteer();
    mockVolunteer.setId("vol123");
    mockVolunteer.setEmail(testEmail);
    mockVolunteer.setName("Test Volunteer");

    when(mockVolunteerRepository.findByEmail(testEmail)).thenReturn(Optional.of(mockVolunteer));

    UserDetails userDetails = volunteerDetailsService.loadUserByUsername(testEmail);
    assertNotNull(userDetails, "UserDetails should not be null");
    assertEquals(testEmail, userDetails.getUsername(), "Email should match the username in UserDetails");
    assertTrue(userDetails instanceof VolunteerDetails, "UserDetails should be an instance of VolunteerDetails");

    VolunteerDetails volunteerDetails = (VolunteerDetails) userDetails;
    assertEquals(mockVolunteer.getEmail(), volunteerDetails.getUsername(), "Volunteer object should match");
  }

  @Test
  void testLoadUserByUsername_VolunteerNotFound() {
    String nonExistentEmail = "notfound@volunteer.com";
    when(mockVolunteerRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

    UsernameNotFoundException exception = assertThrows(
        UsernameNotFoundException.class,
        () -> volunteerDetailsService.loadUserByUsername(nonExistentEmail),
        "Expected UsernameNotFoundException when volunteer is not found"
    );

    assertEquals("Volunteer not found", exception.getMessage(), "Exception message should match");
  }

  @Test
  void testLoadUserByUsername_NullEmail() {
    assertThrows(
        UsernameNotFoundException.class,
        () -> volunteerDetailsService.loadUserByUsername(null),
        "Expected  UsernameNotFoundException for null email"
    );
  }
}
