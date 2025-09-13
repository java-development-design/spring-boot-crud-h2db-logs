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

        return repository.findAll().stream()
                .map(EmployeeMapper::toResponse)
                .toList();
    }

    public EmployeeResponse findByEmployeeId(Long id) {
        LOG.info("Employee id{} :", id);
        Employee  response = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
        LOG.debug("Fetched employee details: {}", response);
        return EmployeeMapper.toResponse(response);
    }

    public Employee saveEmployees(Employee employee) {
        repository.findByEmail(employee.getEmail()).ifPresent(e -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        });
        return repository.save(employee);
    }

    public EmployeeResponse updateEmployeeById(Long id, EmployeeRequest data) {
        Employee existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        existing.setName(data.getName());
        existing.setEmail(data.getEmail());
        existing.setRole(data.getRole());

        Employee updated = repository.save(existing);
        return EmployeeMapper.toResponse(updated);

    }

    public void deleteEmployeeById(Long id) {
        LOG.info("Delete employee id{} :", id);
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Employee not found"
                ));

        repository.delete(employee);
    }
}
