package com.garlicbread.includify.repository.volunteer;

import com.garlicbread.includify.entity.volunteer.Volunteer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, String> {

  Optional<Volunteer> findByEmail(String email);
}
