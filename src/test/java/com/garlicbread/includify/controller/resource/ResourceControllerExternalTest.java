package com.garlicbread.includify.controller.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garlicbread.includify.config.SecurityConfig;
import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.entity.resource.ResourceType;
import com.garlicbread.includify.model.resource.ResourceRequest;
import com.garlicbread.includify.profile.organisation.OrganisationDetails;
import com.garlicbread.includify.service.auth.OrganisationDetailsService;
import com.garlicbread.includify.service.auth.ProfileServiceSelector;
import com.garlicbread.includify.service.organisation.OrganisationService;
import com.garlicbread.includify.service.resource.ResourceService;
import com.garlicbread.includify.service.resource.ResourceTypeService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@Transactional
public class ResourceControllerExternalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private OrganisationService organisationService;

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
    private ResourceType testResourceType;
    private Resource testResource;
    private ResourceRequest resourceRequest;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        testOrganisation = new Organisation();
        testOrganisation.setName("Test Organisation");
        testOrganisation.setEmail("test@org.com");
        testOrganisation.setPassword("securePassword123");
        testOrganisation.setAddress("123 Test Street, Test City, 12345");
        testOrganisation.setLatitude("40.7128");
        testOrganisation.setLongitude("-74.0060");
        testOrganisation.setDescription("A test organisation for unit testing");
        organisationService.createOrganisation(testOrganisation);

        testResourceType = new ResourceType();
        testResourceType.setTitle("Test Resource Type");
        testResourceType.setDescription("Test Resource Type Description");
        resourceTypeService.createResourceType(testResourceType);

        testResource = new Resource();
        testResource.setTitle("Test Resource");
        testResource.setDescription("Test Resource Description");
        testResource.setUsageInstructions("Test Usage Instructions");
        testResource.setResourceType(List.of(testResourceType));
        testResource.setOrganisation(testOrganisation);
        resourceService.addResource(testResource);

        resourceRequest = new ResourceRequest();
        resourceRequest.setOrganisationId(testOrganisation.getId());
        resourceRequest.setTitle("New Test Resource");
        resourceRequest.setDescription("New Test Resource Description");
        resourceRequest.setUsageInstructions("New Test Usage Instructions");
        resourceRequest.setResourceTypeIds(List.of(String.valueOf(testResourceType.getId())));

        when(jwtDecoder.decode(any())).thenReturn(jwt);
        when(jwt.getClaimAsString("sub")).thenReturn("test_user");
        when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
        when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
        when(organisationDetailsService.loadUserByUsername(any())).thenReturn(new OrganisationDetails(testOrganisation));
    }

    @Test
    void testGetAllResources_Happy_Path() throws Exception {
        mockMvc.perform(get("/resource/all")
                .header("Authorization", "Bearer Test-JWT-Token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Resource"))
                .andExpect(jsonPath("$[0].description").value("Test Resource Description"));
    }

    @Test
    void testGetResourceById_Happy_Path() throws Exception {
        String resourceId = resourceService.getAllResources().get(0).getId();
        mockMvc.perform(get("/resource/{id}", resourceId)
                .header("Authorization", "Bearer Test-JWT-Token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Resource"))
                .andExpect(jsonPath("$.description").value("Test Resource Description"));
    }

    @Test
    void testGetResourceById_Sad_Path() throws Exception {
        mockMvc.perform(get("/resource/{id}", "non_existent_id")
                .header("Authorization", "Bearer Test-JWT-Token"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Resource not found with id: non_existent_id"));
    }

    @Test
    void testAddResource_Happy_Path() throws Exception {
        mockMvc.perform(post("/resource/add")
                .header("Authorization", "Bearer Test-JWT-Token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resourceRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Test Resource"))
                .andExpect(jsonPath("$.description").value("New Test Resource Description"));
    }

    @Test
    void testAddResource_Sad_Path_InvalidOrganisation() throws Exception {
        resourceRequest.setOrganisationId("non_existent_id");
        mockMvc.perform(post("/resource/add")
                .header("Authorization", "Bearer Test-JWT-Token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resourceRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteResource_ServerError() throws Exception {
        String resourceId = resourceService.getAllResources().get(0).getId();
        mockMvc.perform(delete("/resource/delete/{id}", resourceId)
                .header("Authorization", "Bearer Test-JWT-Token"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error !!!"));
    }

    @Test
    void testDeleteResource_Sad_Path() throws Exception {
        mockMvc.perform(delete("/resource/delete/{id}", "non_existent_id")
                .header("Authorization", "Bearer Test-JWT-Token"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Resource not found with id: non_existent_id"));
    }
}