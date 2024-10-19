package com.garlicbread.includify.repository.organisation;

import com.garlicbread.includify.entity.organisation.Organisation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Organisation entities.
 * This interface extends JpaRepository to provide CRUD operations for Organisation objects.
 */
@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, String> {

  Optional<Organisation> findByEmail(String email);
}
