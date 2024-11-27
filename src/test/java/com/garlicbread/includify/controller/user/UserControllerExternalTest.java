package com.garlicbread.includify.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garlicbread.includify.config.SecurityConfig;
import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.entity.user.UserCategory;
import com.garlicbread.includify.model.user.UserRequest;
import com.garlicbread.includify.profile.user.UserDetails;
import com.garlicbread.includify.service.auth.ProfileServiceSelector;
import com.garlicbread.includify.service.auth.UserDetailsService;
import com.garlicbread.includify.service.user.UserCategoryService;
import com.garlicbread.includify.service.user.UserService;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@Transactional
public class UserControllerExternalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserCategoryService userCategoryService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private ProfileServiceSelector profileServiceSelector;

    @MockBean
    private UserDetailsService userDetailsService;

    @Mock
    private Jwt jwt;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private UserCategory testCategory;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setId("1");
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");

        testCategory = new UserCategory();
        testCategory.setTitle("Test Category");

        when(jwtDecoder.decode(any())).thenReturn(jwt);
        when(jwt.getClaimAsString("sub")).thenReturn("test_user");
        when(jwt.getClaimAsString("profile")).thenReturn("USER");
        when(profileServiceSelector.selectService(any())).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(any())).thenReturn(new UserDetails(testUser));
    }

    @Test
    void testGetAllUsers_Happy_Path() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(testUser));

        mockMvc.perform(get("/registration/all")
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].email").value("test@example.com"))
            .andExpect(jsonPath("$[0].name").value("Test User"));
    }

    @Test
    void testGetAllUsers_Sad_Path() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/registration/all")
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isNoContent());
    }

    @Test
    void getUserById_Happy_Path() throws Exception {
        when(userService.getUserById("1")).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/registration/1")
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void getUserById_Sad_Path() throws Exception {
        when(userService.getUserById("22")).thenReturn(Optional.empty());

        mockMvc.perform(get("/registration/22")
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("User not found with id: 22"));
    }

    @Test
    void createUser_Happy_Path() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(testUser);
        when(userCategoryService.getById(anyString())).thenReturn(testCategory);

        UserRequest userRequest = new UserRequest();
        userRequest.setName("Test User");
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password123");
        userRequest.setCategoryIds(List.of("1"));
        userRequest.setAge(25);

        String requestBody = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(post("/registration/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void deleteUser_Happy_Path() throws Exception {
        when(userService.getUserById(anyString())).thenReturn(Optional.of(testUser));

        mockMvc.perform(delete("/registration/delete/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isNoContent())
            .andExpect(content().string("User deleted successfully"));
    }

    @Test
    void deleteUser_Sad_Path() throws Exception {
        when(userService.getUserById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/registration/delete/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer Test-JWT-Token"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("User not found with id: 1"));
    }
}