package com.garlicbread.includify.controller.appointment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garlicbread.includify.config.SecurityConfig;

import static org.hibernate.internal.util.collections.CollectionHelper.listOf;
import static org.mockito.ArgumentMatchers.*;

import com.garlicbread.includify.entity.appointment.Appointment;
import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.entity.resource.ResourceType;
import com.garlicbread.includify.entity.resource.types.ResourceTool;
import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.entity.volunteer.Volunteer;
import com.garlicbread.includify.model.appointment.AppointmentRequest;
import com.garlicbread.includify.profile.organisation.OrganisationDetails;
import com.garlicbread.includify.profile.user.UserDetails;
import com.garlicbread.includify.profile.volunteer.VolunteerDetails;
import com.garlicbread.includify.service.appointment.AppointmentService;
import com.garlicbread.includify.service.auth.OrganisationDetailsService;
import com.garlicbread.includify.service.auth.ProfileServiceSelector;
import com.garlicbread.includify.service.auth.UserDetailsService;
import com.garlicbread.includify.service.auth.VolunteerDetailsService;
import com.garlicbread.includify.service.organisation.OrganisationService;
import com.garlicbread.includify.service.resource.ResourceService;
import com.garlicbread.includify.service.resource.types.ResourceToolService;
import com.garlicbread.includify.service.user.UserService;
import com.garlicbread.includify.service.volunteer.VolunteerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
@Import(SecurityConfig.class)
public class AppointmentControllerTest {

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
  private AppointmentService appointmentService;

  @MockBean
  private VolunteerDetailsService volunteerDetailsService;

  @MockBean
  private ResourceService resourceService;

  @MockBean
  private UserService userService;

  @MockBean
  private VolunteerService volunteerService;

  @MockBean
  private UserDetailsService userDetailsService;

  @MockBean
  private ResourceToolService resourceToolService;

  @MockBean
  private Authentication authentication;

  @Mock
  private Jwt jwt;

  @Autowired
  private ObjectMapper objectMapper;

  private Organisation testOrganisation;
  private User testUser;
  private Volunteer testVolunteer;
  private AppointmentRequest appointmentRequest;
  private Resource testResource;
  private ResourceType testResourceType;

  @BeforeEach
  void setup() {
    appointmentRequest = new AppointmentRequest();
    appointmentRequest.setUserId("user123");
    appointmentRequest.setOrganisationId("org123");
    appointmentRequest.setDate("12252024");
    appointmentRequest.setTimeStart(10);
    appointmentRequest.setTimeEnd(12);
    appointmentRequest.setResourceIds(List.of("res123"));

    testOrganisation = new Organisation();
    testOrganisation.setId("testId");
    testOrganisation.setAddress("New York");
    testOrganisation.setEmail("test@cu.com");
    testOrganisation.setLatitude("12.89");
    testOrganisation.setLongitude("-12.89");
    testOrganisation.setDescription("Ivy League University");
    testOrganisation.setName("Columbia University");

    testUser = new User();
    testUser.setId("user1");
    testUser.setEmail("ps014566@gmail.com");
    testUser.setName("Puja S");

    testVolunteer = new Volunteer();
    testVolunteer.setId("testId");
    testVolunteer.setEmail("ps6789@columbia.edu");
    testVolunteer.setName("Puja S");
    testVolunteer.setAge(25);
    testVolunteer.setAddress("116st, NY");
    testVolunteer.setPhone("929-428-6666");

    testResourceType = new ResourceType();
    testResourceType.setId(4);
    testResourceType.setTitle("infrastructure");
    testResourceType.setDescription("infrastructure resource category.");

    testResource = new Resource();
    testResource.setTitle("Test Resource");
    testResource.setDescription("Resource added for testing.");
    testResource.setUsageInstructions("set usage instructions");
    testResource.setResourceType(listOf(testResourceType));
    testResource.setOrganisation(testOrganisation);
  }

