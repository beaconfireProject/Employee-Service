package com.example.employee.service;

import com.example.employee.service.EmployeeService;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.domain.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void test_getEmployeeById_success()  {
        Employee expected = new Employee();
        expected.setId("11");
        expected.setFirstName("John");
        expected.setLastName("Smith");
        Optional<Employee> opt =  Optional.of(expected);
        doReturn(opt).when(employeeRepository).findById("11");
        assertEquals(expected, employeeService.getEmployeeById("11"));
    }

    @Test
    void test_getEmployeeById_Failure()  {
        doReturn(Optional.empty()).when(employeeRepository).findById("-1");
        assertThrows(RuntimeException.class, () -> employeeService.getEmployeeById("-1"));
    }

    @Test
    void test_getAllEmployees_success() {
        List<Employee> expected = new ArrayList<>();
        Employee expected1 = new Employee();
        expected1.setId("11");
        expected1.setFirstName("John");
        expected1.setLastName("Smith");
        expected.add(expected1);
        Employee expected2 = new Employee();
        expected2.setId("12");
        expected2.setFirstName("Jane");
        expected2.setLastName("Doe");
        expected.add(expected2);
        doReturn(expected).when(employeeRepository).findAll();
        assertEquals(expected, employeeService.getAllEmployees());
    }

    @Test
    void test_getAllEmployees_successWhenEmpty() {
        List<Employee> expected = new ArrayList<>();
        doReturn(expected).when(employeeRepository).findAll();
        assertEquals(expected, employeeService.getAllEmployees());
    }

    @Test
    void test_createEmployee_success() {
        Employee expected = new Employee();
        expected.setId("11");
        expected.setFirstName("John");
        expected.setLastName("Smith");
        doReturn(expected).when(employeeRepository).save(expected);
        assertEquals(expected, employeeService.createEmployee(expected));
    }

    @Test
    void test_updateEmployee_success() {
        Employee expected1 = new Employee();
        expected1.setId("11");
        expected1.setFirstName("John");
        expected1.setLastName("Smith");
        Employee expected2 = new Employee();
        expected2.setId("11");
        expected2.setFirstName("Jane");
        expected2.setLastName("Doe");
        doReturn(Optional.of(expected1)).when(employeeRepository).findById("11");
        doReturn(expected2).when(employeeRepository).save(expected2);
        assertEquals(expected2, employeeService.updateEmployee("11", expected2));
    }

    @Test
    void test_deleteEmployeeById_Failure()  {
        assertThrows(RuntimeException.class, () -> employeeService.deleteEmployee("-1"));
    }

    @Test
    void test_patchEmployeeById_Success()  {
        Employee expected1 = new Employee();
        expected1.setId("11");
        expected1.setFirstName("John");
        expected1.setLastName("Smith");
        Employee expected2 = new Employee();
        expected2.setId("11");
        expected2.setFirstName("John");
        expected2.setLastName("Doe");
        Map<String, Object> map = new HashMap<>();
        map.put("lastName", "Doe");
        Query query = new Query(Criteria.where("id").is("11"));
        Update update = new Update();
        update.set("lastName", "Doe");
        doReturn(Optional.of(expected2)).when(employeeRepository).findById("11");
        assertEquals(expected2, employeeService.patchEmployee("11", map));
    }

    @Test
    void test_patchEmployeeById_Failure()  {
        Map<String, Object> map = new HashMap<>();
        map.put("visaStatus", "Doe");
        Query query = new Query();
        Update update = new Update();
        assertThrows(RuntimeException.class, () -> employeeService.patchEmployee("11", map));
    }
}
