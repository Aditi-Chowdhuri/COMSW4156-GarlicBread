package com.garlicbread.includify.controller.user;

import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.entity.user.UserCategory;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.model.user.UserRequest;
import com.garlicbread.includify.profile.user.UserDetails;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing user-related operations.
 */
@RestController
@RequestMapping("/registration")
public class UserController {

  private final UserService userService;
  private final UserCategoryService userCategoryService;

  public UserController(UserService userService, UserCategoryService userCategoryService) {
    this.userService = userService;
    this.userCategoryService = userCategoryService;
  }

  /**
   * Returns all user categories.
   *
   * @return a ResponseEntity containing the list of all user categories
   */
  @GetMapping("/category/all")
  @PermitAll
  public ResponseEntity<List<UserCategory>> getAllUserCategories() {
    List<UserCategory> userCategories = userCategoryService.getAll();
    if (userCategories.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(userCategories, HttpStatus.OK);  }

  /**
   * Creates a new user category.
   *
   * @param userCategory the user category to create
   * @return a ResponseEntity containing the created UserCategory
   */
  @PostMapping("/createCategory")
  @PermitAll
  public ResponseEntity<UserCategory> createUserCategory(
      @Valid @RequestBody UserCategory userCategory) {
    UserCategory createdUserCategory = userCategoryService.createCategory(userCategory);
    return new ResponseEntity<>(createdUserCategory, HttpStatus.CREATED);
  }

  /**
   * Deletes a user category by its ID.
   *
   * @param id the ID of the user category to delete
   * @return a ResponseEntity containing a success message or error
   */
  @DeleteMapping("/deleteCategory/{id}")
  @PermitAll
  public ResponseEntity<String> deleteUserCategory(
      @PathVariable String id) {
    try {
      if (Integer.parseInt(id) <= 6) {
        return new ResponseEntity<>("Cannot delete a default user category", HttpStatus.FORBIDDEN);
      }
    } catch (NumberFormatException ignored) {
      return new ResponseEntity<>("Invalid id passed", HttpStatus.BAD_REQUEST);
    }

    UserCategory userCategory = userCategoryService.getById(id);
    if (userCategory != null) {
      userCategoryService.deleteCategoryById(id);
      return new ResponseEntity<>("User category deleted successfully", HttpStatus.NO_CONTENT);
    } else {
      throw new ResourceNotFoundException("User category not found with " + "id: " + id);
    }
  }

  /**
   * Retrieves all users.
   *
   * @return a ResponseEntity containing a list of all users
   */
  @GetMapping("/all")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    if (users.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  /**
   * Retrieves details of the authenticated user.
   *
   * @return a ResponseEntity containing the authenticated User
   */
  @GetMapping()
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<User> getUser(Authentication authentication) {
    String authenticatedUserId = ((UserDetails) authentication.getPrincipal()).getId();
    Optional<User> user = userService.getUserById(authenticatedUserId);
    return user.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * Retrieves a user by their ID.
   *
   * @param id the ID of the user to retrieve
   * @return a ResponseEntity containing the found User
   * @throws ResourceNotFoundException if the user is not found
   */
  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<User> getUserById(@PathVariable String id) {
    Optional<User> user = userService.getUserById(id);
    return user.map(ResponseEntity::ok).orElseThrow(
        () -> new ResourceNotFoundException("User not" + " " + "found with id: " + id));
  }

  /**
   * Creates a new user based on the provided UserRequest.
   *
   * @param userRequest the request containing user information
   * @return a ResponseEntity containing the created User
   */
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

  /**
   * Updates an existing user.
   *
   * @param id          the ID of the user to update
   * @param userRequest the request containing updated user information
   * @return a ResponseEntity containing the updated User
   */
  @PutMapping("/update/{id}")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<User> updateUser(@PathVariable String id,
                                         @Valid @RequestBody UserRequest userRequest,
                                         Authentication authentication) {
    String authenticatedUserId = ((UserDetails) authentication.getPrincipal()).getId();
    if (!authenticatedUserId.equals(id)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    List<UserCategory> userCategories = new ArrayList<>();
    userRequest.getCategoryIds().forEach(categoryId -> {
      userCategories.add(userCategoryService.getById(categoryId));
    });

    User user = UserMapper.mapToUser(userRequest, userCategories);
    User updatedUser = userService.updateUser(id, user);
    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
  }

  /**
   * Deletes a user by their ID.
   *
   * @param id the ID of the user to delete
   * @return a ResponseEntity with a success message
   * @throws ResourceNotFoundException if the user is not found
   */
  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<String> deleteUser(@PathVariable String id, Authentication authentication) {
    String authenticatedUserId = ((UserDetails) authentication.getPrincipal()).getId();
    if (!authenticatedUserId.equals(id)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    Optional<User> user = userService.getUserById(id);
    if (user.isPresent()) {
      userService.deleteUser(id);
      return new ResponseEntity<>("User deleted successfully", HttpStatus.NO_CONTENT);
    } else {
      throw new ResourceNotFoundException("User not found with id: " + id);
    }
  }

}