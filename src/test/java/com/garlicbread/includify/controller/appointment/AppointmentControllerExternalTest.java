package com.garlicbread.includify.controller.appointment;

import com.garlicbread.includify.config.SecurityConfig;

import static org.hibernate.internal.util.collections.CollectionHelper.listOf;
import static org.mockito.ArgumentMatchers.*;

import com.garlicbread.includify.entity.appointment.Appointment;
import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.entity.resource.ResourceType;
import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.entity.volunteer.Volunteer;
import com.garlicbread.includify.model.appointment.AppointmentRequest;
import com.garlicbread.includify.profile.organisation.OrganisationDetails;
import com.garlicbread.includify.service.appointment.AppointmentService;
import com.garlicbread.includify.service.auth.OrganisationDetailsService;
import com.garlicbread.includify.service.auth.ProfileServiceSelector;
import com.garlicbread.includify.service.organisation.OrganisationService;
import com.garlicbread.includify.service.resource.ResourceService;
import com.garlicbread.includify.service.resource.ResourceTypeService;
import com.garlicbread.includify.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@Transactional
public class AppointmentControllerExternalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganisationService organisationService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceTypeService resourceTypeService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private ProfileServiceSelector profileServiceSelector;

    @MockBean
    private OrganisationDetailsService organisationDetailsService;

    @Mock
    private Jwt jwt;

    private Organisation testOrganisation;
    private User testUser;
    private Volunteer testVolunteer;
    private AppointmentRequest appointmentRequest;
    private Resource testResource;
    private ResourceType testResourceType;

    @BeforeEach
    void setup() {
        appointmentRequest = new AppointmentRequest();
        appointmentRequest.setOrganisationId("org123");
        appointmentRequest.setDate("12252024");
        appointmentRequest.setTimeStart(10);
        appointmentRequest.setTimeEnd(12);
        appointmentRequest.setResourceIds(List.of("res123"));

        testOrganisation = new Organisation();
        testOrganisation.setAddress("New York");
        testOrganisation.setEmail("test@cu.com");
        testOrganisation.setPassword("pass");
        testOrganisation.setLatitude("12.89");
        testOrganisation.setLongitude("-12.89");
        testOrganisation.setDescription("Ivy League University");
        testOrganisation.setName("Columbia University");

        testUser = new User();
        testUser.setAge(23);
        testUser.setPassword("test");
        testUser.setEmail("ps014566@gmail.com");
        testUser.setName("Puja S");

        testVolunteer = new Volunteer();
        testVolunteer.setEmail("ps6789@columbia.edu");
        testVolunteer.setName("Puja S");
        testVolunteer.setAge(25);
        testVolunteer.setAddress("116st, NY");
        testVolunteer.setPhone("929-428-6666");

        testResourceType = new ResourceType();
        testResourceType.setTitle("infrastructure");
        testResourceType.setDescription("infrastructure resource category.");

        testResource = new Resource();
        testResource.setTitle("Test Resource");
        testResource.setDescription("Resource added for testing.");
        testResource.setUsageInstructions("set usage instructions");
        testResource.setResourceType(listOf(testResourceType));
        testResource.setOrganisation(testOrganisation);

        userService.createUser(testUser);
        organisationService.createOrganisation(testOrganisation);
        resourceTypeService.createResourceType(testResourceType);
        resourceService.addResource(testResource);
    }

    @Test
    void testGetAllAppointmentsForOrganisation_Happy_Path() throws Exception  {
        Appointment appointment = new Appointment();
        appointment.setResources(listOf(testResource));
        appointment.setOrganisation(testOrganisation);
        appointment.setUser(testUser);
        appointment.setDate("12252024");
        appointment.setTimeStart(10);
        appointment.setTimeEnd(12);

        appointmentService.createAppointment(appointment);

        when(jwtDecoder.decode(any())).thenReturn(jwt);
        when(jwt.getClaimAsString("sub")).thenReturn("test_user");
        when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
        when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
        when(organisationDetailsService.loadUserByUsername(any())).thenReturn(
            new OrganisationDetails(testOrganisation));

        mockMvc.perform(get("/appointment/organisation")
                .header("Authorization", "Bearer Test-JWT-Token")
                .param("organisation", organisationService.getAllOrganisations().get(0).getId()))
            .andExpect(jsonPath("$[0].date").value("12252024"))
            .andExpect(jsonPath("$[0].timeStart").value(10))
            .andExpect(jsonPath("$[0].timeEnd").value(12))
            .andExpect(status().isOk());
    }

}
