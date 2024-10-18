package com.garlicbread.includify.service.auth;

import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.profile.organisation.OrganisationDetails;
import com.garlicbread.includify.repository.organisation.OrganisationRepository;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OrganisationDetailsService implements UserDetailsService {

  private final OrganisationRepository organisationRepository;

  public OrganisationDetailsService(OrganisationRepository organisationRepository) {
    this.organisationRepository = organisationRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    Optional<Organisation> organisation = organisationRepository.findByEmail(email);

    if (organisation.isEmpty()) {
      throw new UsernameNotFoundException("Organisation not found");
    }

    return new OrganisationDetails(organisation.get());
  }
}
