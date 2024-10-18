package com.garlicbread.includify.profile.volunteer;

import com.garlicbread.includify.entity.volunteer.Volunteer;
import com.garlicbread.includify.util.Constants;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Represents the details of a volunteer user in the security context.
 * Implements the UserDetails interface to provide user-related information
 * required by Spring Security.
 */
public class VolunteerDetails implements UserDetails {

  private final Volunteer volunteer;

  public VolunteerDetails(Volunteer volunteer) {
    this.volunteer = volunteer;
  }

  @Override
  public String getUsername() {
    return volunteer.getEmail();
  }

  @Override
  public String getPassword() {
    return volunteer.getPassword();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of((GrantedAuthority) () -> Constants.AUTHORITY_VOLUNTEER);
  }
}
