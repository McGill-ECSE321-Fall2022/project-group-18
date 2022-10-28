package com.example.museum.repository;

import com.example.museum.model.EmployeeHour;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Test of EmployeeHourRepo
// Springboot testing setup
@SpringBootTest
public class EmployeeHourRepositoryTests {
    // Injecting the employeeHourRepository
    @Autowired
    private EmployeeHourRepository employeeHourRepository;

    // Clear the persistence data in database after each test
    @AfterEach
    public void clearDatabase() {
        employeeHourRepository.deleteAll();
    }

    // The test method - read and write the object, its reference and its attributes
    @Test
    public void testPersistAndLoadEmployeeHour() {
        Date day = Date.valueOf("2022-11-11");
        Time startTime = Time.valueOf("08:30:00");
        Time endTime = Time.valueOf("17:30:00");

        // Create an employeeHour object with some attributes
        EmployeeHour employeeHour = new EmployeeHour();
        employeeHour.setDay(day);
        employeeHour.setStartTime(startTime);
        employeeHour.setEndTime(endTime);

        // Save the employeeHour object in the DB
        employeeHour = employeeHourRepository.save(employeeHour);
        // Keep track with the automatically generated ID
        int employeeHourid = employeeHour.getEmployeeHourID();

        employeeHour = null;
        // Read from the database using the id, test its reading
        employeeHour = employeeHourRepository.findEmployeeHourByEmployeeHourID(employeeHourid);
        // Test if an object is returned
        assertNotNull(employeeHour);
        // Test if the attributes read from DB is correct
        assertEquals(employeeHourid, employeeHour.getEmployeeHourID());
        assertEquals(day, employeeHour.getDay());
        assertEquals(startTime, employeeHour.getStartTime());
        assertEquals(endTime, employeeHour.getEndTime());

        
    }
}