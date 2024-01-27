package com.hillel.ua.graphql.controller;

import com.github.javafaker.Faker;
import com.hillel.ua.graphql.dto.EmployeeRequestDto;
import com.hillel.ua.graphql.entities.Department;
import com.hillel.ua.graphql.entities.Employee;
import com.hillel.ua.graphql.entities.Organization;
import com.hillel.ua.graphql.filter.EmployeeFilter;
import com.hillel.ua.graphql.filter.FilterField;
import com.hillel.ua.graphql.repository.DepartmentRepository;
import com.hillel.ua.graphql.repository.EmployeeRepository;
import com.hillel.ua.graphql.repository.OrganizationRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Controller
public class EmployeeController {

    DepartmentRepository departmentRepository;
    EmployeeRepository employeeRepository;
    OrganizationRepository organizationRepository;

    EmployeeController(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository, OrganizationRepository organizationRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.organizationRepository = organizationRepository;
    }

    @QueryMapping
    public Iterable<Employee> employees() {
        return employeeRepository.findAll();
    }

    @QueryMapping
    public Employee employee(@Argument Integer id) {
        return employeeRepository.findById(id).orElseThrow();
    }

    @MutationMapping
    public Employee newEmployee(@Argument EmployeeRequestDto employee) {
        Optional<Department> departmentOptional = departmentRepository.findById(employee.getDepartmentId());
        Optional<Organization> organizationOptional = organizationRepository.findById(employee.getOrganizationId());

        if (departmentOptional.isPresent() && organizationOptional.isPresent()) {
            Department department = departmentOptional.get();
            Organization organization = organizationOptional.get();
            return employeeRepository.save(new Employee(null, employee.getFirstName(), employee.getLastName(), employee.getPosition(), employee.getAge(), employee.getSalary(), department, organization));
        }else {
            throw new RuntimeException("Department or Organization not found for given IDs");
        }
    }

    @MutationMapping
    public List<Employee> generateEmployees(@Argument(name = "count") int count) {
        List<Employee> employees = new ArrayList<>();
        Faker faker = new Faker();

        int batchSize = 10000;

        List<Department> departments = (List<Department>) departmentRepository.findAll();
        List<Organization> organizations = (List<Organization>) organizationRepository.findAll();

        if (departments.isEmpty() || organizations.isEmpty()) {
            throw new RuntimeException("No departments or organizations found in the database");
        }

        ExecutorService executorService = Executors.newFixedThreadPool(20);

        for (int i = 0; i < count; i += batchSize) {
            int finalI = i;
            executorService.submit(() -> {
                List<Employee> batch = new ArrayList<>();
                for (int j = finalI; j < finalI + batchSize && j < count; j++) {
                    EmployeeRequestDto employeeDto = new EmployeeRequestDto();
                    employeeDto.setFirstName(faker.name().firstName());
                    employeeDto.setLastName(faker.name().lastName());
                    employeeDto.setPosition(faker.job().position());
                    employeeDto.setAge(faker.number().numberBetween(20, 60));
                    employeeDto.setSalary(faker.number().numberBetween(50000, 150000));

                    Department department = departments.get(faker.random().nextInt(departments.size()));
                    Organization organization = organizations.get(faker.random().nextInt(organizations.size()));

                    long departmentId = faker.random().nextInt(department.getId());
                    long organizationId = faker.random().nextInt(organization.getId());

                    employeeDto.setDepartmentId((int) departmentId);
                    employeeDto.setOrganizationId((int) organizationId);

                    Employee employee = employeeRepository.save(new Employee(null, employeeDto.getFirstName(), employeeDto.getLastName(), employeeDto.getPosition(), employeeDto.getAge(), employeeDto.getSalary(), department, organization));
                    batch.add(employee);
                }
                List<Employee> savedBatch = (List<Employee>) employeeRepository.saveAll(batch);
                synchronized (employees) {
                    employees.addAll(savedBatch);
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return employees;
    }


    @QueryMapping
    public Iterable<Employee> employeesWithFilter(@Argument EmployeeFilter filter) {
        Specification<Employee> spec = null;
        if (filter.getSalary() != null) spec = bySalary(filter.getSalary());
        if (filter.getAge() != null) spec = (spec == null ? byAge(filter.getAge()) : spec.and(byAge(filter.getAge())));
        if (filter.getPosition() != null)
            spec = (spec == null ? byPosition(filter.getPosition()) : spec.and(byPosition(filter.getPosition())));
        if (spec != null) return employeeRepository.findAll(spec);
        else return employeeRepository.findAll();
    }

    private Specification<Employee> bySalary(FilterField filterField) {
        return (root, query, builder) -> filterField.generateCriteria(builder, root.get("salary"));
    }

    private Specification<Employee> byAge(FilterField filterField) {
        return (root, query, builder) -> filterField.generateCriteria(builder, root.get("age"));
    }

    private Specification<Employee> byPosition(FilterField filterField) {
        return (root, query, builder) -> filterField.generateCriteria(builder, root.get("position"));
    }
}
