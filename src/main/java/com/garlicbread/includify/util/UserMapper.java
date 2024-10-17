package com.garlicbread.includify.util;

import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.entity.user.UserCategory;
import com.garlicbread.includify.model.user.UserRequest;

import java.util.List;

public class UserMapper {

    public static User mapToUser(UserRequest userRequest, List<UserCategory> userCategories) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setAge(userRequest.getAge());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setCategories(userCategories);
        return user;
    }
}
