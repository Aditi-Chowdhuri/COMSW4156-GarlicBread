package com.garlicbread.includify.controller.organisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garlicbread.includify.config.SecurityConfig;
import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.profile.organisation.OrganisationDetails;
import com.garlicbread.includify.service.auth.OrganisationDetailsService;
import com.garlicbread.includify.service.auth.ProfileServiceSelector;
import com.garlicbread.includify.service.organisation.OrganisationService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@Transactional
public class OrganisationControllerExternalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrganisationService organisationService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private ProfileServiceSelector profileServiceSelector;

    @MockBean
    private OrganisationDetailsService organisationDetailsService;

    @Mock
    private Jwt jwt;

    private Organisation testOrganisation;

    @BeforeEach
    void setup() {
        testOrganisation = new Organisation();
        testOrganisation.setName("Test Organisation");
        testOrganisation.setEmail("test@organisation.com");
        testOrganisation.setPassword("password123");
        testOrganisation.setAddress("123 Test St");
        testOrganisation.setLatitude("40.7128");
        testOrganisation.setLongitude("-74.0060");
        testOrganisation.setDescription("Test Description");

        when(jwtDecoder.decode(any())).thenReturn(jwt);
        when(jwt.getClaimAsString("sub")).thenReturn("test_user");
        when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
        when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
        when(organisationDetailsService.loadUserByUsername(any())).thenReturn(
            new OrganisationDetails(testOrganisation));
    }

    @Test
    void testCreateAndGetOrganisation_Happy_Path() throws Exception {
        String organisationJson = new ObjectMapper().writeValueAsString(testOrganisation);

        String createdOrganisationId = mockMvc.perform(post("/organisation")
                .header("Authorization", "Bearer Test-JWT-Token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(organisationJson))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/organisation/{id}", createdOrganisationId)
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Test Organisation"))
            .andExpect(jsonPath("$.email").value("test@organisation.com"));
    }

    @Test
    void testUpdateOrganisation_Happy_Path() throws Exception {
        Organisation savedOrganisation = organisationService.createOrganisation(testOrganisation);
        savedOrganisation.setName("Updated Organisation");
        String updatedOrganisationJson = new ObjectMapper().writeValueAsString(savedOrganisation);

        mockMvc.perform(put("/organisation/{id}", savedOrganisation.getId())
                .header("Authorization", "Bearer Test-JWT-Token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedOrganisationJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Organisation"));
    }

    @Test
    void testDeleteOrganisation_Happy_Path() throws Exception {
        Organisation savedOrganisation = organisationService.createOrganisation(testOrganisation);

        mockMvc.perform(delete("/organisation/{id}", savedOrganisation.getId())
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/organisation/{id}", savedOrganisation.getId())
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllOrganisations_Happy_Path() throws Exception {
        organisationService.createOrganisation(testOrganisation);

        mockMvc.perform(get("/organisation/all")
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Test Organisation"))
            .andExpect(jsonPath("$[0].email").value("test@organisation.com"));
    }

    @Test
    void testGetOrganisationById_Sad_Path() throws Exception {
        mockMvc.perform(get("/organisation/{id}", "non_existent_id")
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateOrganisation_Sad_Path() throws Exception {
        String updatedOrganisationJson = new ObjectMapper().writeValueAsString(testOrganisation);

        mockMvc.perform(put("/organisation/{id}", "non_existent_id")
                .header("Authorization", "Bearer Test-JWT-Token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedOrganisationJson))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteOrganisation_Sad_Path() throws Exception {
        mockMvc.perform(delete("/organisation/{id}", "non_existent_id")
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isNotFound());
    }
}