package com.garlicbread.includify.util;

import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.entity.user.UserCategory;
import com.garlicbread.includify.model.user.UserRequest;
import java.util.List;

/**
 * A utility class responsible for mapping data from a {@link UserRequest}
 * to a {@link User} entity.
 */
public class UserMapper {

  /**
   * Maps a {@link UserRequest} object to a {@link User} entity, setting
   * the user's name, age, email, password, and associated categories.
   *
   * @param userRequest    the request object containing details to create
   *                       or update a user
   * @param userCategories a list of {@link UserCategory} objects
   *                       associated with the user
   * @return a {@link User} entity populated with the provided data
   */
  public static User mapToUser(final UserRequest userRequest,
                               final List<UserCategory> userCategories) {
    User user = new User();
    user.setName(userRequest.getName());
    user.setAge(userRequest.getAge());
    user.setEmail(userRequest.getEmail());
    user.setPassword(userRequest.getPassword());
    user.setCategories(userCategories);
    return user;
  }
}
