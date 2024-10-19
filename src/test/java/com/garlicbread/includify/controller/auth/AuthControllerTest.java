package com.garlicbread.includify.controller.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garlicbread.includify.config.SecurityConfig;
import com.garlicbread.includify.model.auth.AuthRequest;
import com.garlicbread.includify.service.auth.OrganisationDetailsService;
import com.garlicbread.includify.service.auth.ProfileServiceSelector;
import com.garlicbread.includify.service.auth.TokenService;
import com.garlicbread.includify.service.auth.UserDetailsService;
import com.garlicbread.includify.service.auth.VolunteerDetailsService;
import com.garlicbread.includify.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TokenService tokenService;

  @MockBean
  @Qualifier(Constants.ORGANISATION_AUTHENTICATION_MANAGER)
  private AuthenticationManager organisationAuthenticationManager;

  @MockBean
  @Qualifier(Constants.USER_AUTHENTICATION_MANAGER)
  private AuthenticationManager userAuthenticationManager;

  @MockBean
  @Qualifier(Constants.VOLUNTEER_AUTHENTICATION_MANAGER)
  private AuthenticationManager volunteerAuthenticationManager;

  @MockBean
  private ProfileServiceSelector profileServiceSelector;

  @MockBean
  private OrganisationDetailsService organisationDetailsService;

  @MockBean
  private UserDetailsService userDetailsService;

  @MockBean
  private VolunteerDetailsService volunteerDetailsService;

  @MockBean
  private JwtDecoder jwtDecoder;

  @Autowired
  private ObjectMapper objectMapper;

  @Mock
  private Authentication authentication;

  @BeforeEach
  void setUp() {
    //        when(authentication.getAuthorities()).thenReturn(
    //                List.of((GrantedAuthority) () -> "USER"));
    //        when(authentication.getName()).thenReturn("rb3713@columbia.edu");
  }

  @Test
  void testOrganisationLogin_HappyPath() throws Exception {
    when(organisationAuthenticationManager.authenticate(
        any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
    when(tokenService.generateToken(any(), any(), any())).thenReturn("test-token");

    AuthRequest authRequest = new AuthRequest();
    authRequest.setEmail("columbiia@columbia.edu");
    authRequest.setPassword("password");

    mockMvc.perform(post("/organisation/login").contentType("application/json")
            .content(objectMapper.writeValueAsString(authRequest))).andExpect(status().isOk())
        .andExpect(jsonPath("$").value("test-token"));
  }

  @Test
  void testVolunteerLogin_HappyPath() throws Exception {
    when(volunteerAuthenticationManager.authenticate(
        any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
    when(tokenService.generateToken(any(), any(), any())).thenReturn("test-token");

    AuthRequest authRequest = new AuthRequest();
    authRequest.setEmail("rb3713@columbia.edu");
    authRequest.setPassword("password");
    mockMvc.perform(post("/volunteer/login").contentType("application/json")
            .content(objectMapper.writeValueAsString(authRequest))).andExpect(status().isOk())
        .andExpect(jsonPath("$").value("test-token"));
  }

  @Test
  void testUserLogin_HappyPath() throws Exception {
    when(userAuthenticationManager.authenticate(
        any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
    when(tokenService.generateToken(any(), any(), any())).thenReturn("test-token");

    AuthRequest authRequest = new AuthRequest();
    authRequest.setEmail("user@columbia.com");
    authRequest.setPassword("password");

    mockMvc.perform(post("/user/login").contentType("application/json")
            .content(objectMapper.writeValueAsString(authRequest))).andExpect(status().isOk())
        .andExpect(jsonPath("$").value("test-token"));
  }

  @Test
  void testLogin_InvalidCredentials() throws Exception {
    when(organisationAuthenticationManager.authenticate(
        any(UsernamePasswordAuthenticationToken.class))).thenThrow(
        new org.springframework.security.core.AuthenticationException(
            "Invalid username or password") {
        });

    AuthRequest authRequest = new AuthRequest();
    authRequest.setEmail("invalid_user@columbi.com");
    authRequest.setPassword("wrongpassword");

    mockMvc.perform(post("/organisation/login").contentType("application/json")
            .content(objectMapper.writeValueAsString(authRequest)))
        .andExpect(status().isUnauthorized());
  }
}
