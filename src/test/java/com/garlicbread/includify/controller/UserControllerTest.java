package com.garlicbread.includify.controller;

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
import com.garlicbread.includify.controller.user.UserController;
import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.entity.user.UserCategory;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.profile.organisation.OrganisationDetails;
import com.garlicbread.includify.profile.user.UserDetails;
import com.garlicbread.includify.service.auth.OrganisationDetailsService;
import com.garlicbread.includify.service.auth.ProfileServiceSelector;
import com.garlicbread.includify.service.auth.UserDetailsService;
import com.garlicbread.includify.service.auth.VolunteerDetailsService;
import com.garlicbread.includify.service.user.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link UserController}.
 */
@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private JwtDecoder jwtDecoder;

  @MockBean
  private ProfileServiceSelector profileServiceSelector;

  @MockBean
  private UserDetailsService userDetailsService;

  @MockBean
  private VolunteerDetailsService volunteerDetailsService;

  @MockBean
  private OrganisationDetailsService organisationDetailsService;

  @Mock
  private Jwt jwt;

  @Autowired
  private ObjectMapper objectMapper;

  private User testUser;

  UserCategory category1 = new UserCategory("Category1");
  UserCategory category2 = new UserCategory("Category2");

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    testUser = new User();
    testUser.setName("Aditi");
    testUser.setEmail("test@columbia.edu");
    testUser.setAge(23);
    testUser.setCategories(Arrays.asList(category1, category2));

    when(jwtDecoder.decode(any())).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    when(jwt.getClaimAsString("profile")).thenReturn("USER");
    when(profileServiceSelector.selectService(any())).thenReturn(userDetailsService);
    when(userDetailsService.loadUserByUsername(any())).thenReturn(
        new UserDetails(testUser));
  }

  @Test
  void testGetAllUSer_Happy_Path() throws Exception {
    when(userService.getAllUsers()).thenReturn(
        Collections.singletonList(testUser));

    mockMvc.perform(get("/user/all")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].name").value("Aditi"))
        .andExpect(jsonPath("$[0].age").value("23"))
        .andExpect(jsonPath("$[0].categories").value(Arrays.asList(category1, category2)));
  }

  @Test
  void testGetAllOrganisations_Sad_Path() throws Exception {
    when(userService.getAllUsers()).thenReturn(List.of());

    mockMvc.perform(get("/user/all")).andExpect(status().isNoContent());
  }

  @Test
  void getOrganisationById_Happy_Path() throws Exception {
    when(userService.getUserById("1")).thenReturn(Optional.of(testUser));

    mockMvc.perform(get("/user/1")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name").value("Aditi"))
        .andExpect(jsonPath("$.age").value("23"))
        .andExpect(jsonPath("$.categories").value(Arrays.asList(category1, category2)));
  }

  @Test
  void getUserById_Sad_Path() throws Exception {
    when(userService.getUserById("999")).thenReturn(Optional.empty());

    mockMvc.perform(get("/user/999")).andExpect(status().isNotFound())
        .andExpect(content().string("User not found with id: 999"));
  }

  @Test
  void createUser() throws Exception {
    when(userService.createUser(any(User.class))).thenReturn(
        testUser);

    String requestBody = objectMapper.writeValueAsString(testUser);
    String requestBodyWithPassword = requestBody.substring(0,
        requestBody.length() - 1) + "," + "\"password\":\"Test Password\"}";

    mockMvc.perform(post("/user/create").contentType(MediaType.APPLICATION_JSON)
        .content(requestBodyWithPassword)).andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name").value("Aditi"));
  }

  @Test
  void updateUser_Happy_Path() throws Exception {
    testUser.setEmail("Updated Email");
    when(userService.updateUser(anyString(), any(User.class))).thenReturn(
        testUser);

    String requestBody = objectMapper.writeValueAsString(testUser);
    String requestBodyWithPassword = requestBody.substring(0,
        requestBody.length() - 1) + "," + "\"password\":\"Test Password\"}";

    mockMvc.perform(put("/user/update/testId").contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer testJWTToken").content(requestBodyWithPassword))
        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.email").value("Updated Email"));
  }

  @Test
  void updateUser_Sad_Path() throws Exception {
    testUser.setEmail("Updated Email");
    when(userService.updateUser(anyString(), any(User.class))).thenThrow(
        new ResourceNotFoundException("User not found with id: testId"));

    String requestBody = objectMapper.writeValueAsString(testUser);
    String requestBodyWithPassword = requestBody.substring(0,
        requestBody.length() - 1) + "," + "\"password\":\"Test Password\"}";

    mockMvc.perform(put("/user/update/testId").contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer testJWTToken").content(requestBodyWithPassword))
        .andExpect(status().isNotFound())
        .andExpect(content().string("User not found with id: testId"));
  }

  @Test
  void deleteUser_Sad_Path() throws Exception {
    when(userService.getUserById(anyString())).thenReturn(Optional.empty());

    mockMvc.perform(delete("/user/delete/testId").contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer testJWTToken")).andExpect(status().isNotFound())
        .andExpect(content().string("User not found with id: testId"));
  }

  @Test
  void deleteUser_Happy_Path() throws Exception {
    when(userService.getUserById(anyString())).thenReturn(
        Optional.of(testUser));

    mockMvc.perform(delete("/user/delete/testId").contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer testJWTToken")).andExpect(status().isNoContent())
        .andExpect(content().string("User deleted successfully"));
  }

}