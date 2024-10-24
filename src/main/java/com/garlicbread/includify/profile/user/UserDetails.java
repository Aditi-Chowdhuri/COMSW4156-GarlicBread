package com.garlicbread.includify.profile.user;

import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.util.Constants;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;

/**
 * Custom implementation of the {@link org.springframework.security.core.userdetails.UserDetails}
 * interface to represent the application's user details.
 */
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

  private final User user;

  public UserDetails(User user) {
    this.user = user;
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  public String getId() {
    return user.getId();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of((GrantedAuthority) () -> Constants.AUTHORITY_USER);
  }
}
