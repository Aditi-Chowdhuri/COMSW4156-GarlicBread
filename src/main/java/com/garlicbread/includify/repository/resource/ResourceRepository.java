package com.garlicbread.includify.repository.resource;

import com.garlicbread.includify.entity.resource.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, String> {

    List<Resource> findByOrganisationId(String organisationId);

}
