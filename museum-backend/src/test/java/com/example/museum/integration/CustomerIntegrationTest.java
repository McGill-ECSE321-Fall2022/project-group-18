package com.example.museum.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.museum.dto.CustomerDto;
import com.example.museum.model.Customer;
import com.example.museum.repository.CustomerRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        customerRepository.deleteAll();
    }

    @Test
    public void testCreateGetUpdateCustomer() {
        int id = testCreateCustomer();
        testCreateInvalidCustomer();
        testGetCustomer(id);
    }

    private int testCreateCustomer() {
        final String username = "customer1";
        final String password = "password";
        final String firstName = "Customer";
        final String lastName = "Account";
        final int credit = 10;
        final Customer customer = new Customer(0, username, password, firstName, lastName, credit);
        final CustomerDto customerDto = new CustomerDto(customer);

        ResponseEntity<CustomerDto> response = client.postForEntity("/customer", customerDto, CustomerDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getAccountID() > 0);
        assertEquals(username, response.getBody().getUsername());
        assertEquals(password, response.getBody().getPassword());
        assertEquals(firstName, response.getBody().getFirstName());
        assertEquals(lastName, response.getBody().getLastName());
        assertEquals(credit, response.getBody().getCredit());

        return response.getBody().getAccountID();
    }

    private void testGetCustomer(int id) {
        final String username = "customer1";
        final String password = "password";
        final String firstName = "Customer";
        final String lastName = "Account";
        final int credit = 10;
        ResponseEntity<CustomerDto> response = client.getForEntity("/customer/" + id, CustomerDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getAccountID());
        assertEquals(username, response.getBody().getUsername());
        assertEquals(password, response.getBody().getPassword());
        assertEquals(firstName, response.getBody().getFirstName());
        assertEquals(lastName, response.getBody().getLastName());
        assertEquals(credit, response.getBody().getCredit());
    }

    @Test
    public void testCreateInvalidCustomer() {
        // Create user with existing username
        final String username = "customer1";
        final String password = "password";
        final String firstName = "SecondCustomer";
        final String lastName = "Account";
        final int credit = 20;
        final CustomerDto customerDto = new CustomerDto(
                new Customer(0, username, password, firstName, lastName, credit));

        try {
            ResponseEntity<CustomerDto> response = client.postForEntity("/customer", customerDto, CustomerDto.class);
            assertEquals(1, 2);
        } catch (Exception e) {
            ResponseEntity<String> response = client.postForEntity("/customer", customerDto, String.class);
            assertNotNull(response);
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        }
    }
}
