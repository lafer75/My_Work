package com.hillel.ua.graphql.controller;

import com.hillel.ua.graphql.dto.DepartmentRequestDto;
import com.hillel.ua.graphql.entities.Department;
import com.hillel.ua.graphql.entities.Organization;
import com.hillel.ua.graphql.repository.DepartmentRepository;
import com.hillel.ua.graphql.repository.OrganizationRepository;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class DepartmentController {

    private final DepartmentRepository departmentRepository;
    private final OrganizationRepository organizationRepository;

    public DepartmentController(DepartmentRepository departmentRepository, OrganizationRepository organizationRepository) {
        this.departmentRepository = departmentRepository;
        this.organizationRepository = organizationRepository;
    }

    @MutationMapping
    public Department newDepartment(@Argument DepartmentRequestDto department) {
        Organization organization = organizationRepository.findById(department.getOrganizationId()).orElseThrow(() -> new RuntimeException("Organization not found"));
        return departmentRepository.save(new Department(null, department.getName(), null, organization));
    }

    @QueryMapping
    public Iterable<Department> departments(DataFetchingEnvironment environment) {
        DataFetchingFieldSelectionSet selectionSet = environment.getSelectionSet();
        List<Specification<Department>> specifications = buildSpecifications(selectionSet);
        return departmentRepository.findAll(Specification.where(specifications.stream().reduce(Specification::and).orElse(null)));
    }

    @QueryMapping
    public Department department(@Argument Integer id, DataFetchingEnvironment environment) {
        Specification<Department> spec = byId(id);
        DataFetchingFieldSelectionSet selectionSet = environment.getSelectionSet();
        if (selectionSet.contains("employees")) spec = spec.and(fetchEmployees());
        if (selectionSet.contains("organization")) spec = spec.and(fetchOrganization());
        return departmentRepository.findOne(spec).orElseThrow(() -> new RuntimeException("Department not found"));
    }

    private List<Specification<Department>> buildSpecifications(DataFetchingFieldSelectionSet selectionSet) {
        return List.of(selectionSet.contains("employees") ? fetchEmployees() : null, selectionSet.contains("organization") ? fetchOrganization() : null);
    }

    private Specification<Department> fetchEmployees() {
        return (root, query, builder) -> {
            root.fetch("employees", JoinType.LEFT);
            return builder.isNotEmpty(root.get("employees"));
        };
    }

    private Specification<Department> fetchOrganization() {
        return (root, query, builder) -> {
            root.fetch("organization", JoinType.LEFT);
            return builder.isNotNull(root.get("organization"));
        };
    }

    private Specification<Department> byId(Integer id) {
        return (root, query, builder) -> builder.equal(root.get("id"), id);
    }

}
