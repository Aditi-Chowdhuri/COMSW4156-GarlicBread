package com.garlicbread.includify.controller.user;

import com.garlicbread.includify.service.user.UserCategoryService;
import com.garlicbread.includify.service.user.UserService;
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

    // add the required methods
}
