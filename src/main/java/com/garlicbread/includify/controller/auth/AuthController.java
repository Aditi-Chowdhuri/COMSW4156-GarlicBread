package com.garlicbread.includify.controller.auth;

import com.garlicbread.includify.model.auth.AuthRequest;
import com.garlicbread.includify.service.auth.TokenService;
import com.garlicbread.includify.util.Constants;
import com.garlicbread.includify.util.Profile;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  private final TokenService tokenService;

  private final AuthenticationManager organisationAuthenticationManager;
  private final AuthenticationManager userAuthenticationManager;
  private final AuthenticationManager volunteerAuthenticationManager;

  public AuthController(TokenService tokenService,
                        @Qualifier(Constants.ORGANISATION_AUTHENTICATION_MANAGER)
                        AuthenticationManager organisationAuthenticationManager,
                        @Qualifier(Constants.USER_AUTHENTICATION_MANAGER)
                        AuthenticationManager userAuthenticationManager,
                        @Qualifier(Constants.VOLUNTEER_AUTHENTICATION_MANAGER)
                        AuthenticationManager volunteerAuthenticationManager) {
    this.tokenService = tokenService;

    this.organisationAuthenticationManager = organisationAuthenticationManager;
    this.userAuthenticationManager = userAuthenticationManager;
    this.volunteerAuthenticationManager = volunteerAuthenticationManager;
  }

  @PostMapping("/organisation/login")
  @PermitAll
  public String organisationLogin(@Valid @RequestBody AuthRequest authRequest) {
    return processLogin(organisationAuthenticationManager, authRequest, Profile.ORGANISATION);
  }

  @PostMapping("/volunteer/login")
  @PermitAll
  public String volunteerLogin(@Valid @RequestBody AuthRequest authRequest) {
    return processLogin(volunteerAuthenticationManager, authRequest, Profile.VOLUNTEER);
  }

  @PostMapping("/user/login")
  @PermitAll
  public String userLogin(@Valid @RequestBody AuthRequest authRequest) {
    return processLogin(userAuthenticationManager, authRequest, Profile.USER);
  }

  private String processLogin(AuthenticationManager authenticationManager, AuthRequest authRequest,
                              Profile profile) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
              authRequest.getPassword()));

      List<String> authorities =
          authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

      return tokenService.generateToken(authRequest.getEmail(), profile, authorities);

    } catch (AuthenticationException e) {
      throw new AuthenticationException("Invalid username or password") {
      };
    }
  }
}
