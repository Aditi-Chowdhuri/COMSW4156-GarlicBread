package com.garlicbread.includify.controller.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garlicbread.includify.config.SecurityConfig;
import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.entity.volunteer.Volunteer;
import com.garlicbread.includify.model.auth.AuthRequest;
import com.garlicbread.includify.service.auth.TokenService;
import com.garlicbread.includify.service.organisation.OrganisationService;
import com.garlicbread.includify.service.user.UserService;
import com.garlicbread.includify.service.volunteer.VolunteerService;
import com.garlicbread.includify.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@Transactional
public class AuthControllerExternalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrganisationService organisationService;

    @MockBean
    private UserService userService;

    @MockBean
    private VolunteerService volunteerService;

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
    private TokenService tokenService;

    private Organisation testOrganisation;
    private User testUser;
    private Volunteer testVolunteer;

    @BeforeEach
    void setup() {
        testOrganisation = new Organisation();
        testOrganisation.setEmail("test@organisation.com");
        testOrganisation.setPassword("password");
        testOrganisation.setName("Test Organisation");

        testUser = new User();
        testUser.setEmail("test@user.com");
        testUser.setPassword("password");
        testUser.setName("Test User");

        testVolunteer = new Volunteer();
        testVolunteer.setId("testId");
        testVolunteer.setEmail("ps6789@columbia.edu");
        testVolunteer.setName("Puja S");
        testVolunteer.setAge(25);
        testVolunteer.setAddress("116st, NY");
        testVolunteer.setPhone("929-428-6666");
    }

    @Test
    void testOrganisationLogin_HappyPath() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail(testOrganisation.getEmail());
        authRequest.setPassword("password");

        Authentication mockAuthentication = mock(Authentication.class);
        when(organisationAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuthentication);
        when(tokenService.generateToken(any(), any(), any())).thenReturn("mocked-token");

        mockMvc.perform(post("/organisation/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value("mocked-token"));
    }

    @Test
    void testUserLogin_HappyPath() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail(testUser.getEmail());
        authRequest.setPassword("password");

        Authentication mockAuthentication = mock(Authentication.class);
        when(userAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuthentication);
        when(tokenService.generateToken(any(), any(), any())).thenReturn("mocked-user-token");

        mockMvc.perform(post("/registration/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value("mocked-user-token"));
    }

    @Test
    void testVolunteerLogin_HappyPath() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail(testVolunteer.getEmail());
        authRequest.setPassword("password");
    
        Authentication mockAuthentication = mock(Authentication.class);
        when(volunteerAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuthentication);
        when(tokenService.generateToken(any(), any(), any())).thenReturn("mocked-volunteer-token");
    
        mockMvc.perform(post("/volunteer/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value("mocked-volunteer-token"));
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("invalid@email.com");
        authRequest.setPassword("wrongpassword");

        when(organisationAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/organisation/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isUnauthorized());
    }
}