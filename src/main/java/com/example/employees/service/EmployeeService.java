package com.example.employees.service;

import com.example.employees.entity.Employee;
import com.example.employees.mapper.EmployeeMapper;
import com.example.employees.model.EmployeeRequest;
import com.example.employees.model.EmployeeResponse;
import com.example.employees.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<EmployeeResponse> findAllEmployees() {
        LOG.info("Fetching all employees from database");
        List<EmployeeResponse> employees = repository.findAll().stream()
                .map(EmployeeMapper::toResponse)
                .toList();
        LOG.debug("Total employees found: {}", employees.size());
        return employees;
    }

    public EmployeeResponse findByEmployeeId(Long id) {
        LOG.debug("Looking up employee in DB with id={}", id);
        Employee response = repository.findById(id)
                .orElseThrow(() -> {
                    LOG.warn("Employee not found with id={} - findByEmployeeId", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
                });
        LOG.info("Fetching employee by id={}", id);
        LOG.debug("Fetched employee details: {}", response);
        return EmployeeMapper.toResponse(response);
    }

    public Employee saveEmployees(Employee employee) {
        LOG.info("Attempting to save employee: name={}, email={}, role={}",
                employee.getName(), employee.getEmail(), employee.getRole());

        repository.findByEmail(employee.getEmail()).ifPresent(e -> {
            LOG.error("Email already exists: {}", employee.getEmail());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        });

        Employee saved = repository.save(employee);
        LOG.info("Employee saved successfully with id={}", saved.getId());
        LOG.debug("Saved employee full details: {}", saved);
        return saved;
    }

    public EmployeeResponse updateEmployeeById(Long id, EmployeeRequest request) {
        LOG.info("Updating employee with id={}", id);
        Employee existing = repository.findById(id)
                .orElseThrow(() -> {
                    LOG.warn("Employee not found with id={} - updateEmployeeById", id);
                    return new RuntimeException("Employee not found with id: " + id);
                });

        LOG.debug("Existing employee before update: {}", existing);

        existing.setName(request.getName());
        existing.setEmail(request.getEmail());
        existing.setRole(request.getRole());

        Employee updated = repository.save(existing);
        LOG.info("Employee updated successfully with id={}", updated.getId());
        LOG.debug("Updated employee details: {}", updated);

        return EmployeeMapper.toResponse(updated);
    }

    public void deleteEmployeeById(Long id) {
        LOG.info("Deleting employee with id={}", id);
        Employee employee = repository.findById(id)
                .orElseThrow(() -> {
                    LOG.warn("Attempted to delete non-existing employee with id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
                });

        repository.delete(employee);
        LOG.info("Employee deleted successfully with id={}", id);
    }
}
