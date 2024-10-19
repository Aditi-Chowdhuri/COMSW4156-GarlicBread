package com.garlicbread.includify.controller.resource;

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
import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.entity.resource.ResourceType;
import com.garlicbread.includify.entity.user.UserCategory;
import com.garlicbread.includify.model.resource.ResourceRequest;
import com.garlicbread.includify.profile.organisation.OrganisationDetails;
import com.garlicbread.includify.service.auth.OrganisationDetailsService;
import com.garlicbread.includify.service.auth.ProfileServiceSelector;
import com.garlicbread.includify.service.auth.UserDetailsService;
import com.garlicbread.includify.service.auth.VolunteerDetailsService;
import com.garlicbread.includify.service.organisation.OrganisationService;
import com.garlicbread.includify.service.resource.ResourceService;
import com.garlicbread.includify.service.resource.ResourceTypeService;
import com.garlicbread.includify.service.user.UserCategoryService;
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
 * Unit tests for the ResourceController class.
 */
@WebMvcTest(ResourceController.class)
@Import(SecurityConfig.class)
public class ResourceControllerTest {
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
  @MockBean
  private VolunteerDetailsService volunteerDetailsService;

  @MockBean
  private UserDetailsService userDetailsService;
  @Mock
  private Jwt jwt;
  @Autowired
  private ObjectMapper objectMapper;
  private Resource testResource;
  private ResourceType testResourceType;
  private Organisation testOrganisation;
  private UserCategory testUserCategory;

  @BeforeEach
  void setUp() {
    testOrganisation = new Organisation();
    testOrganisation.setName("Columbia University");
    testResourceType = new ResourceType();
    testResourceType.setTitle("infrastructure");
    testResourceType.setDescription("infrastructure resource category.");
    testUserCategory = new UserCategory();
    testUserCategory.setTitle("senior_citizen");
    testResource = new Resource();
    testResource.setTitle("Test Resource");
    testResource.setDescription("Resource added for testing.");
    testResource.setUsageInstructions("set usage instructions");
    testResource.setOrganisation(testOrganisation);
    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("ORGANISATION");
    when(profileServiceSelector.selectService(any())).thenReturn(organisationDetailsService);
    when(organisationDetailsService.loadUserByUsername(any())).thenReturn(
        new OrganisationDetails(testOrganisation));
  }

  @Test
  void testGetAllResources_Happy_Path() throws Exception {
    when(resourceService.getAllResources()).thenReturn(Collections.singletonList(testResource));
    mockMvc.perform(get("/resource/all")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].title").value("Test Resource"));
  }

  @Test
  void testGetAllResources_Sad_Path() throws Exception {
    when(resourceService.getAllResources()).thenReturn(Collections.emptyList());
    mockMvc.perform(get("/resource/all")).andExpect(status().isNoContent());
  }

  @Test
  void getResourceById_Happy_Path() throws Exception {
    when(resourceService.getResourceById("1")).thenReturn(Optional.of(testResource));
    mockMvc.perform(get("/resource/1")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title").value("Test Resource"));
  }

  @Test
  void getResourceById_Sad_Path() throws Exception {
    when(resourceService.getResourceById("999")).thenReturn(Optional.empty());
    mockMvc.perform(get("/resource/999")).andExpect(status().isNotFound())
        .andExpect(content().string("Resource not found with id: 999"));
  }

  @Test
  void createResourceType_Happy_Path() throws Exception {
    when(resourceTypeService.createResourceType(any(ResourceType.class))).thenReturn(
        testResourceType);
    String requestBody = objectMapper.writeValueAsString(testResourceType);

    mockMvc.perform(post("/resource/createResourceType").contentType(MediaType.APPLICATION_JSON)
            .content(requestBody).header("Authorization", "Bearer testJWTToken"))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title").value("infrastructure"));
  }

  @Test
  void addResource_Happy_Path() throws Exception {
    when(organisationService.getOrganisationById(anyString())).thenReturn(
        Optional.of(testOrganisation));
    when(resourceService.addResource(any(Resource.class))).thenReturn(testResource);
    when(resourceTypeService.getById(anyString())).thenReturn(testResourceType);
    when(userCategoryService.getById(anyString())).thenReturn(testUserCategory);
    ResourceRequest resourceRequest = new ResourceRequest();
    resourceRequest.setOrganisationId("1");
    resourceRequest.setTitle("Test Resource");
    resourceRequest.setDescription("Description test");
    resourceRequest.setUsageInstructions("Usage instructions");
    resourceRequest.setResourceTypeIds(List.of("1"));
    resourceRequest.setTargetUserCategoryIds(List.of("1"));
    String requestBody = objectMapper.writeValueAsString(resourceRequest);
    mockMvc.perform(
            post("/resource/add").contentType(MediaType.APPLICATION_JSON).content(requestBody)
                .header("Authorization", "Bearer testJWTToken")).andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title").value("Test Resource"));
  }

  @Test
  void deleteResource_Happy_Path() throws Exception {
    when(resourceService.getResourceById(anyString())).thenReturn(Optional.of(testResource));
    mockMvc.perform(delete("/resource/delete/1").contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer testJWTToken")).andExpect(status().isNoContent())
        .andExpect(content().string("Resource deleted successfully"));
  }

  @Test
  void deleteResource_Sad_Path() throws Exception {
    when(resourceService.getResourceById(anyString())).thenReturn(Optional.empty());
    mockMvc.perform(delete("/resource/delete/1").contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer testJWTToken")).andExpect(status().isNotFound())
        .andExpect(content().string("Resource not found with id: 1"));
  }
}