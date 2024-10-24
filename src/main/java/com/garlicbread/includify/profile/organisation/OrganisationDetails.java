package com.garlicbread.includify.profile.organisation;

import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.util.Constants;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Represents the details of an organization that implements the UserDetails interface.
 * This class is used for authentication and authorization of organizations in the application.
 */
public class OrganisationDetails implements UserDetails {

  private final Organisation organisation;

  public OrganisationDetails(Organisation organisation) {
    this.organisation = organisation;
  }

  @Override
  public String getUsername() {
    return organisation.getEmail();
  }

  @Override
  public String getPassword() {
    return organisation.getPassword();
  }

  public String getId() {
    return organisation.getId();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of((GrantedAuthority) () -> Constants.AUTHORITY_ORGANISATION);
  }
}
