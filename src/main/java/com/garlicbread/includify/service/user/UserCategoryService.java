package com.garlicbread.includify.service.user;

import com.garlicbread.includify.entity.user.UserCategory;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.repository.user.UserCategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class UserCategoryService {

  private final UserCategoryRepository userCategoryRepository;

  public UserCategoryService(UserCategoryRepository userCategoryRepository) {
    this.userCategoryRepository = userCategoryRepository;
  }

  public UserCategory createCategory(UserCategory userCategory) {
    return userCategoryRepository.save(userCategory);
  }

  public UserCategory getById(String id) {
    return userCategoryRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("User Category not found " + "with id " + id));
  }
}
