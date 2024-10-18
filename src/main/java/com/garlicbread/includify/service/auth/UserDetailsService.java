package com.garlicbread.includify.service.auth;

import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.repository.user.UserRepository;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService
    implements org.springframework.security.core.userdetails.UserDetailsService {

  private final UserRepository userRepository;

  public UserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    Optional<User> user = userRepository.findByEmail(email);

    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User not found");
    }

    return new com.garlicbread.includify.profile.user.UserDetails(user.get());
  }
}
