package com.garlicbread.includify.controller.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garlicbread.includify.config.SecurityConfig;
import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.entity.resource.ResourceType;
import com.garlicbread.includify.entity.user.UserCategory;
import com.garlicbread.includify.model.resource.ResourceRequest;
import com.garlicbread.includify.profile.organisation.OrganisationDetails;
import com.garlicbread.includify.service.auth.OrganisationDetailsService;
import com.garlicbread.includify.service.auth.ProfileServiceSelector;
import com.garlicbread.includify.service.organisation.OrganisationService;
import com.garlicbread.includify.service.resource.ResourceService;
import com.garlicbread.includify.service.resource.ResourceTypeService;
import com.garlicbread.includify.service.user.UserCategoryService;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    @MockBean
    private ResourceService resourceService;

    @MockBean
    private ResourceTypeService resourceTypeService;

    @MockBean
    private OrganisationService organisationService;

    @MockBean
    private UserCategoryService userCategoryService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private ProfileServiceSelector profileServiceSelector;

    @MockBean
    private OrganisationDetailsService organisationDetailsService;

    @Mock
    private Jwt jwt;

    @Autowired
    private ObjectMapper objectMapper;

    private Resource testResource;
    private ResourceType testResourceType;
    private Organisation testOrganisation;
    private UserCategory testUserCategory;

    @BeforeEach
    void setup() {
        testOrganisation = new Organisation();
        testOrganisation.setId("1");
        testOrganisation.setName("Test Organisation");
        testOrganisation.setEmail("test@organisation.com");
        testOrganisation.setPassword("password123");
        testOrganisation.setAddress("123 Test St");
        testOrganisation.setLatitude("40.7128");
        testOrganisation.setLongitude("-74.0060");
        testOrganisation.setDescription("Test Description");

        testResourceType = new ResourceType();
        testResourceType.setTitle("Test Resource Type");
        testResourceType.setDescription("Test Resource Type Description");

        testUserCategory = new UserCategory();
        testUserCategory.setTitle("Test User Category");

        testResource = new Resource();
        testResource.setTitle("Test Resource");
        testResource.setDescription("Resource added for testing");
        testResource.setUsageInstructions("Test usage instructions");
        testResource.setResourceType(new ArrayList<>());
        testResource.setOrganisation(testOrganisation);

        when(jwtDecoder.decode(any())).thenReturn(jwt);
        when(jwt.getClaimAsString("sub")).thenReturn("test_user");
        when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
        when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
        when(organisationDetailsService.loadUserByUsername(any())).thenReturn(
            new OrganisationDetails(testOrganisation));
    }

    @Test
    void testCreateAndGetResource_Happy_Path() throws Exception {
        ResourceRequest resourceRequest = new ResourceRequest();
        resourceRequest.setOrganisationId(testOrganisation.getId());
        resourceRequest.setTitle("Test Resource");
        resourceRequest.setDescription("Resource added for testing");
        resourceRequest.setUsageInstructions("Test usage instructions");
        resourceRequest.setResourceTypeIds(List.of("1"));
        resourceRequest.setTargetUserCategoryIds(List.of("1"));

        when(organisationService.getOrganisationById(anyString())).thenReturn(Optional.of(testOrganisation));
        when(resourceService.addResource(any(Resource.class))).thenReturn(testResource);
        when(resourceTypeService.getById(anyString())).thenReturn(testResourceType);
        when(userCategoryService.getById(anyString())).thenReturn(testUserCategory);

        String requestBody = objectMapper.writeValueAsString(resourceRequest);

        mockMvc.perform(post("/resource/add")
                .header("Authorization", "Bearer Test-JWT-Token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Test Resource"))
            .andExpect(jsonPath("$.description").value("Resource added for testing"));
    }

    @Test
    void testGetAllResources_Happy_Path() throws Exception {
        when(resourceService.getAllResources()).thenReturn(Collections.singletonList(testResource));

        mockMvc.perform(get("/resource/all")
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("Test Resource"))
            .andExpect(jsonPath("$[0].description").value("Resource added for testing"));
    }

    @Test
    void testGetAllResources_Sad_Path() throws Exception {
        when(resourceService.getAllResources()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/resource/all")
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteResource_Happy_Path() throws Exception {
        Organisation organisation = new Organisation();
        organisation.setId("1");
        testResource.setId("1");
        testResource.setOrganisation(organisation);
        testResource.setTargetUserCategory(new ArrayList<>());
        testResource.setResourceType(new ArrayList<>());

        when(resourceService.getResourceById(anyString())).thenReturn(Optional.of(testResource));
        doNothing().when(resourceService).deleteResource(anyString());

        mockMvc.perform(delete("/resource/delete/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer testJWTToken"))
            .andExpect(status().isNoContent())
            .andExpect(content().string("Resource deleted successfully"));

        verify(resourceService, times(1)).deleteResource("1");
    }

    @Test
    void testDeleteResource_Sad_Path() throws Exception {
        when(resourceService.getResourceById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/resource/delete/{id}", "non_existent_id")
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Resource not found with id: non_existent_id"));
    }
}