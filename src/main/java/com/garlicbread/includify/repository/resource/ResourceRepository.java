package com.garlicbread.includify.repository.resource;

import com.garlicbread.includify.entity.resource.Resource;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, String> {

  List<Resource> findByOrganisationId(String organisationId);

}
