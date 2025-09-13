package com.example.employees.mapper;

import com.example.employees.entity.Employee;
import com.example.employees.model.EmployeeResponse;

public class EmployeeMapper {

    public static EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getRole()
        );
    }
}
