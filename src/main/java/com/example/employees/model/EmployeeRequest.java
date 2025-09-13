package com.example.employees.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequest{
    String name;
    String email;
    String role;
}
