package com.example.employee.controller;

import com.example.employee.domain.Employee;
import com.example.employee.dto.DtoResponse;
import com.example.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) { this.employeeService = employeeService; }

    @PostMapping
    public ResponseEntity<DtoResponse> createEmployee(@RequestBody Employee employee) {
        Employee created = employeeService.createEmployee(employee);
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(created.getId())
                .message("Employee created successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @GetMapping
    public ResponseEntity<DtoResponse> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(employees)
                .message("Employees retrieved successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DtoResponse> getEmployeeById(@PathVariable String id) {
        Employee employee = employeeService.getEmployeeById(id);
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(employee)
                .message("Employee retrieved successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable String id, @RequestBody Employee employee) {
        Employee updated = employeeService.updateEmployee(id, employee);
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(updated)
                .message("Employee updated successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String id) {
        employeeService.deleteEmployee(id);
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(id)
                .message("Employee deleted successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

}