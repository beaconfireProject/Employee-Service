package com.example.employee.service;

import com.example.employee.domain.Employee;
import com.example.employee.repository.EmployeeRepository;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private MongoTemplate mongoTemplate;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, MongoTemplate mongoTemplate) {
        this.employeeRepository = employeeRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(String id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public Employee updateEmployee(String id, Employee updatedEmployee) {
        return employeeRepository.findById(id)
                .map(existing -> {
                    updatedEmployee.setId(existing.getId());
                    return employeeRepository.save(updatedEmployee);
                })
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public void deleteEmployee(String id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found");
        }
        employeeRepository.deleteById(id);
    }

    public Employee patchEmployee(String id, Map<String, Object> body) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();
        for(String s: body.keySet()) {
            if(s.equals("contact") || s.equals("visaStatus") || s.equals("address") || s.equals("personalDocument")) {
                throw new RuntimeException("Cannot patch subdocuments, please remove and re-add");
            }
            update.set(s, body.get(s));
        }
        mongoTemplate.updateFirst(query, update, Employee.class);
        return this.getEmployeeById(id);
    }

    public Employee getEmployeeByUserId(String userId) {
        Query query= new Query(Criteria.where("userId").is(userId));
        Employee employee = mongoTemplate.findOne(query, Employee.class);
        if(employee == null) {
            throw new RuntimeException("Employee not found");
        }
        return employee;
    }

    public List<Employee> getEmployeesByHouseId(String houseId) {
        Query query= new Query(Criteria.where("houseId").is(houseId));
        return mongoTemplate.find(query, Employee.class);
    }
}
