package com.example.museum.service;

import com.example.museum.exceptions.RequestException;
import com.example.museum.repository.CustomerRepository;
import com.example.museum.repository.EmployeeHourRepository;
import com.example.museum.repository.OwnerRepository;
import org.hibernate.dialect.Database;
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
import com.example.museum.repository.EmployeeRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    OwnerRepository ownerRepository;
    @Mock
    EmployeeHourRepository employeeHourRepository;


    @InjectMocks
    EmployeeService employeeService;


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

    @Test
    void deleteEmployeeByIDNotFound(){
        final int employeeID = 1;
        final String username = "employee";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "Last";
        final Employee employee = new Employee(employeeID, username, password, firstName, lastName);

        when(employeeRepository.findByAccountID(employeeID)).thenAnswer((InvocationOnMock) -> null);

        Exception ex = assertThrows(DatabaseException.class, () -> employeeService.deleteEmployeeByID(employeeID));
        assertEquals("Employee not found", ex.getMessage());
    }

    @Test
    void deleteEmployeeByID(){
        final int employeeID = 1;
        final String username = "employee";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "Last";
        final Employee employee = new Employee(employeeID, username, password, firstName, lastName);

        when(employeeRepository.findByAccountID(employeeID)).thenAnswer((InvocationOnMock) -> employee);
        //not sure how to mock a void method (the deletion method), but this does the trick

        assertTrue(employeeService.deleteEmployeeByID(employeeID));
    }

    @Test
    void testModifyEmployeeByIDNotFound(){
        final int employeeID = 1;
        final String username = "employee";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "Last";
        final Employee employee = new Employee(employeeID, username, password, firstName, lastName);

        when(employeeRepository.findByAccountID(employeeID)).thenAnswer((InvocationOnMock) -> null);

        Exception ex = assertThrows(DatabaseException.class, () -> employeeService.modifyEmployeeByID(employeeID, username, password, firstName, lastName, employee.getEmployeeHours()));
        assertEquals("Employee not found", ex.getMessage());
    }

    @Test
    void testModifyEmployeeByIDConflictingUsername(){
        final int employeeID = 1;
        final String username = "employee";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "Last";
        final Employee employee1 = new Employee(employeeID, username, password, firstName, lastName);
        final Employee employee2 = new Employee(employeeID + 1, username, password, firstName, lastName);
        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);

        when(employeeRepository.findByAccountID(employeeID)).thenAnswer((InvocationOnMock) -> employee1);
        when(employeeRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> employees);
        when(customerRepository.findAll()).thenAnswer((InvocationOnMock) -> new ArrayList<>());


        Exception ex = assertThrows(DatabaseException.class, () -> employeeService.modifyEmployeeByID(employeeID, username, password, firstName, lastName, employee1.getEmployeeHours()));
        assertEquals("An employee with the given username already exists.", ex.getMessage());
    }

    @Test
    void testModifyEmployeeByID(){
        final int employeeID1 = 1;
        final int employeeID2 = 2;
        final String username1 = "employee1";
        final String username2 = "employee2";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "Last";
        final String requestedUsername = "test";
        final String requestedPassword = "pw";
        final String requestedFirstName = "ME";
        final String requestedLastName = "TOO";
        final Employee employee1 = new Employee(employeeID1, username1, password, firstName, lastName);
        final Employee employee2 = new Employee(employeeID2, username2, password, firstName, lastName);
        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);

        when(employeeRepository.findByAccountID(employeeID1)).thenAnswer((InvocationOnMock) -> employee1);
        when(employeeRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> employees);
        when(customerRepository.findAll()).thenAnswer((InvocationOnMock) -> new ArrayList<>());
        when(employeeRepository.save(any(Employee.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        Employee response = employeeService.modifyEmployeeByID(employeeID1, requestedUsername, requestedPassword, requestedFirstName, requestedLastName, employee1.getEmployeeHours());

        assertNotNull(response);
        assertEquals(employeeID1, response.getAccountID());
        assertEquals(requestedUsername, response.getUsername());
        assertEquals(requestedPassword, response.getPassword());
        assertEquals(requestedFirstName, response.getFirstName());
        assertEquals(requestedLastName, response.getLastName());

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testCreateEmployeeExists(){
        final int employeeID = 1;
        final String username = "employee1";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "Last";
        Employee employee = new Employee(employeeID, username, password, firstName, lastName);

        when(employeeRepository.findByAccountID(employeeID)).thenAnswer((InvocationOnMock) -> employee);

        Exception ex = assertThrows(DatabaseException.class, () -> employeeService.createEmployee(employee));
        assertEquals("An employee with the given id already exists.", ex.getMessage());
    }

    @Test
    void testCreateEmployee(){
        final int employeeID = 1;
        final String username = "employee1";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "Last";
        Employee employee = new Employee(employeeID, username, password, firstName, lastName);

        when(employeeRepository.findByAccountID(employeeID)).thenAnswer((InvocationOnMock) -> null);
        when(employeeRepository.save(any(Employee.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        Employee response = employeeService.createEmployee(employee);
        assertNotNull(response);
        assertEquals(employeeID, response.getAccountID());
        assertEquals(username, response.getUsername());
        assertEquals(password, response.getPassword());
        assertEquals(firstName, response.getFirstName());
        assertEquals(lastName, response.getLastName());

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee(){
        final int employeeID1 = 1;
        final int employeeID2 = 2;
        final String username1 = "employee1";
        final String username2 = "employee2";
        final String password1 = "password";
        final String password2 = "password2";
        final String firstName1 = "First1";
        final String firstName2 = "First2";
        final String lastName1 = "Last1";
        final String lastName2 = "Last2";
        Employee employee1 = new Employee(employeeID1, username1, password1, firstName1, lastName1);
        Employee employee2 = new Employee(employeeID2, username2, password2, firstName2, lastName2);

        final int hourID1 = 3;
        final int hourID2 = 4;
        final int hourID3 = 5;
        Date day1 = Date.valueOf("2020-10-10");
        Date day2 = Date.valueOf("2021-10-10");
//        Date day3 = Date.valueOf("2022-10-10");
        Time start = Time.valueOf("8:00:00");
        Time end = Time.valueOf("19:00:00");
        EmployeeHour hour1 = new EmployeeHour(hourID1,day1, start, end);
        //same date - on purpose
        EmployeeHour hour2 = new EmployeeHour(hourID2, day2, start, end);
        EmployeeHour hour3 = new EmployeeHour(hourID3, day2, start, end);

        employee1.addEmployeeHour(hour1);
        employee2.addEmployeeHour(hour2);

        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);

        //list of hours that will override the previous list of hours for the employee
        List<EmployeeHour> newHours = new ArrayList<>();
        newHours.add(hour1);
        newHours.add(hour3);

        //hour 3 is missing. now we will add it to the first employee and sure that no exception is thrown.
        //in another test, we would add this to employee 2 (see below) and ensure that an exception is thrown
        when(employeeRepository.findByAccountID(employeeID1)).thenAnswer((InvocationOnMock) -> employee1);
        when(employeeRepository.save(any(Employee.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
        when(employeeHourRepository.save(any(EmployeeHour.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        Employee response = employeeService.modifyEmployeeByID(employeeID1, username1, password1, firstName1, lastName1, newHours);

        assertNotNull(response);
        assertEquals(2, response.getEmployeeHours().size());
        assertEquals(hourID1, response.getEmployeeHour(0).getEmployeeHourID());
        assertEquals(day1, response.getEmployeeHour(0).getDay());
        assertEquals(hourID3, response.getEmployeeHour(1).getEmployeeHourID());
        assertEquals(day2, response.getEmployeeHour(1).getDay());
    }

    @Test
    void testUpdateEmployeeWithConflictingEmployeeHours(){
        final int employeeID1 = 1;
        final int employeeID2 = 2;
        final String username1 = "employee1";
        final String username2 = "employee2";
        final String password1 = "password";
        final String password2 = "password2";
        final String firstName1 = "First1";
        final String firstName2 = "First2";
        final String lastName1 = "Last1";
        final String lastName2 = "Last2";
        Employee employee1 = new Employee(employeeID1, username1, password1, firstName1, lastName1);
        Employee employee2 = new Employee(employeeID2, username2, password2, firstName2, lastName2);

        final int hourID1 = 3;
        final int hourID2 = 4;
        final int hourID3 = 5;
        Date day1 = Date.valueOf("2020-10-10");
        Date day2 = Date.valueOf("2021-10-10");
//        Date day3 = Date.valueOf("2022-10-10");
        Time start = Time.valueOf("8:00:00");
        Time end = Time.valueOf("19:00:00");
        EmployeeHour hour1 = new EmployeeHour(hourID1,day1, start, end);
        //same date - on purpose
        EmployeeHour hour2 = new EmployeeHour(hourID2, day2, start, end);
        EmployeeHour hour3 = new EmployeeHour(hourID3, day2, start, end);

        employee1.addEmployeeHour(hour1);
        employee2.addEmployeeHour(hour2);

        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);

        //list of hours that will override the previous list of hours for the employee2
        List<EmployeeHour> newHours = new ArrayList<>();
        newHours.add(hour2);
        newHours.add(hour3);

        //hour 3 is missing. now we will add it to the first employee and sure that no exception is thrown.
        //in this test, we would add this to employee 2 and ensure that an exception is thrown
        when(employeeRepository.findByAccountID(employeeID1)).thenAnswer((InvocationOnMock) -> employee1);
        when(employeeHourRepository.save(any(EmployeeHour.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        Exception ex = assertThrows(DatabaseException.class, () -> employeeService.modifyEmployeeByID(employeeID1, username1, password1, firstName1, lastName1, newHours));
        assertEquals("A EmployeeHour with the given date already exists for this employee", ex.getMessage());
    }
}