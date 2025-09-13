package com.example.employees.controller;

import com.example.employees.entity.Employee;
import com.example.employees.mapper.EmployeeMapper;
import com.example.employees.model.EmployeeResponse;
import com.example.employees.service.EmployeeService;
import com.example.employees.model.EmployeeRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService service;
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping
    public List<EmployeeResponse> getEmployeeList() {
        LOG.info("Fetching all employees");
        List<EmployeeResponse> employees = service.findAllEmployees();
        LOG.debug("Total employees found: {}", employees.size());
        return employees;
    }

    @GetMapping("/{id}")
    public EmployeeResponse getEmployeeById(@PathVariable Long id) {
        LOG.info("Fetching employee by id={}", id);
        EmployeeResponse response = service.findByEmployeeId(id);
        LOG.debug("Employee details fetched: {}", response);
        return response;
    }

    @PostMapping("/save")
    public ResponseEntity<EmployeeResponse> saveEmployeeData(@Valid @RequestBody EmployeeRequest req) {
        LOG.info("Saving new employee: name={}, email={}, role={}", req.getName(), req.getEmail(), req.getRole());
        Employee saved = service.saveEmployees(Employee.builder()
                .name(req.getName())
                .email(req.getEmail())
                .role(req.getRole())
                .build());
        LOG.info("Employee saved successfully with id={}", saved.getId());
        LOG.debug("Saved employee full details: {}", saved);
        return ResponseEntity.created(URI.create("/api/employees/" + saved.getId()))
                .body(EmployeeMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    public EmployeeResponse updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequest req) {
        LOG.info("Updating employee with id={} | Request: name={}, email={}, role={}",
                id, req.getName(), req.getEmail(), req.getRole());
        EmployeeResponse updated = service.updateEmployeeById(id, req);
        LOG.info("Employee updated successfully with id={}", id);
        LOG.debug("Updated employee details: {}", updated);
        return updated;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployeeById(@PathVariable Long id) {
        LOG.info("Deleting employee with id={}", id);
        service.deleteEmployeeById(id);
        LOG.info("Employee deleted successfully with id={}", id);
    }
}
