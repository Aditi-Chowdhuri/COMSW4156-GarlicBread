package com.garlicbread.includify.repository.resource.types;

import com.garlicbread.includify.entity.resource.types.ResourceInfra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing ResourceContact entities.
 * This interface extends JpaRepository to provide CRUD operations for ResourceContact objects.
 */
@Repository
public interface ResourceInfraRepository extends JpaRepository<ResourceInfra, String> {

}
