package com.example.museum.integration;

import com.example.museum.dto.ArtifactDto;
import com.example.museum.repository.EmployeeHourRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.museum.dto.EmployeeDto;
import com.example.museum.dto.EmployeeHourDto;
import com.example.museum.model.Employee;
import com.example.museum.model.EmployeeHour;
import com.example.museum.repository.EmployeeRepository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeIntegrationTest {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeHourRepository employeeHourRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        employeeRepository.deleteAll();
    }

    @Test
    public void testCreateGetUpdateEmployee() {
        EmployeeHour employeeHour = createEmployeeHour();
        int id = testCreateEmployee(employeeHour);
        testGetEmployee(id);
        testGetAllEmployees(id);
        testGetEmployeeHours(id);
        testLoginEmployee();
        testInvalidLoginEmployee();
        testCreateInvalidEmployee();
        testUpdateInvalidEmployee();
        // testUpdateEmployee(id);
        testDeleteInvalidEmployee();
        testDeleteEmployee(id);
    }

    private EmployeeHour createEmployeeHour() {
        final Date day = Date.valueOf("2022-11-11");
        final Time startTime = Time.valueOf("08:30:00");
        final Time endTime = Time.valueOf("17:30:00");
        final EmployeeHour employeeHourDto = new EmployeeHour(0, day, startTime, endTime);
        ResponseEntity<EmployeeHourDto> response = client.postForEntity("/employeeHour", employeeHourDto,
                EmployeeHourDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        return response.getBody().toModel();
    }

    private int testCreateEmployee(EmployeeHour employeeHour) {
        final String username = "employee1";
        final String password = "password";
        final String firstName = "Employee";
        final String lastName = "Account";
        final Employee employee = new Employee(0, username, password, firstName, lastName);
        employee.addEmployeeHour(employeeHour);
        final EmployeeDto employeeDto = new EmployeeDto(employee);

        ResponseEntity<EmployeeDto> response = client.postForEntity("/employee", employeeDto, EmployeeDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getAccountID() > 0);
        assertEquals(username, response.getBody().getUsername());
        assertEquals(password, response.getBody().getPassword());
        assertEquals(firstName, response.getBody().getFirstName());
        assertEquals(lastName, response.getBody().getLastName());
        assertEquals(1, response.getBody().getEmployeeHours().size());
        assertEquals(employeeHour.getEmployeeHourID(),
                response.getBody().getEmployeeHours().get(0).getEmployeeHourID());
        assertEquals(employeeHour.getDay(), response.getBody().getEmployeeHours().get(0).getDay());
        assertEquals(employeeHour.getStartTime(), response.getBody().getEmployeeHours().get(0).getStartTime());
        assertEquals(employeeHour.getEndTime(), response.getBody().getEmployeeHours().get(0).getEndTime());

        return response.getBody().getAccountID();
    }

    private void testLoginEmployee() {
        final String username = "employee1";
        final String password = "password";
        final String firstName = "Employee";
        final String lastName = "Account";
        final EmployeeDto employeeDto = new EmployeeDto(
                new Employee(0, username, password, firstName, lastName));

        ResponseEntity<Integer> response = client.postForEntity("/employee/login", employeeDto, Integer.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0);
    }

    private void testInvalidLoginEmployee() {
        final String username = "employee1";
        final String password = "wrongPassword";
        final String firstName = "Employeee";
        final String lastName = "Account";
        final EmployeeDto employeeDto = new EmployeeDto(
                new Employee(0, username, password, firstName, lastName));

        ResponseEntity<String> response = client.postForEntity("/employee/login", employeeDto, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Wrong password", response.getBody());
    }

    private void testGetEmployee(int id) {
        final String username = "employee1";
        final String password = "password";
        final String firstName = "Employee";
        final String lastName = "Account";
        ResponseEntity<EmployeeDto> response = client.getForEntity("/employee/" + id, EmployeeDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getAccountID());
        assertEquals(username, response.getBody().getUsername());
        assertEquals(password, response.getBody().getPassword());
        assertEquals(firstName, response.getBody().getFirstName());
        assertEquals(lastName, response.getBody().getLastName());
    }

    private void testGetAllEmployees(int id) {
        final String username = "employee1";
        final String password = "password";
        final String firstName = "Employee";
        final String lastName = "Account";
        ResponseEntity<List<EmployeeDto>> response = client.exchange("/employee/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<EmployeeDto>>() {});
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().get(0).getAccountID());
        assertEquals(username, response.getBody().get(0).getUsername());
        assertEquals(password, response.getBody().get(0).getPassword());
        assertEquals(firstName, response.getBody().get(0).getFirstName());
        assertEquals(lastName, response.getBody().get(0).getLastName());
    }

    private void testGetEmployeeHours(int id) {
        final Date day = Date.valueOf("2022-11-11");
        final Time startTime = Time.valueOf("08:30:00");
        final Time endTime = Time.valueOf("17:30:00");

        ResponseEntity<List<EmployeeHourDto>> response = client.exchange(
                "/employee/" + id + "/employeeHours",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<EmployeeHourDto>>() {
                });

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(day, response.getBody().get(0).getDay());
        assertEquals(startTime, response.getBody().get(0).getStartTime());
        assertEquals(endTime, response.getBody().get(0).getEndTime());
    }

    private void testCreateInvalidEmployee() {
        // Create user with existing username
        final String username = "employee1";
        final String password = "password";
        final String firstName = "SecondEmployee";
        final String lastName = "Account";
        final EmployeeDto employeeDto = new EmployeeDto(
                new Employee(0, username, password, firstName, lastName));

        try {
            ResponseEntity<EmployeeDto> response = client.postForEntity("/employee", employeeDto, EmployeeDto.class);
            assertEquals(1, 2);
        } catch (Exception e) {
            ResponseEntity<String> response = client.postForEntity("/employee", employeeDto, String.class);
            assertNotNull(response);
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        }
    }

    private void testUpdateInvalidEmployee() {
        int id = Integer.MAX_VALUE;
        EmployeeHour hour = new EmployeeHour();
        Employee employee = new Employee();
        employee.setUsername("krab");
        employee.setPassword("mr");
        employee.addEmployeeHour(hour);
        EmployeeDto employeeDto = new EmployeeDto(employee);
        try {
            ResponseEntity<Boolean> response = client.postForEntity("/employee/update/" + id, employeeDto,
                    Boolean.class);
            assertEquals(1, 2);
        } catch (Exception e) {
            ResponseEntity<String> response = client.postForEntity("/employee/update/" + id, employeeDto, String.class);
            assertNotNull(response);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Employee not found", response.getBody());
        }
    }

    @Test
    public void testUpdateEmployee() {
        EmployeeDto employeeDto = testCreateAndUpdateEmployee();
    }

    private EmployeeDto testCreateAndUpdateEmployee() {
        final String username = "employee1";
        final String password = "password";
        final String firstName = "Employee";
        final String lastName = "Account";
        Employee emp = employeeRepository.save(new Employee(0, username, password, firstName, lastName));
        int id = emp.getAccountID();
        Date day = Date.valueOf("2020-10-10");
        Time start = Time.valueOf("08:08:08");
        Time end = Time.valueOf("14:14:14");
        EmployeeHour hour = new EmployeeHour(0, day, start, end);
        Employee employee = new Employee();
        employee.setUsername("krab");
        employee.setPassword("mr");
        employee.addEmployeeHour(hour);
        EmployeeDto employeeDto = new EmployeeDto(employee);
        ResponseEntity<EmployeeDto> response = client.postForEntity("/employee/update/" + id, employeeDto,
                EmployeeDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getAccountID());
        assertEquals("krab", response.getBody().getUsername());
        assertEquals("mr", response.getBody().getPassword());
        // checking employee hour
        assertEquals(1, response.getBody().getEmployeeHours().size());
        assertEquals(day, response.getBody().getEmployeeHours().get(0).getDay());
        assertNotEquals(0, response.getBody().getEmployeeHours().get(0).getEmployeeHourID());
        return response.getBody();
    }

    private void testDeleteInvalidEmployee() {
        int id = Integer.MAX_VALUE;
        EmployeeDto employeeDto = new EmployeeDto();
        try {
            ResponseEntity<Boolean> response = client.postForEntity("/employee/delete/" + id, employeeDto,
                    Boolean.class);
            assertEquals(1, 2);
        } catch (Exception e) {
            ResponseEntity<String> response = client.postForEntity("/employee/delete/" + id, employeeDto, String.class);
            assertNotNull(response);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    private void testDeleteEmployee(int employeeID) {
        int id = employeeID;
        EmployeeDto employeeDto = new EmployeeDto();
        ResponseEntity<String> response = client.postForEntity("/employee/delete/" + id, employeeDto, String.class);
        assertNotNull(response);
        assertEquals("Employee deleted successfully.", response.getBody());
        assertNull(employeeRepository.findByAccountID(id));
    }
}
