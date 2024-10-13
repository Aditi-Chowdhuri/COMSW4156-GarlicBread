package com.garlicbread.includify.service.user;

import com.garlicbread.includify.repository.user.UserCategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class UserCategoryService {

    private final UserCategoryRepository userCategoryRepository;

    public UserCategoryService(UserCategoryRepository userCategoryRepository) {
        this.userCategoryRepository = userCategoryRepository;
    }

    // add the required methods

}
