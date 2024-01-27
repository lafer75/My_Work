package com.hillel.ua.graphql.controller;

import com.github.javafaker.Faker;
import com.hillel.ua.graphql.dto.OrganizationRequestDto;
import com.hillel.ua.graphql.entities.Department;
import com.hillel.ua.graphql.entities.Organization;
import com.hillel.ua.graphql.entities.Employee;
import com.hillel.ua.graphql.repository.OrganizationRepository;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Controller
public class OrganizationController {

    OrganizationRepository repository;

    OrganizationController(OrganizationRepository repository) {
        this.repository = repository;
    }

    @MutationMapping
    public Organization newOrganization(@Argument OrganizationRequestDto organization) {
        return repository.save(new Organization(null, organization.getName(), null, null));
    }
    @MutationMapping
    public List<Organization> generateOrganizations(@Argument(name = "count") int count) {
        List<Organization> organizations = new ArrayList<>();
        Faker faker = new Faker();

        int batchSize = 20;

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < count; i += batchSize) {
            int finalI = i;
            executorService.submit(() -> {
                List<Organization> batch = new ArrayList<>();
                for (int j = finalI; j < finalI + batchSize && j < count; j++) {
                    OrganizationRequestDto organizationDto = new OrganizationRequestDto();
                    organizationDto.setName(faker.company().name());

                    Organization organization = new Organization(null, organizationDto.getName(), null, null);
                    batch.add(organization);
                }
                List<Organization> savedBatch = (List<Organization>) repository.saveAll(batch);
                synchronized (organizations) {
                    organizations.addAll(savedBatch);
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return organizations;
    }

    @QueryMapping
    public Iterable<Organization> organizations() {
        return repository.findAll();
    }

    @QueryMapping
    public Organization organization(@Argument Integer id, DataFetchingEnvironment environment) {
        Specification<Organization> spec = byId(id);
        DataFetchingFieldSelectionSet selectionSet = environment
                .getSelectionSet();
        if (selectionSet.contains("employees"))
            spec = spec.and(fetchEmployees());
        if (selectionSet.contains("departments"))
            spec = spec.and(fetchDepartments());
        return repository.findOne(spec).orElseThrow();
    }

    private Specification<Organization> fetchDepartments() {
        return (root, query, builder) -> {
            Fetch<Organization, Department> f = root
                    .fetch("departments", JoinType.LEFT);
            Join<Organization, Department> join = (Join<Organization, Department>) f;
            return join.getOn();
        };
    }

    private Specification<Organization> fetchEmployees() {
        return (root, query, builder) -> {
            Fetch<Organization, Employee> f = root
                    .fetch("employees", JoinType.LEFT);
            Join<Organization, Employee> join = (Join<Organization, Employee>) f;
            return join.getOn();
        };
    }

    private Specification<Organization> byId(Integer id) {
        return (root, query, builder) -> builder.equal(root.get("id"), id);
    }
}
