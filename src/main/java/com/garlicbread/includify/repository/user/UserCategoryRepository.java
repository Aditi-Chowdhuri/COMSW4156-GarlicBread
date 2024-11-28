package com.garlicbread.includify.repository.user;

import com.garlicbread.includify.entity.user.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing User Category entities.
 * This interface extends JpaRepository to provide CRUD operations for User Category .
 */
@Repository
public interface UserCategoryRepository extends JpaRepository<UserCategory, String> {

  // add required methods
}
