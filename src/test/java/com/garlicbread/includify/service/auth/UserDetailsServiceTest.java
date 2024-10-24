package com.garlicbread.includify.service.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.profile.user.UserDetails;
import com.garlicbread.includify.repository.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class UserDetailsServiceTest {

  @Mock
  private UserRepository userRepository;

  private UserDetailsService userDetailsService;

  private User user;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    userDetailsService = new UserDetailsService(userRepository);

    user = new User();
    user.setEmail("test@example.com");
    user.setPassword("password123");
  }

  @Test
  void testLoadUserByUsername_Success() {
    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

    UserDetails userDetails =
        (UserDetails) userDetailsService.loadUserByUsername("test@example.com");

    assertNotNull(userDetails);
    assertEquals("test@example.com", userDetails.getUsername());
    verify(userRepository, times(1)).findByEmail("test@example.com");
  }

  @Test
  void testLoadUserByUsername_UserNotFound() {
    when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class, () -> {
      userDetailsService.loadUserByUsername("nonexistent@example.com");
    });
    verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
  }
}
