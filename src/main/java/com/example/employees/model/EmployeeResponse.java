package com.example.employees.model;

public record EmployeeResponse(
        Long id,
        String name,
        String email,
        String role

) {
}
