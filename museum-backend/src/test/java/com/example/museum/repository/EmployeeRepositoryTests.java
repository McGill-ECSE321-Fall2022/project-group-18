package com.example.museum.repository;

import com.example.museum.model.Employee;
import com.example.museum.model.EmployeeHour;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.sql.Time;

// Test of EmployeeRepo
// Springboot testing setup
@SpringBootTest
public class EmployeeRepositoryTests {
    // Injecting the employeeRepository
    @Autowired
    private EmployeeRepository employeeRepository;
    // Injecting the employeeHourRepository
    @Autowired
    private EmployeeHourRepository employeeHourRepository;

    // Clear the persistence data in database after each test
    @AfterEach
    public void clearDatabase() {
        employeeRepository.deleteAll();
        employeeHourRepository.deleteAll();
    }

    // The test method - read and write the object, its reference and its attributes
    @Test
    public void testPersistAndLoadEmployee() {
        // EmployeeHour
        Date day = Date.valueOf("2022-11-11");
        Time startTime = Time.valueOf("08:30:00");
        Time endTime = Time.valueOf("17:30:00");
        // Create a new employeeHour object with some attributes
        EmployeeHour employeeHour = new EmployeeHour();
        employeeHour.setDay(day);
        employeeHour.setStartTime(startTime);
        employeeHour.setEndTime(endTime);
        // Save the employeeHour in its database, tests its writing
        employeeHour = employeeHourRepository.save(employeeHour);
        // Keep track with its automatically generated ID
        int employeeHourId = employeeHour.getEmployeeHourID();

        String employeeUsername = "employee";
        String employeePassword = "password";
        // Create an employee object with some attributes and associations
        Employee employee = new Employee();
        employee.setUsername(employeeUsername);
        employee.setPassword(employeePassword);
        employee.setEmployeeHourList();
        employee.addEmployeeHour(employeeHour);
        // Save the employee in its database, tests its writing
        employee = employeeRepository.save(employee);
        // Keep track with its automatically generated ID
        int employeeId = employee.getAccountID();

        employee = null;
        employeeHour = null;
        // Read from the database using the id, test its reading
        employee = employeeRepository.findByAccountID(employeeId);
        // Test if an object is returned
        assertNotNull(employee);
        // Test if the attributes read from DB is correct
        assertEquals(employeeId, employee.getAccountID());
        assertEquals(employeeUsername, employee.getUsername());
        assertEquals(employeePassword, employee.getPassword());
        // Test if the associations are correct
        assertNotNull(employee.getEmployeeHour(0));
        assertEquals(employeeHourId,employee.getEmployeeHour(0).getEmployeeHourID());
        assertEquals(day, employee.getEmployeeHour(0).getDay());
        assertEquals(startTime, employee.getEmployeeHour(0).getStartTime());
        assertEquals(endTime, employee.getEmployeeHour(0).getEndTime());


    }
}
