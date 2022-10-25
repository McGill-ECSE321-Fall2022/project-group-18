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

@SpringBootTest
public class EmployeeHourRepositoryTests {
    @Autowired
    private EmployeeHourRepository employeeHourRepository;

    @AfterEach
    public void clearDatabase() {
        employeeHourRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadEmployeeHour() {
        Date day = Date.valueOf("2022-11-11");
        Time startTime = Time.valueOf("08:30:00");
        Time endTime = Time.valueOf("17:30:00");

        EmployeeHour employeeHour = new EmployeeHour();
        employeeHour.setDay(day);
        employeeHour.setStartTime(startTime);
        employeeHour.setEndTime(endTime);


        employeeHour = employeeHourRepository.save(employeeHour);
        int employeeHourid = employeeHour.getEmployeeHourID();

        employeeHour = null;

        employeeHour = employeeHourRepository.findEmployeeHourByEmployeeHourID(employeeHourid);


        assertNotNull(employeeHour);
        assertEquals(employeeHourid, employeeHour.getEmployeeHourID());
        assertEquals(day, employeeHour.getDay());
        assertEquals(startTime, employeeHour.getStartTime());
        assertEquals(endTime, employeeHour.getEndTime());

        
    }
}