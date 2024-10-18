package com.garlicbread.includify.controller.volunteer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garlicbread.includify.config.SecurityConfig;
import com.garlicbread.includify.entity.volunteer.Volunteer;
import com.garlicbread.includify.profile.volunteer.VolunteerDetails;
import com.garlicbread.includify.service.auth.ProfileServiceSelector;
import com.garlicbread.includify.service.auth.VolunteerDetailsService;
import com.garlicbread.includify.service.volunteer.VolunteerService;
import java.util.Collections;
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

@WebMvcTest(VolunteerController.class)
@Import(SecurityConfig.class)
public class VolunteerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VolunteerService volunteerService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private ProfileServiceSelector profileServiceSelector;

    @Autowired
    private VolunteerDetailsService volunteerDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private Jwt jwt;

    private Volunteer testVolunteer;

    @BeforeEach
    void setUp() {
        testVolunteer = new Volunteer();
        testVolunteer.setEmail("rb3713@columbia.edu");
        testVolunteer.setName("Rahaf Ibrahim");

        when(jwtDecoder.decode(any())).thenReturn(jwt);
        when(jwt.getClaimAsString("sub")).thenReturn("test_user");
        when(jwt.getClaimAsString("profile")).thenReturn("VOLUNTEER");

        when(profileServiceSelector.selectService(any())).thenReturn(volunteerDetailsService);
        when(volunteerDetailsService.loadUserByUsername(any())).thenReturn(new VolunteerDetails(testVolunteer));
    }

    @Test
    void testGetAllVolunteers_Happy_Path() throws Exception {
        when(volunteerService.getAllVolunteers()).thenReturn(Collections.singletonList(testVolunteer));

        mockMvc.perform(get("/volunteer/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email").value("rb3713@columbia.edu"))
                .andExpect(jsonPath("$[0].name").value("Rahaf Ibrahim"));
    }

    @Test
    void testGetAllVolunteers_Sad_Path() throws Exception {
        when(volunteerService.getAllVolunteers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/volunteer/all"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getVolunteerById_Happy_Path() throws Exception {
        when(volunteerService.getVolunteerById("1")).thenReturn(Optional.of(testVolunteer));

        mockMvc.perform(get("/volunteer/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("rb3713@columbia.edu"))
                .andExpect(jsonPath("$.name").value("Rahaf Ibrahim"));
    }

    @Test
    void getVolunteerById_Sad_Path() throws Exception {
        when(volunteerService.getVolunteerById("22")).thenReturn(Optional.empty());

        mockMvc.perform(get("/volunteer/22"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Volunteer not found with id: 22"));
    }

    @Test
    void createVolunteer_Happy_Path() throws Exception {
        when(volunteerService.addVolunteer(any(Volunteer.class))).thenReturn(testVolunteer);

        String requestBody = objectMapper.writeValueAsString(testVolunteer);

        mockMvc.perform(post("/volunteer/add").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Rahaf Ibrahim"));
    }

    @Test
    void deleteVolunteer_Happy_Path() throws Exception {
        when(volunteerService.getVolunteerById(anyString())).thenReturn(Optional.of(testVolunteer));

        mockMvc.perform(delete("/volunteer/delete/testId").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer testJWTToken"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("Volunteer deleted successfully"));
    }

    @Test
    void deleteVolunteer_Sad_Path() throws Exception {
        when(volunteerService.getVolunteerById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/volunteer/delete/testId").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer testJWTToken"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Volunteer not found with id: testId"));
    }
}
