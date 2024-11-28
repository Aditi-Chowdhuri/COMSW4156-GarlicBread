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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@Transactional
public class VolunteerControllerExternalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VolunteerService volunteerService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private ProfileServiceSelector profileServiceSelector;

    @MockBean
    private VolunteerDetailsService volunteerDetailsService;

    @Mock
    private Jwt jwt;

    @Autowired
    private ObjectMapper objectMapper;

    private Volunteer testVolunteer;

    @BeforeEach
    void setup() {
        testVolunteer = new Volunteer();
        testVolunteer.setId("testId");
        testVolunteer.setEmail("rb3713@columbia.edu");
        testVolunteer.setName("Rahaf Ibrahim");
        testVolunteer.setAge(25);
        testVolunteer.setAddress("116st, NY");
        testVolunteer.setPhone("929-428-6666");

        when(jwtDecoder.decode(any())).thenReturn(jwt);
        when(jwt.getClaimAsString("sub")).thenReturn("test_user");
        when(jwt.getClaimAsString("profile")).thenReturn("VOLUNTEER");
        when(profileServiceSelector.selectService(any())).thenReturn(volunteerDetailsService);
        when(volunteerDetailsService.loadUserByUsername(any())).thenReturn(
            new VolunteerDetails(testVolunteer));
    }

    @Test
    void testGetAllVolunteers_Happy_Path() throws Exception {
        when(volunteerService.getAllVolunteers()).thenReturn(Collections.singletonList(testVolunteer));

        mockMvc.perform(get("/volunteer/all")
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].email").value("rb3713@columbia.edu"))
            .andExpect(jsonPath("$[0].name").value("Rahaf Ibrahim"));
    }

    @Test
    void testGetAllVolunteers_Sad_Path() throws Exception {
        when(volunteerService.getAllVolunteers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/volunteer/all")
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isNoContent());
    }

    @Test
    void getVolunteerById_Happy_Path() throws Exception {
        when(volunteerService.getVolunteerById("testId")).thenReturn(Optional.of(testVolunteer));

        mockMvc.perform(get("/volunteer/testId")
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.email").value("rb3713@columbia.edu"))
            .andExpect(jsonPath("$.name").value("Rahaf Ibrahim"));
    }

    @Test
    void getVolunteerById_Sad_Path() throws Exception {
        when(volunteerService.getVolunteerById("non_existent_id")).thenReturn(Optional.empty());

        mockMvc.perform(get("/volunteer/non_existent_id")
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Volunteer not found with id: non_existent_id"));
    }

    @Test
    void createVolunteer_Happy_Path() throws Exception {
        when(volunteerService.addVolunteer(any(Volunteer.class))).thenReturn(testVolunteer);

        String requestBody = objectMapper.writeValueAsString(testVolunteer);
        String requestBodyWithPassword = requestBody.substring(0, requestBody.length() - 1) + ",\"password\":\"Test Password\"}";

        mockMvc.perform(post("/volunteer/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyWithPassword)
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("Rahaf Ibrahim"));
    }

    @Test
    void deleteVolunteer_Happy_Path() throws Exception {
        when(volunteerService.getVolunteerById(anyString())).thenReturn(Optional.of(testVolunteer));

        mockMvc.perform(delete("/volunteer/delete/testId")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isNoContent())
            .andExpect(content().string("Volunteer deleted successfully"));
    }

    @Test
    void deleteVolunteer_Sad_Path() throws Exception {
        when(volunteerService.getVolunteerById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/volunteer/delete/non_existent_id")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isForbidden());
    }
}