package com.garlicbread.includify.controller.organisation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garlicbread.includify.config.SecurityConfig;
import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.profile.organisation.OrganisationDetails;
import com.garlicbread.includify.service.auth.OrganisationDetailsService;
import com.garlicbread.includify.service.auth.ProfileServiceSelector;
import com.garlicbread.includify.service.auth.UserDetailsService;
import com.garlicbread.includify.service.auth.VolunteerDetailsService;
import com.garlicbread.includify.service.organisation.OrganisationService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link OrganisationController}.
 */
@WebMvcTest(OrganisationController.class)
@Import(SecurityConfig.class)
public class OrganisationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrganisationService organisationService;

  @MockBean
  private JwtDecoder jwtDecoder;

  @MockBean
  private ProfileServiceSelector profileServiceSelector;

  @MockBean
  private OrganisationDetailsService organisationDetailsService;

  @MockBean
  private VolunteerDetailsService volunteerDetailsService;

  @MockBean
  private UserDetailsService userDetailsService;

  @Mock
  private Jwt jwt;

  @Autowired
  private ObjectMapper objectMapper;

  private Organisation testOrganisation;

  @BeforeEach
  void setUp() {
    testOrganisation = new Organisation();
    testOrganisation.setAddress("New York");
    testOrganisation.setEmail("test@cu.com");
    testOrganisation.setLatitude("12.89");
    testOrganisation.setLongitude("-12.89");
    testOrganisation.setDescription("Ivy League University");
    testOrganisation.setName("Columbia University");

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
    when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
    when(organisationDetailsService.loadUserByUsername(any())).thenReturn(
        new OrganisationDetails(testOrganisation));
  }

  @Test
  void testGetAllOrganisations_Happy_Path() throws Exception {
    when(organisationService.getAllOrganisations()).thenReturn(
        Collections.singletonList(testOrganisation));

    mockMvc.perform(get("/organisation/all")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].email").value("test@cu.com"))
        .andExpect(jsonPath("$[0].name").value("Columbia University"))
        .andExpect(jsonPath("$[0].latitude").value("12.89"))
        .andExpect(jsonPath("$[0].longitude").value("-12.89"))
        .andExpect(jsonPath("$[0].description").value("Ivy League University"));
  }

  @Test
  void testGetAllOrganisations_Sad_Path() throws Exception {
    when(organisationService.getAllOrganisations()).thenReturn(List.of());

    mockMvc.perform(get("/organisation/all")).andExpect(status().isNoContent());
  }

  @Test
  void getOrganisationById_Happy_Path() throws Exception {
    when(organisationService.getOrganisationById("1")).thenReturn(Optional.of(testOrganisation));

    mockMvc.perform(get("/organisation/1")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.email").value("test@cu.com"))
        .andExpect(jsonPath("$.name").value("Columbia University"))
        .andExpect(jsonPath("$.latitude").value("12.89"))
        .andExpect(jsonPath("$.longitude").value("-12.89"))
        .andExpect(jsonPath("$.description").value("Ivy League University"));
  }

  @Test
  void getOrganisationById_Sad_Path() throws Exception {
    when(organisationService.getOrganisationById("999")).thenReturn(Optional.empty());

    mockMvc.perform(get("/organisation/999")).andExpect(status().isNotFound())
        .andExpect(content().string("Organisation not found with id: 999"));
  }

  @Test
  void createOrganisation() throws Exception {
    when(organisationService.createOrganisation(any(Organisation.class))).thenReturn(
        testOrganisation);

    String requestBody = objectMapper.writeValueAsString(testOrganisation);
    String requestBodyWithPassword = requestBody.substring(0,
        requestBody.length() - 1) + "," + "\"password\":\"Test Password\"}";

    mockMvc.perform(post("/organisation/create").contentType(MediaType.APPLICATION_JSON)
            .content(requestBodyWithPassword)).andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name").value("Columbia University"));
  }

  @Test
  void updateOrganisation_Happy_Path() throws Exception {
    testOrganisation.setAddress("Updated Address");
    when(organisationService.updateOrganisation(anyString(), any(Organisation.class))).thenReturn(
        testOrganisation);

    String requestBody = objectMapper.writeValueAsString(testOrganisation);
    String requestBodyWithPassword = requestBody.substring(0,
        requestBody.length() - 1) + "," + "\"password\":\"Test Password\"}";

    mockMvc.perform(put("/organisation/update/testId").contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer testJWTToken").content(requestBodyWithPassword))
        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.address").value("Updated Address"));
  }

  @Test
  void updateOrganisation_Sad_Path() throws Exception {
    testOrganisation.setAddress("Updated Address");
    when(organisationService.updateOrganisation(anyString(), any(Organisation.class))).thenThrow(
        new ResourceNotFoundException("Organisation not found with id: testId"));

    String requestBody = objectMapper.writeValueAsString(testOrganisation);
    String requestBodyWithPassword = requestBody.substring(0,
        requestBody.length() - 1) + "," + "\"password\":\"Test Password\"}";

    mockMvc.perform(put("/organisation/update/testId").contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer testJWTToken").content(requestBodyWithPassword))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Organisation not found with id: testId"));
  }

  @Test
  void deleteOrganisation_Sad_Path() throws Exception {
    when(organisationService.getOrganisationById(anyString())).thenReturn(Optional.empty());

    mockMvc.perform(delete("/organisation/delete/testId").contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer testJWTToken")).andExpect(status().isNotFound())
        .andExpect(content().string("Organisation not found with id: testId"));
  }

  @Test
  void deleteOrganisation_Happy_Path() throws Exception {
    when(organisationService.getOrganisationById(anyString())).thenReturn(
        Optional.of(testOrganisation));

    mockMvc.perform(delete("/organisation/delete/testId").contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer testJWTToken")).andExpect(status().isNoContent())
        .andExpect(content().string("Organisation deleted successfully"));
  }

}