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


@SpringBootTest
public class EmployeeRepositoryTests {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeHourRepository employeeHourRepository;

    @AfterEach
    public void clearDatabase() {
        employeeRepository.deleteAll();
        employeeHourRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadEmployee() {
        // EmployeeHour
        Date day = Date.valueOf("2022-11-11");
        Time startTime = Time.valueOf("08:30:00");
        Time endTime = Time.valueOf("17:30:00");
        EmployeeHour employeeHour = new EmployeeHour();
        employeeHour.setDay(day);
        employeeHour.setStartTime(startTime);
        employeeHour.setEndTime(endTime);
        employeeHour = employeeHourRepository.save(employeeHour);
        int employeeHourId = employeeHour.getEmployeeHourID();

        String employeeUsername = "employee";
        String employeePassword = "password";

        Employee employee = new Employee();
        employee.setUsername(employeeUsername);
        employee.setPassword(employeePassword);
        employee.setEmployeeHourList();
        employee.addEmployeeHour(employeeHour);

        employee = employeeRepository.save(employee);
        int employeeId = employee.getAccountID();

        employee = null;
        employeeHour = null;

        employee = employeeRepository.findByAccountID(employeeId);
        

        assertNotNull(employee);
        assertEquals(employeeId, employee.getAccountID());
        assertEquals(employeeUsername, employee.getUsername());
        assertEquals(employeePassword, employee.getPassword());
        assertNotNull(employee.getEmployeeHour(0));
        assertEquals(employeeHourId,employee.getEmployeeHour(0).getEmployeeHourID());


    }
}
