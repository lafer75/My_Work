package com.hillel.ua.graphql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDto {
    private String firstName;
    private String lastName;
    private String position;
    private int salary;
    private int age;
    private Integer departmentId;
    private Integer organizationId;
}