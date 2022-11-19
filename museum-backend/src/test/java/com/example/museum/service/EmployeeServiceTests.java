package com.example.museum.service;

import com.example.museum.exceptions.RequestException;
import com.example.museum.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Employee;
import com.example.museum.model.EmployeeHour;
import com.example.museum.repository.EmployeeHourRepository;
import com.example.museum.repository.EmployeeRepository;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    EmployeeHourRepository employeeHourRepository;

    @InjectMocks
    EmployeeService employeeService;
    @InjectMocks
    EmployeeHourService employeeHourService;

    @Test
    public void testGetEmployeeID() {
        // EmployeeHour
        final int employeeHourId = 1;
        final Date day = Date.valueOf("2022-11-11");
        final Time startTime = Time.valueOf("08:30:00");
        final Time endTime = Time.valueOf("17:30:00");
        final EmployeeHour testEmployeeHour = new EmployeeHour(employeeHourId, day, startTime, endTime);

        // Employee
        final int employeeID = 2;
        final String username = "employee1";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "Last";
        final Employee testEmployee = new Employee(employeeID, username, password, firstName, lastName);
        testEmployee.setEmployeeHourList();
        testEmployee.addEmployeeHour(testEmployeeHour);

        when(employeeRepository.findByAccountID(employeeID)).thenAnswer((InvocationOnMock invocation) -> testEmployee);

        Employee employee = employeeService.getEmployeeByID(employeeID);

        // Testing employee
        assertNotNull(employee);
        assertEquals(employeeID, employee.getAccountID());
        assertEquals(username, employee.getUsername());
        assertEquals(password, employee.getPassword());
        assertEquals(firstName, employee.getFirstName());
        assertEquals(lastName, employee.getLastName());
        // Testing associations
        assertEquals(employee.getEmployeeHour(0).getEmployeeHourID(),
                testEmployee.getEmployeeHour(0).getEmployeeHourID());
    }

    @Test
    void testGetEmployeeByInvalidID() {
        final int invalidID = 11111;

        when(employeeRepository.findByAccountID(invalidID)).thenAnswer((InvocationOnMock) -> null);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> employeeService.getEmployeeByID(invalidID));

        assertEquals("Employee not found", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void testEmployeeLoginDoesNotExist(){
        final int employeeID = 1;
        final String username = "employee";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "Last";
        final Employee testEmployee = new Employee(employeeID, username, password, firstName, lastName);
        final List<Employee> employees = new ArrayList<>();
        when(employeeRepository.findAll()).thenAnswer((InvocationOnMock) -> employees);

        Exception ex = assertThrows(DatabaseException.class, () -> employeeService.loginEmployee(testEmployee));
        assertEquals("Employee does not exist", ex.getMessage());
    }

    @Test
    void testEmployeeLoginWrongPassword(){
        final int employeeID = 1;
        final String username = "employee";
        final String password = "password";
        final String wrongpassword = "pass";
        final String firstName = "First";
        final String lastName = "Last";
        final Employee testEmployee = new Employee(employeeID, username, password, firstName, lastName);
        final Employee actualEmployee = new Employee(employeeID, username, wrongpassword, firstName, lastName);
        List<Employee> employees = new ArrayList<>();
        employees.add(actualEmployee);

        when(employeeRepository.findAll()).thenAnswer((InvocationOnMock) -> employees);

        Exception ex = assertThrows(RequestException.class, () -> employeeService.loginEmployee(testEmployee));
        assertEquals("Wrong password", ex.getMessage());
    }

    @Test
    void testEmployeeLogin(){
        final int employeeID = 1;
        final String username = "employee";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "Last";
        final Employee testEmployee = new Employee(employeeID, username, password, firstName, lastName);
        final Employee storedEmployee = new Employee(employeeID, username, password, firstName, lastName);
        List<Employee> employees = new ArrayList<>();
        employees.add(storedEmployee);

        when(employeeRepository.findAll()).thenAnswer((InvocationOnMock) -> employees);

        assertDoesNotThrow(() -> employeeService.loginEmployee(testEmployee));
    }
}