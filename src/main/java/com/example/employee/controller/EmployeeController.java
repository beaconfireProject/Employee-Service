package com.example.employee.controller;

import com.example.employee.domain.*;
import com.example.employee.dto.DtoResponse;
import com.example.employee.service.EmployeeService;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService, MongoTemplate mongoTemplate) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<DtoResponse> createEmployee(@RequestBody Employee employee) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Object userIdObj = auth.getDetails();
            if (userIdObj instanceof Long) {
                Long userId = (Long) userIdObj;
                System.out.println("User ID from JWT token: " + userId);
            } else {
                System.out.println("User ID not found in authentication details");
            }
        } else {
            System.out.println("No authenticated user found");
        }
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

    @PatchMapping("/{id}")
    public ResponseEntity<DtoResponse> updateEmployee(@PathVariable String id, @RequestBody Map<String, Object> body) {
        Employee employee = employeeService.patchEmployee(id, body);
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(employee)
                .message("Employee updated successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DtoResponse> deleteEmployee(@PathVariable String id) {
        employeeService.deleteEmployee(id);
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(id)
                .message("Employee deleted successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<DtoResponse> getEmployeeByUserId(@PathVariable String userId) {
        Employee employee = employeeService.getEmployeeByUserId(userId);
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(employee)
                .message("Employee retrieved by UserId successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @PostMapping("/{id}/document")
    public ResponseEntity<DtoResponse> addDocument(@PathVariable String id, @RequestBody PersonalDocument document) {
        document.setId("" + (int)(Math.random() * (Integer.MAX_VALUE - 1)));
        Employee employee = employeeService.getEmployeeById(id);
        List<PersonalDocument> pds = employee.getPersonalDocument();
        pds.add(document);
        employee.setPersonalDocument(pds);
        employee = employeeService.updateEmployee(id, employee);
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(employee)
                .message("Employee document added successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @PostMapping("/{id}/visa")
    public ResponseEntity<DtoResponse> addVisa(@PathVariable String id, @RequestBody VisaStatus visa) {
        visa.setId("" + (int)(Math.random() * (Integer.MAX_VALUE - 1)));
        Employee employee = employeeService.getEmployeeById(id);
        List<VisaStatus> vs = employee.getVisaStatus();
        vs.add(visa);
        employee.setVisaStatus(vs);
        employee = employeeService.updateEmployee(id, employee);
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(employee)
                .message("Employee visa added successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @PostMapping("/{id}/address")
    public ResponseEntity<DtoResponse> addAddress(@PathVariable String id, @RequestBody Address address) {
        address.setId("" + (int)(Math.random() * (Integer.MAX_VALUE - 1)));
        Employee employee = employeeService.getEmployeeById(id);
        List<Address> addresses = employee.getAddress();
        addresses.add(address);
        employee.setAddress(addresses);
        employee = employeeService.updateEmployee(id, employee);
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(employee)
                .message("Employee address added successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @PostMapping("/{id}/contact")
    public ResponseEntity<DtoResponse> addContact(@PathVariable String id, @RequestBody Contact contact) {
        contact.setId("" + (int)(Math.random() * (Integer.MAX_VALUE - 1)));
        Employee employee = employeeService.getEmployeeById(id);
        List<Contact> contacts = employee.getContact();
        contacts.add(contact);
        employee.setContact(contacts);
        employee = employeeService.updateEmployee(id, employee);
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(employee)
                .message("Employee contact added successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @DeleteMapping("/{id}/contact/{contactId}")
    public ResponseEntity<DtoResponse> removeContact(@PathVariable String id, @PathVariable String contactId) {
        Employee employee = employeeService.getEmployeeById(id);
        employee.setContact(employee.getContact()
                .stream()
                .filter(c -> c.getId().equals(contactId))
                .collect(Collectors.toList()));
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(employee)
                .message("Employee contact removed successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @DeleteMapping("/{id}/visa/{visaId}")
    public ResponseEntity<DtoResponse> removeVisa(@PathVariable String id, @PathVariable String visaId) {
        Employee employee = employeeService.getEmployeeById(id);
        employee.setVisaStatus(employee.getVisaStatus()
                .stream()
                .filter(c -> c.getId().equals(visaId))
                .collect(Collectors.toList()));
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(employee)
                .message("Employee visa removed successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @DeleteMapping("/{id}/address/{addressId}")
    public ResponseEntity<DtoResponse> removeAddress(@PathVariable String id, @PathVariable String addressId) {
        Employee employee = employeeService.getEmployeeById(id);
        employee.setAddress(employee.getAddress()
                .stream()
                .filter(c -> c.getId().equals(addressId))
                .collect(Collectors.toList()));
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(employee)
                .message("Employee address removed successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @DeleteMapping("/{id}/document/{documentId}")
    public ResponseEntity<DtoResponse> removeDocument(@PathVariable String id, @PathVariable String documentId) {
        Employee employee = employeeService.getEmployeeById(id);
        employee.setPersonalDocument(employee.getPersonalDocument()
                .stream()
                .filter(c -> c.getId().equals(documentId))
                .collect(Collectors.toList()));
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(employee)
                .message("Employee document removed successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @GetMapping("/{id}/visa")
    public ResponseEntity<DtoResponse> getVisas(@PathVariable String id) {
        Employee employee = employeeService.getEmployeeById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("fullName", employee.getFirstName() + " " + employee.getLastName());
        map.put("visaStatus", employee.getVisaStatus());
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(map)
                .message("Employee full name and visas retrieved successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @GetMapping("/houseId/{houseId}")
    public ResponseEntity<DtoResponse> getEmployeesByHouseId(@PathVariable String houseId) {
        List<Employee> employees = employeeService.getEmployeesByHouseId(houseId);
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(employees)
                .message("Employee retrieved by HouseId successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }
}