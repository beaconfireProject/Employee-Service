package com.example.employee.controller;

import com.example.employee.domain.*;
import com.example.employee.dto.DtoResponse;
import com.example.employee.exception.ForbiddenException;
import com.example.employee.service.EmployeeService;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

    private boolean checkUserID(String employeeUserId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            for(GrantedAuthority role : auth.getAuthorities())
            {
                if(role.getAuthority().equals("HR")) {
                    return true;
                }
            }
            Object userIdObj = auth.getDetails();
            if (userIdObj instanceof Long) {
                Long userId = (Long) userIdObj;
                return userId.toString().equals(employeeUserId);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('HR', 'employee')")
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
    @PreAuthorize("hasAuthority('HR')")
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
    @PreAuthorize("hasAnyAuthority('HR', 'employee')")
    public ResponseEntity<DtoResponse> getEmployeeById(@PathVariable String id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (!checkUserID(employee.getUserId())) throw new ForbiddenException();
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(employee)
                .message("Employee retrieved successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('HR', 'employee')")
    public ResponseEntity<DtoResponse> updateEmployee(@PathVariable String id, @RequestBody Map<String, Object> body) {
        Employee employee = employeeService.patchEmployee(id, body);
        if (!checkUserID(employee.getUserId())) throw new ForbiddenException();
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(employee)
                .message("Employee updated successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('HR')")
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
    @PreAuthorize("hasAnyAuthority('HR', 'employee')")
    public ResponseEntity<DtoResponse> getEmployeeByUserId(@PathVariable String userId) {
        if (!checkUserID(userId)) throw new ForbiddenException();
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
    @PreAuthorize("hasAnyAuthority('HR', 'employee')")
    public ResponseEntity<DtoResponse> addDocument(@PathVariable String id, @RequestBody PersonalDocument document) {
        document.setId("" + (int)(Math.random() * (Integer.MAX_VALUE - 1)));
        Employee employee = employeeService.getEmployeeById(id);
        if (!checkUserID(employee.getUserId())) throw new ForbiddenException();
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
    @PreAuthorize("hasAnyAuthority('HR', 'employee')")
    public ResponseEntity<DtoResponse> addVisa(@PathVariable String id, @RequestBody VisaStatus visa) {
        visa.setId("" + (int)(Math.random() * (Integer.MAX_VALUE - 1)));
        Employee employee = employeeService.getEmployeeById(id);
        if (!checkUserID(employee.getUserId())) throw new ForbiddenException();
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
    @PreAuthorize("hasAnyAuthority('HR', 'employee')")
    public ResponseEntity<DtoResponse> addAddress(@PathVariable String id, @RequestBody Address address) {
        address.setId("" + (int)(Math.random() * (Integer.MAX_VALUE - 1)));
        Employee employee = employeeService.getEmployeeById(id);
        if (!checkUserID(employee.getUserId())) throw new ForbiddenException();
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
        if (!checkUserID(employee.getUserId())) throw new ForbiddenException();
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
        if (!checkUserID(employee.getUserId())) throw new ForbiddenException();
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
    @PreAuthorize("hasAnyAuthority('HR', 'employee')")
    public ResponseEntity<DtoResponse> removeVisa(@PathVariable String id, @PathVariable String visaId) {
        Employee employee = employeeService.getEmployeeById(id);
        if (!checkUserID(employee.getUserId())) throw new ForbiddenException();
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
    @PreAuthorize("hasAnyAuthority('HR', 'employee')")
    public ResponseEntity<DtoResponse> removeAddress(@PathVariable String id, @PathVariable String addressId) {
        Employee employee = employeeService.getEmployeeById(id);
        if (!checkUserID(employee.getUserId())) throw new ForbiddenException();
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
    @PreAuthorize("hasAnyAuthority('HR', 'employee')")
    public ResponseEntity<DtoResponse> removeDocument(@PathVariable String id, @PathVariable String documentId) {
        Employee employee = employeeService.getEmployeeById(id);
        if (!checkUserID(employee.getUserId())) throw new ForbiddenException();
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
    @PreAuthorize("hasAnyAuthority('HR', 'employee')")
    public ResponseEntity<DtoResponse> getVisas(@PathVariable String id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (!checkUserID(employee.getUserId())) throw new ForbiddenException();
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
    @PreAuthorize("hasAuthority('HR')")
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