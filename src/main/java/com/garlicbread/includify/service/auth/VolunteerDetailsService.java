package com.garlicbread.includify.service.auth;

import com.garlicbread.includify.entity.volunteer.Volunteer;
import com.garlicbread.includify.profile.volunteer.VolunteerDetails;
import com.garlicbread.includify.repository.volunteer.VolunteerRepository;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class for loading volunteer details from the database.
 * This class implements Spring Security's UserDetailsService interface
 * to retrieve volunteer information based on email for authentication.
 */
@Service
public class VolunteerDetailsService
    implements org.springframework.security.core.userdetails.UserDetailsService {

  private final VolunteerRepository volunteerRepository;

  public VolunteerDetailsService(VolunteerRepository volunteerRepository) {
    this.volunteerRepository = volunteerRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    Optional<Volunteer> volunteer = volunteerRepository.findByEmail(email);

    if (volunteer.isEmpty()) {
      throw new UsernameNotFoundException("Volunteer not found");
    }

    return new VolunteerDetails(volunteer.get());
  }
}
