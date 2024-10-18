package com.garlicbread.includify.repository.user;

import com.garlicbread.includify.entity.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing User entities.
 * This interface extends JpaRepository to provide CRUD operations for User objects.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByEmail(String email);
}
