package com.garlicbread.includify.repository.user;

import com.garlicbread.includify.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // add required methods
}
