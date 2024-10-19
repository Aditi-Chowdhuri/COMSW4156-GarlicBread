package com.garlicbread.includify.repository.resource;

import com.garlicbread.includify.entity.resource.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Resource Types entities.
 * This interface extends JpaRepository to provide CRUD operations for Resource Types objects.
 */
@Repository
public interface ResourceTypeRepository extends JpaRepository<ResourceType, String> {

  // add required methods

}
