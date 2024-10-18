package com.garlicbread.includify.controller.user;

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
import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.entity.user.UserCategory;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.model.user.UserRequest;
import com.garlicbread.includify.service.user.UserCategoryService;
import com.garlicbread.includify.service.user.UserService;
import com.garlicbread.includify.util.UserMapper;
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

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserCategoryService userCategoryService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Mock
    private Jwt jwt;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private UserCategory testCategory;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("ima014566@gmail.com");
        testUser.setName("Ibrahim Mo");

        testCategory = new UserCategory();
        testCategory.setName("senior_citizen");

        when(jwtDecoder.decode(any())).thenReturn(jwt);
        when(jwt.getClaimAsString("sub")).thenReturn("test_user");
    }

    @Test
    void testGetAllUsers_Happy_Path() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(testUser));

        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email").value("ima014566@gmail.com"))
                .andExpect(jsonPath("$[0].name").value("Ibrahim Mo"));
    }

    @Test
    void testGetAllUsers_Sad_Path() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/user/all"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUserById_Happy_Path() throws Exception {
        when(userService.getUserById("1")).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("ima014566@gmail.com"))
                .andExpect(jsonPath("$.name").value("Ibrahim Mo"));
    }

    @Test
    void getUserById_Sad_Path() throws Exception {
        when(userService.getUserById("22")).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/22"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found with id: 22"));
    }

    @Test
    void createUser_Happy_Path() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(testUser);
        when(userCategoryService.getById(anyString())).thenReturn(testCategory);

        UserRequest userRequest = new UserRequest();
        userRequest.setName("Ibrahim Mo");
        userRequest.setEmail("ima014566@gmail.com");
        userRequest.setCategoryIds(List.of("1"));

        String requestBody = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(post("/user/create").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Ibrahim Mo"));
    }

    @Test
    void updateUser_Happy_Path() throws Exception {
        when(userService.updateUser(anyString(), any(User.class))).thenReturn(testUser);
        when(userCategoryService.getById(anyString())).thenReturn(testCategory);

        UserRequest userRequest = new UserRequest();
        userRequest.setName("Ibrahim Mo");
        userRequest.setEmail("ima014566@gmail.com");
        userRequest.setCategoryIds(List.of("1"));

        String requestBody = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(put("/user/update/1").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Ibrahim Mo"));
    }

    @Test
    void updateUser_Sad_Path() throws Exception {
        when(userService.updateUser(anyString(), any(User.class)))
                .thenThrow(new ResourceNotFoundException("User not found with id: 1"));

        UserRequest userRequest = new UserRequest();
        userRequest.setName("Ibrahim Mo");
        userRequest.setEmail("ima014566@gmail.com");
        userRequest.setCategoryIds(List.of("1"));

        String requestBody = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(put("/user/update/1").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found with id: 1"));
    }

    @Test
    void deleteUser_Happy_Path() throws Exception {
        when(userService.getUserById(anyString())).thenReturn(Optional.of(testUser));

        mockMvc.perform(delete("/user/delete/1").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer testJWTToken"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("User deleted successfully"));
    }

    @Test
    void deleteUser_Sad_Path() throws Exception {
        when(userService.getUserById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/user/delete/1").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer testJWTToken"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found with id: 1"));
    }
}
