package com.example.museum.service;

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
}
