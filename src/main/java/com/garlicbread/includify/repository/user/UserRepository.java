package com.garlicbread.includify.repository.user;

import com.garlicbread.includify.entity.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing User entities.
 * This interface provides methods to perform CRUD operations on User entities,
 * extending the JpaRepository for convenient access to data persistence methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByEmail(String email);
}
