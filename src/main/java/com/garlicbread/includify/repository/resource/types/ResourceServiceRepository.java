package com.garlicbread.includify.repository.resource.types;

import com.garlicbread.includify.entity.resource.types.ResourceService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing ResourceContact entities.
 * This interface extends JpaRepository to provide CRUD operations for ResourceContact objects.
 */
@Repository
public interface ResourceServiceRepository extends JpaRepository<ResourceService, String> {

}
