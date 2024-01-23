package com.hillel.ua.graphql.repository;

import com.hillel.ua.graphql.entities.Organization;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface OrganizationRepository extends
        CrudRepository<Organization, Integer>, JpaSpecificationExecutor<Organization> {
}

