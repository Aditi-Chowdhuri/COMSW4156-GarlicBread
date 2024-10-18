package com.garlicbread.includify.controller.user;

import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.service.user.UserCategoryService;
import com.garlicbread.includify.service.user.UserService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

  public UserController(UserService userService, 
      UserCategoryService userCategoryService) {
    this.userService = userService;
    this.userCategoryService = userCategoryService;
  }

  @GetMapping("/all")
  public List<User> getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping("/{id}")
  public Optional<User> getUSerById(@PathVariable String id) {
    return userService.getUserById(id);
  }

  @PostMapping("/create")
  public User createUser(@RequestBody User user) {
    return userService.createUser(user);
  }

  @PutMapping("/update/{id}")
  public User updateUser(@PathVariable String id, 
      @RequestBody User userDetails) {
    return userService.updateUser(id, userDetails);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteUser(@PathVariable String id) {
    userService.deleteUser(id);
  }
}