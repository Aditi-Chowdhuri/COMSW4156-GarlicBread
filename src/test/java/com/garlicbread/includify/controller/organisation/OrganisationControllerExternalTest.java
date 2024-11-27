package com.garlicbread.includify.controller.organisation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garlicbread.includify.config.SecurityConfig;
import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.service.organisation.OrganisationService;
import com.garlicbread.includify.util.Profile;
import com.garlicbread.includify.service.auth.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@Transactional
public class OrganisationControllerExternalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrganisationService organisationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenService tokenService;

    private Organisation testOrganisation;
    private String authToken;

    @BeforeEach
    void setUp() {
        testOrganisation = new Organisation();
        testOrganisation.setAddress("New York");
        testOrganisation.setEmail("test@cu.com");
        testOrganisation.setPassword("password");
        testOrganisation.setLatitude("12.89");
        testOrganisation.setLongitude("-12.89");
        testOrganisation.setDescription("Ivy League University");
        testOrganisation.setName("Columbia University");

        // Generate auth token for tests
        String username = testOrganisation.getEmail();
        Profile profile = Profile.ORGANISATION;
        List<String> authorities = Collections.singletonList("ROLE_ORGANISATION");
        authToken = tokenService.generateToken(username, profile, authorities);
    }

    @Test
    void testCreateAndGetOrganisation() throws Exception {
        String requestBody = objectMapper.writeValueAsString(testOrganisation);

        // Create organisation
        String responseContent = mockMvc.perform(post("/organisation/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Columbia University"))
            .andReturn().getResponse().getContentAsString();

        Organisation createdOrganisation = objectMapper.readValue(responseContent, Organisation.class);

        // Get organisation by ID
        mockMvc.perform(get("/organisation/" + createdOrganisation.getId())
                .header("Authorization", "Bearer " + authToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("test@cu.com"))
            .andExpect(jsonPath("$.name").value("Columbia University"))
            .andExpect(jsonPath("$.latitude").value("12.89"))
            .andExpect(jsonPath("$.longitude").value("-12.89"))
            .andExpect(jsonPath("$.description").value("Ivy League University"));
    }

    @Test
    void testUpdateOrganisation() throws Exception {
        // Create organisation
        Organisation createdOrganisation = organisationService.createOrganisation(testOrganisation);

        // Update organisation
        createdOrganisation.setAddress("Updated Address");
        String updateRequestBody = objectMapper.writeValueAsString(createdOrganisation);

        mockMvc.perform(put("/organisation/update/" + createdOrganisation.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequestBody)
                .header("Authorization", "Bearer " + authToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.address").value("Updated Address"));
    }

    @Test
    void testDeleteOrganisation() throws Exception {
        // Create organisation
        Organisation createdOrganisation = organisationService.createOrganisation(testOrganisation);

        // Delete organisation
        mockMvc.perform(delete("/organisation/delete/" + createdOrganisation.getId())
                .header("Authorization", "Bearer " + authToken))
            .andExpect(status().isNoContent());

        // Verify organisation is deleted
        mockMvc.perform(get("/organisation/" + createdOrganisation.getId())
                .header("Authorization", "Bearer " + authToken))
            .andExpect(status().isNotFound());
    }
}