  @Test
  void testGetAllAppointmentsForOrganisation_Happy_Path() throws Exception  {
    Appointment appointment = new Appointment();
    appointment.setDate("12252024");
    appointment.setTimeStart(10);
    appointment.setTimeEnd(12);

    List<Appointment> appointments = new ArrayList<>();
    appointments.add(appointment);
    when(appointmentService.getAllAppointmentsForOrganisation(any())).thenReturn(appointments);

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
    when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
    when(organisationDetailsService.loadUserByUsername(any())).thenReturn(
        new OrganisationDetails(testOrganisation));

    mockMvc.perform(get("/appointment/organisation")
        .header("Authorization", "Bearer Test-JWT-Token")
        .param("organisation", "testId"))
        .andExpect(jsonPath("$[0].date").value("12252024"))
        .andExpect(jsonPath("$[0].timeStart").value(10))
        .andExpect(jsonPath("$[0].timeEnd").value(12))
        .andExpect(status().isOk());
  }

  @Test
  void testGetAllAppointmentsForOrganisation_Sad_Path() throws Exception  {
    Appointment appointment = new Appointment();
    appointment.setDate("12252024");
    appointment.setTimeStart(10);
    appointment.setTimeEnd(12);

    List<Appointment> appointments = new ArrayList<>();
    appointments.add(appointment);
    when(appointmentService.getAllAppointmentsForOrganisation(any())).thenReturn(appointments);

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
    when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
    when(organisationDetailsService.loadUserByUsername(any())).thenReturn(
        new OrganisationDetails(testOrganisation));

    mockMvc.perform(get("/appointment/organisation")
            .header("Authorization", "Bearer Test-JWT-Token")
            .param("organisation", "testIdd"))
            .andExpect(status().isForbidden());
  }

