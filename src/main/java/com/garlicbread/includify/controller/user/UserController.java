package com.garlicbread.includify.controller.user;

import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.entity.user.UserCategory;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.model.user.UserRequest;
import com.garlicbread.includify.service.user.UserCategoryService;
import com.garlicbread.includify.service.user.UserService;
import com.garlicbread.includify.util.UserMapper;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;
  private final UserCategoryService userCategoryService;

  public UserController(UserService userService, UserCategoryService userCategoryService) {
    this.userService = userService;
    this.userCategoryService = userCategoryService;
  }

  @PostMapping("/createCategory")
  @PermitAll
  public ResponseEntity<UserCategory> createUserCategory(@Valid @RequestBody UserCategory userCategory) {
    UserCategory createdUserCategory = userCategoryService.createCategory(userCategory);
    return new ResponseEntity<>(createdUserCategory, HttpStatus.CREATED);
  }

  @GetMapping("/all")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    if (users.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<User> getUserById(@PathVariable String id) {
    Optional<User> user = userService.getUserById(id);
    return user
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
  }

  @PostMapping("/create")
  @PermitAll
  public ResponseEntity<User> createUser(@Valid @RequestBody UserRequest userRequest) {
    List<UserCategory> userCategories = new ArrayList<>();
    userRequest.getCategoryIds().forEach(categoryId -> {
      userCategories.add(userCategoryService.getById(categoryId));
    });

    User user = UserMapper.mapToUser(userRequest, userCategories);

    User createdUser = userService.createUser(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @PutMapping("/update/{id}")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<User> updateUser(@PathVariable String id, @Valid @RequestBody UserRequest userRequest) {
    List<UserCategory> userCategories = new ArrayList<>();
    userRequest.getCategoryIds().forEach(categoryId -> {
      userCategories.add(userCategoryService.getById(categoryId));
    });

    User user = UserMapper.mapToUser(userRequest, userCategories);
    User updatedUser = userService.updateUser(id, user);
    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<String> deleteUser(@PathVariable String id) {
    Optional<User> user = userService.getUserById(id);
    if (user.isPresent()) {
      userService.deleteUser(id);
      return new ResponseEntity<>("User deleted successfully", HttpStatus.NO_CONTENT);
    } else {
      throw new ResourceNotFoundException("User not found with id: " + id);
    }
  }

}