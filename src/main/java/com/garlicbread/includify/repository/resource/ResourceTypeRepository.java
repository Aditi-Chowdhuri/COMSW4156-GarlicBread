package com.garlicbread.includify.repository.resource;

import com.garlicbread.includify.entity.resource.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceTypeRepository extends JpaRepository<ResourceType, String> {

  // add required methods

}