  @Test
  void testCreateAppointment_Happy_Path_Organisation() throws Exception {
    Appointment appointment = new Appointment();
    appointment.setDate("12232024");
    appointment.setTimeStart(32400000);
    appointment.setTimeEnd(61200000);

    ResourceTool resourceTool = new ResourceTool();
    resourceTool.setId("resource1");
    resourceTool.setCount(1);

    testOrganisation.setId("org123");
    testResource.setOrganisation(testOrganisation);

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
    when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
    when(organisationDetailsService.loadUserByUsername(any())).thenReturn(
        new OrganisationDetails(testOrganisation));

    when(organisationService.getOrganisationById("org123")).thenReturn(Optional.of(testOrganisation));
    when(userService.getUserById("user123")).thenReturn(Optional.of(testUser));
    when(resourceService.getResourceById(anyString())).thenReturn(Optional.of(testResource));
    when(resourceToolService.getResourceToolById("resource1")).thenReturn(Optional.of(resourceTool));
    when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(appointment);

    String requestBody = objectMapper.writeValueAsString(appointmentRequest);
    mockMvc.perform(post("/appointment")
            .header("Authorization", "Bearer Test-JWT-Token")
            .contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.date").value("12232024"))
            .andExpect(jsonPath("$.timeStart").value(32400000))
            .andExpect(jsonPath("$.timeEnd").value(61200000));
  }

  @Test
  void testCreateAppointment_Happy_Path_User() throws Exception {
    Appointment appointment = new Appointment();
    appointment.setDate("12232024");
    appointment.setTimeStart(32400000);
    appointment.setTimeEnd(61200000);

    ResourceTool resourceTool = new ResourceTool();
    resourceTool.setId("resource1");
    resourceTool.setCount(1);

    testOrganisation.setId("org123");
    testUser.setId("user123");
    testResource.setOrganisation(testOrganisation);

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("USER");
    when(profileServiceSelector.selectService(any())).thenReturn(userDetailsService);
    when(userDetailsService.loadUserByUsername(any())).thenReturn(
        new UserDetails(testUser));

    when(organisationService.getOrganisationById("org123")).thenReturn(Optional.of(testOrganisation));
    when(userService.getUserById("user123")).thenReturn(Optional.of(testUser));
    when(resourceService.getResourceById(anyString())).thenReturn(Optional.of(testResource));
    when(resourceToolService.getResourceToolById("resource1")).thenReturn(Optional.of(resourceTool));
    when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(appointment);

    String requestBody = objectMapper.writeValueAsString(appointmentRequest);
    mockMvc.perform(post("/appointment")
            .header("Authorization", "Bearer Test-JWT-Token")
            .contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.date").value("12232024"))
        .andExpect(jsonPath("$.timeStart").value(32400000))
        .andExpect(jsonPath("$.timeEnd").value(61200000));

  }

  @Test
  void testCreateAppointment_Sad_Path_UserForbidden() throws Exception {
    Appointment appointment = new Appointment();
    appointment.setDate("12232024");
    appointment.setTimeStart(32400000);
    appointment.setTimeEnd(61200000);

    ResourceTool resourceTool = new ResourceTool();
    resourceTool.setId("resource1");
    resourceTool.setCount(1);

    testOrganisation.setId("org123");
    testResource.setOrganisation(testOrganisation);

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("USER");
    when(profileServiceSelector.selectService(any())).thenReturn(userDetailsService);
    when(userDetailsService.loadUserByUsername(any())).thenReturn(
        new UserDetails(testUser));

    when(organisationService.getOrganisationById("org123")).thenReturn(Optional.of(testOrganisation));
    when(userService.getUserById("user123")).thenReturn(Optional.of(testUser));
    when(resourceService.getResourceById(anyString())).thenReturn(Optional.of(testResource));
    when(resourceToolService.getResourceToolById("resource1")).thenReturn(Optional.of(resourceTool));
    when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(appointment);

    String requestBody = objectMapper.writeValueAsString(appointmentRequest);
    mockMvc.perform(post("/appointment")
            .header("Authorization", "Bearer Test-JWT-Token")
            .contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isForbidden());

  }

  @Test
  void testCreateAppointment_Sad_Path_OrganisationForbidden() throws Exception {
    Appointment appointment = new Appointment();
    appointment.setDate("12232024");
    appointment.setTimeStart(32400000);
    appointment.setTimeEnd(61200000);

    ResourceTool resourceTool = new ResourceTool();
    resourceTool.setId("resource1");
    resourceTool.setCount(1);

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
    when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
    when(organisationDetailsService.loadUserByUsername(any())).thenReturn(
        new OrganisationDetails(testOrganisation));

    when(organisationService.getOrganisationById("org123")).thenReturn(Optional.of(testOrganisation));
    when(userService.getUserById("user123")).thenReturn(Optional.of(testUser));
    when(resourceService.getResourceById(anyString())).thenReturn(Optional.of(testResource));
    when(resourceToolService.getResourceToolById("resource1")).thenReturn(Optional.of(resourceTool));
    when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(appointment);

    String requestBody = objectMapper.writeValueAsString(appointmentRequest);
    mockMvc.perform(post("/appointment")
            .header("Authorization", "Bearer Test-JWT-Token")
            .contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isForbidden());

  }

  @Test
  void testCreateAppointment_Sad_Path_ResourceNotAvailable() throws Exception {
    Appointment appointment = new Appointment();
    appointment.setDate("12232024");
    appointment.setTimeStart(32400000);
    appointment.setTimeEnd(61200000);

    ResourceTool resourceTool = new ResourceTool();
    resourceTool.setId("resource1");
    resourceTool.setCount(0);

    testOrganisation.setId("org123");
    testResource.setOrganisation(testOrganisation);

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
    when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
    when(organisationDetailsService.loadUserByUsername(any())).thenReturn(
        new OrganisationDetails(testOrganisation));

    when(organisationService.getOrganisationById("org123")).thenReturn(Optional.of(testOrganisation));
    when(userService.getUserById("user123")).thenReturn(Optional.of(testUser));
    when(resourceService.getResourceById(anyString())).thenReturn(Optional.of(testResource));
    when(resourceToolService.getResourceToolById(anyString())).thenReturn(Optional.of(resourceTool));
    when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(appointment);

    String requestBody = objectMapper.writeValueAsString(appointmentRequest);
    mockMvc.perform(post("/appointment")
            .header("Authorization", "Bearer Test-JWT-Token")
            .contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Resource not available: res123"));
  }

  @Test
  void testCreateAppointment_Sad_Path_ResourceInvalid() throws Exception {
    Appointment appointment = new Appointment();
    appointment.setDate("12232024");
    appointment.setTimeStart(32400000);
    appointment.setTimeEnd(61200000);

    ResourceTool resourceTool = new ResourceTool();
    resourceTool.setId("resource1");
    resourceTool.setCount(1);

    testOrganisation.setId("org123");
    Organisation newOrg = new Organisation();
    newOrg.setId("123");
    testResource.setOrganisation(newOrg);

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
    when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
    when(organisationDetailsService.loadUserByUsername(any())).thenReturn(
        new OrganisationDetails(testOrganisation));

    when(organisationService.getOrganisationById("org13")).thenReturn(Optional.of(testOrganisation));
    when(userService.getUserById("user123")).thenReturn(Optional.of(testUser));
    when(resourceService.getResourceById(anyString())).thenReturn(Optional.of(testResource));
    when(resourceToolService.getResourceToolById(anyString())).thenReturn(Optional.of(resourceTool));
    when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(appointment);

    String requestBody = objectMapper.writeValueAsString(appointmentRequest);
    mockMvc.perform(post("/appointment")
            .header("Authorization", "Bearer Test-JWT-Token")
            .contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Invalid Resource"));
  }

  @Test
  void testCreateAppointment_Sad_Path_InvalidAppointmentDate() throws Exception {
    Appointment appointment = new Appointment();
    appointment.setDate("12232024");
    appointment.setTimeStart(32400000);
    appointment.setTimeEnd(61200000);

    ResourceTool resourceTool = new ResourceTool();
    resourceTool.setId("resource1");
    resourceTool.setCount(1);

    testOrganisation.setId("org123");
    testResource.setOrganisation(testOrganisation);

    appointmentRequest.setDate("11252024");

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
    when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
    when(organisationDetailsService.loadUserByUsername(any())).thenReturn(
        new OrganisationDetails(testOrganisation));

    when(organisationService.getOrganisationById("org123")).thenReturn(Optional.of(testOrganisation));
    when(userService.getUserById("user123")).thenReturn(Optional.of(testUser));
    when(resourceService.getResourceById(anyString())).thenReturn(Optional.of(testResource));
    when(resourceToolService.getResourceToolById("resource1")).thenReturn(Optional.of(resourceTool));
    when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(appointment);

    String requestBody = objectMapper.writeValueAsString(appointmentRequest);
    mockMvc.perform(post("/appointment")
            .header("Authorization", "Bearer Test-JWT-Token")
            .contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Invalid date: date must be in the future"));
  }

  @Test
  void testCreateAppointment_Sad_Path_InvalidAppointmentDateFormat() throws Exception {
    Appointment appointment = new Appointment();
    appointment.setDate("12232024");
    appointment.setTimeStart(32400000);
    appointment.setTimeEnd(61200000);

    ResourceTool resourceTool = new ResourceTool();
    resourceTool.setId("resource1");
    resourceTool.setCount(1);

    testOrganisation.setId("org123");
    testResource.setOrganisation(testOrganisation);

    appointmentRequest.setDate("23122024");

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
    when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
    when(organisationDetailsService.loadUserByUsername(any())).thenReturn(
        new OrganisationDetails(testOrganisation));

    when(organisationService.getOrganisationById("org123")).thenReturn(Optional.of(testOrganisation));
    when(userService.getUserById("user123")).thenReturn(Optional.of(testUser));
    when(resourceService.getResourceById(anyString())).thenReturn(Optional.of(testResource));
    when(resourceToolService.getResourceToolById("resource1")).thenReturn(Optional.of(resourceTool));
    when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(appointment);

    String requestBody = objectMapper.writeValueAsString(appointmentRequest);
    mockMvc.perform(post("/appointment")
            .header("Authorization", "Bearer Test-JWT-Token")
            .contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Invalid date format: expected mmddyyyy"));
  }

  @Test
  void testCreateAppointment_Sad_Path_InvalidAppointmentTimeRange() throws Exception {
    Appointment appointment = new Appointment();
    appointment.setDate("12232024");
    appointment.setTimeStart(32400000);
    appointment.setTimeEnd(61200000);

    ResourceTool resourceTool = new ResourceTool();
    resourceTool.setId("resource1");
    resourceTool.setCount(1);

    testOrganisation.setId("org123");
    testResource.setOrganisation(testOrganisation);

    appointmentRequest.setTimeStart(61200000);
    appointmentRequest.setTimeEnd(32400000);

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
    when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
    when(organisationDetailsService.loadUserByUsername(any())).thenReturn(
        new OrganisationDetails(testOrganisation));

    when(organisationService.getOrganisationById("org123")).thenReturn(Optional.of(testOrganisation));
    when(userService.getUserById("user123")).thenReturn(Optional.of(testUser));
    when(resourceService.getResourceById(anyString())).thenReturn(Optional.of(testResource));
    when(resourceToolService.getResourceToolById("resource1")).thenReturn(Optional.of(resourceTool));
    when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(appointment);

    String requestBody = objectMapper.writeValueAsString(appointmentRequest);
    mockMvc.perform(post("/appointment")
            .header("Authorization", "Bearer Test-JWT-Token")
            .contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Invalid time range: timeStart must be before timeEnd"));
  }

  @Test
  void testCreateAppointment_Sad_Path_UserDoesNotExit() throws Exception {
    Appointment appointment = new Appointment();
    appointment.setDate("12232024");
    appointment.setTimeStart(32400000);
    appointment.setTimeEnd(61200000);

    ResourceTool resourceTool = new ResourceTool();
    resourceTool.setId("resource1");
    resourceTool.setCount(1);

    testOrganisation.setId("org123");
    testUser.setId("user123");
    testResource.setOrganisation(testOrganisation);

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("USER");
    when(profileServiceSelector.selectService(any())).thenReturn(userDetailsService);
    when(userDetailsService.loadUserByUsername(any())).thenReturn(
        new UserDetails(testUser));

    when(organisationService.getOrganisationById("org123")).thenReturn(Optional.of(testOrganisation));
    when(userService.getUserById("user123")).thenReturn(Optional.empty());
    when(resourceService.getResourceById(anyString())).thenReturn(Optional.of(testResource));
    when(resourceToolService.getResourceToolById("resource1")).thenReturn(Optional.of(resourceTool));
    when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(appointment);

    String requestBody = objectMapper.writeValueAsString(appointmentRequest);
    mockMvc.perform(post("/appointment")
            .header("Authorization", "Bearer Test-JWT-Token")
            .contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect((content().string("User does not exist")));
  }

  @Test
  void testCreateAppointment_Sad_Path_OrganisationDoesNotExit() throws Exception {
    Appointment appointment = new Appointment();
    appointment.setDate("12232024");
    appointment.setTimeStart(32400000);
    appointment.setTimeEnd(61200000);

    ResourceTool resourceTool = new ResourceTool();
    resourceTool.setId("resource1");
    resourceTool.setCount(1);

    testOrganisation.setId("org123");
    testResource.setOrganisation(testOrganisation);

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
    when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
    when(organisationDetailsService.loadUserByUsername(any())).thenReturn(
        new OrganisationDetails(testOrganisation));

    when(organisationService.getOrganisationById("org123")).thenReturn(Optional.empty());
    when(userService.getUserById("user123")).thenReturn(Optional.of(testUser));
    when(resourceService.getResourceById(anyString())).thenReturn(Optional.of(testResource));
    when(resourceToolService.getResourceToolById("resource1")).thenReturn(Optional.of(resourceTool));
    when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(appointment);

    String requestBody = objectMapper.writeValueAsString(appointmentRequest);
    mockMvc.perform(post("/appointment")
            .header("Authorization", "Bearer Test-JWT-Token")
            .contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Organisation does not exist"));
  }

  @Test
  void testCreateAppointment_Sad_Path_ResourceDoesNotExit() throws Exception {
    Appointment appointment = new Appointment();
    appointment.setDate("12232024");
    appointment.setTimeStart(32400000);
    appointment.setTimeEnd(61200000);

    ResourceTool resourceTool = new ResourceTool();
    resourceTool.setId("resource1");
    resourceTool.setCount(1);

    testOrganisation.setId("org123");
    testResource.setOrganisation(testOrganisation);

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
    when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
    when(organisationDetailsService.loadUserByUsername(any())).thenReturn(
        new OrganisationDetails(testOrganisation));

    when(organisationService.getOrganisationById("org123")).thenReturn(Optional.of(testOrganisation));
    when(userService.getUserById("user123")).thenReturn(Optional.of(testUser));
    when(resourceService.getResourceById(anyString())).thenReturn(Optional.empty());
    when(resourceToolService.getResourceToolById("resource1")).thenReturn(Optional.of(resourceTool));
    when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(appointment);

    String requestBody = objectMapper.writeValueAsString(appointmentRequest);
    mockMvc.perform(post("/appointment")
            .header("Authorization", "Bearer Test-JWT-Token")
            .contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Resource does not exist"));
  }

  @Test
  void testDeleteAppointment_Organisation () throws Exception {
    Appointment appointment = new Appointment();
    appointment.setDate("12232024");
    appointment.setTimeStart(32400000);
    appointment.setTimeEnd(61200000);
    appointment.setOrganisation(testOrganisation);

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
    when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
    when(organisationDetailsService.loadUserByUsername(any())).thenReturn(
        new OrganisationDetails(testOrganisation));

    when(appointmentService.getAppointmentById(anyString())).thenReturn(appointment);
    mockMvc.perform(delete("/appointment/1")
        .header("Authorization", "Bearer Test-JWT-Token"))
        .andExpect(status().isNoContent());
  }

  @Test
  void testDeleteAppointment_User () throws Exception {
    Appointment appointment = new Appointment();
    appointment.setDate("12232024");
    appointment.setTimeStart(32400000);
    appointment.setTimeEnd(61200000);
    appointment.setUser(testUser);

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("USER");
    when(profileServiceSelector.selectService(any())).thenReturn(userDetailsService);
    when(userDetailsService.loadUserByUsername(any())).thenReturn(
        new UserDetails(testUser));

    when(appointmentService.getAppointmentById(anyString())).thenReturn(appointment);
    mockMvc.perform(delete("/appointment/1")
            .header("Authorization", "Bearer Test-JWT-Token"))
        .andExpect(status().isNoContent());
  }

  @Test
  void testDeleteAppointment_UserForbidden () throws Exception {
    Appointment appointment = new Appointment();
    appointment.setDate("12232024");
    appointment.setTimeStart(32400000);
    appointment.setTimeEnd(61200000);

    User testNewUser = new User();
    testNewUser.setId("user55");
    appointment.setUser(testNewUser);


    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("USER");
    when(profileServiceSelector.selectService(any())).thenReturn(userDetailsService);
    when(userDetailsService.loadUserByUsername(any())).thenReturn(
        new UserDetails(testUser));

    when(appointmentService.getAppointmentById(anyString())).thenReturn(appointment);
    mockMvc.perform(delete("/appointment/1")
            .header("Authorization", "Bearer Test-JWT-Token"))
        .andExpect(status().isForbidden());
  }

  @Test
  void testDeleteAppointment_OrganisationForbidden () throws Exception {
    Appointment appointment = new Appointment();
    appointment.setDate("12232024");
    appointment.setTimeStart(32400000);
    appointment.setTimeEnd(61200000);

    Organisation testNewOrganisation = new Organisation();
    testNewOrganisation.setId("org55");
    appointment.setOrganisation(testNewOrganisation);

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
    when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
    when(organisationDetailsService.loadUserByUsername(any())).thenReturn(
        new OrganisationDetails(testOrganisation));

    when(appointmentService.getAppointmentById(anyString())).thenReturn(appointment);
    mockMvc.perform(delete("/appointment/1")
            .header("Authorization", "Bearer Test-JWT-Token"))
        .andExpect(status().isForbidden());
  }

  @Test
  void testAddVolunteerToAppointment_Happy_Path () throws Exception {
    Appointment appointment = new Appointment();
    appointment.setDate("12232024");
    appointment.setTimeStart(32400000);
    appointment.setTimeEnd(61200000);

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("VOLUNTEER");
    when(profileServiceSelector.selectService(any())).thenReturn(volunteerDetailsService);
    when(volunteerDetailsService.loadUserByUsername(any())).thenReturn(
        new VolunteerDetails(testVolunteer));

    when(appointmentService.getAppointmentById(anyString())).thenReturn(appointment);
    when(volunteerService.getVolunteerById(anyString())).thenReturn(Optional.of(testVolunteer));
    mockMvc.perform(post("/appointment/1/volunteer")
            .header("Authorization", "Bearer Test-JWT-Token"))
        .andExpect(status().isOk());
  }

  @Test
  void testAddVolunteerToAppointment_Sad_Path () throws Exception {
    Appointment appointment = new Appointment();
    appointment.setDate("12232024");
    appointment.setTimeStart(32400000);
    appointment.setTimeEnd(61200000);
    appointment.setVolunteer(testVolunteer);

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("VOLUNTEER");
    when(profileServiceSelector.selectService(any())).thenReturn(volunteerDetailsService);
    when(volunteerDetailsService.loadUserByUsername(any())).thenReturn(
        new VolunteerDetails(testVolunteer));

    when(appointmentService.getAppointmentById(anyString())).thenReturn(appointment);
    mockMvc.perform(post("/appointment/1/volunteer")
            .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isBadRequest())
        .andExpect(content().string("Appointment already has a volunteer assigned"));
  }



}
