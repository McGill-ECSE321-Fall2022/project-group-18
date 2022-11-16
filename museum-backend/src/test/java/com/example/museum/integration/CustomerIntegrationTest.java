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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.museum.dto.CustomerDto;
import com.example.museum.dto.TicketDto;
import com.example.museum.model.Customer;
import com.example.museum.model.Ticket;
import com.example.museum.repository.CustomerRepository;

import java.sql.Date;
import java.util.List;

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
        Ticket ticket = createTicket();
        int id = testCreateCustomer(ticket);
        testGetCustomer(id);
        // testGetCustomerTickets(id);
        testLoginCustomer();
        testInvalidLoginCustomer();
        testCreateInvalidCustomer();
    }

    private Ticket createTicket() {
        final Date day = Date.valueOf("2022-11-08");
        final int price = 25;
        final Ticket ticket = new Ticket(0, day, price);
        final TicketDto ticketDto = new TicketDto(ticket);

        ResponseEntity<TicketDto> response = client.postForEntity("/ticket", ticketDto, TicketDto.class);

        return response.getBody().toModel();
    }

    private int testCreateCustomer(Ticket ticket) {
        final String username = "customer1";
        final String password = "password";
        final String firstName = "Customer";
        final String lastName = "Account";
        final int credit = 10;
        final Customer customer = new Customer(0, username, password, firstName, lastName, credit);
        customer.addCustomerTicket(ticket);
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
        assertEquals(1, response.getBody().getCustomerTickets().size());
        assertEquals(ticket.getTicketID(), response.getBody().getCustomerTickets().get(0).getTicketID());
        assertEquals(ticket.getDay(), response.getBody().getCustomerTickets().get(0).getDay());
        assertEquals(ticket.getPrice(), response.getBody().getCustomerTickets().get(0).getPrice());

        return response.getBody().getAccountID();
    }

    private void testLoginCustomer() {
        final String username = "customer1";
        final String password = "password";
        final String firstName = "Customer";
        final String lastName = "Account";
        final int credit = 10;
        final CustomerDto customerDto = new CustomerDto(
                new Customer(0, username, password, firstName, lastName, credit));

        ResponseEntity<String> response = client.postForEntity("/customer/login", customerDto, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successful login", response.getBody());
    }

    private void testInvalidLoginCustomer() {
        final String username = "customer1";
        final String password = "wrongPassword";
        final String firstName = "Customer";
        final String lastName = "Account";
        final int credit = 10;
        final CustomerDto customerDto = new CustomerDto(
                new Customer(0, username, password, firstName, lastName, credit));

        ResponseEntity<String> response = client.postForEntity("/customer/login", customerDto, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Wrong password", response.getBody());
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

    private void testGetCustomerTickets(int id) {
        final Date day = Date.valueOf("2022-11-08");
        final int price = 25;

        ResponseEntity<List<TicketDto>> response = client.exchange(
                "/customer/" + id + "/ticket",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TicketDto>>() {
                });

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(day, response.getBody().get(0).getDay());
        assertEquals(price, response.getBody().get(0).getPrice());
    }

    private void testCreateInvalidCustomer() {
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
