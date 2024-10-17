package com.garlicbread.includify.controller.user;

import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.service.user.UserCategoryService;
import com.garlicbread.includify.service.user.UserService;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserCategoryService userCategoryService;

    public UserController(UserService userService, UserCategoryService userCategoryService) {
        this.userService = userService;
        this.userCategoryService = userCategoryService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('USER')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public Optional<User> getUSerById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PostMapping("/create")
    @PermitAll
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public User updateUser(@PathVariable String id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

}