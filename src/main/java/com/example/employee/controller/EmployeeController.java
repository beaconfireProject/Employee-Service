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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private MongoTemplate mongoTemplate;

    public EmployeeController(EmployeeService employeeService, MongoTemplate mongoTemplate) {
        this.employeeService = employeeService;
        this.mongoTemplate = mongoTemplate;
    }

    @PostMapping
    public ResponseEntity<DtoResponse> createEmployee(@RequestBody Employee employee) {
        System.out.println(employee);
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
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();
        for(String s: body.keySet()) {
            if(s.equals("contact") || s.equals("visaStatus") || s.equals("address") || s.equals("personalDocument")) {
                throw new RuntimeException("Cannot patch subdocuments, please remove and re-add");
            }
            update.set(s, body.get(s));
        }
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Employee.class);
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(updateResult)
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

    @GetMapping("userId/{userId}")
    public ResponseEntity<DtoResponse> getEmployeeByUserId(@PathVariable String userId) {
        Query query= new Query(Criteria.where("userId").is(userId));
        Employee employee = mongoTemplate.findOne(query, Employee.class);
        if(employee == null) {
            throw new RuntimeException("Employee not found");
        }
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
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().addToSet("contact", contact);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Employee.class);
        if (updateResult.getMatchedCount() == 0) {
            throw new RuntimeException("Contact not added");
        }
        Employee employee = employeeService.getEmployeeById(id);
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
        Query query = new Query(Criteria.where("id").is(id));
        Query query2 = new Query(Criteria.where("id").is(contactId));
        Update update = new Update().pull("contact", query2);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Employee.class);
        if (updateResult.getMatchedCount() == 0) {
            throw new RuntimeException("Contact not removed");
        }
        Employee employee = employeeService.getEmployeeById(id);
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
        Query query = new Query(Criteria.where("id").is(id));
        Query query2 = new Query(Criteria.where("id").is(visaId));
        Update update = new Update().pull("visaStatus", query2);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Employee.class);
        if (updateResult.getMatchedCount() == 0) {
            throw new RuntimeException("Visa Status not removed");
        }
        Employee employee = employeeService.getEmployeeById(id);
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(employee)
                .message("Employee visa removed successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

    @DeleteMapping("/{id}/address/{address}")
    public ResponseEntity<DtoResponse> removeAddress(@PathVariable String id, @PathVariable String address) {
        Query query = new Query(Criteria.where("id").is(id));
        Query query2 = new Query(Criteria.where("id").is(address));
        Update update = new Update().pull("address", query2);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Employee.class);
        if (updateResult.getMatchedCount() == 0) {
            throw new RuntimeException("Address not removed");
        }
        Employee employee = employeeService.getEmployeeById(id);
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
        Query query = new Query(Criteria.where("id").is(id));
        Query query2 = new Query(Criteria.where("id").is(documentId));
        Update update = new Update().pull("personalDocument", query2);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Employee.class);
        if (updateResult.getMatchedCount() == 0) {
            throw new RuntimeException("Document not removed");
        }
        Employee employee = employeeService.getEmployeeById(id);
        DtoResponse dtoResponse = DtoResponse.builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(employee)
                .message("Employee document removed successfully")
                .build();
        return ResponseEntity.ok().body(dtoResponse);
    }

}