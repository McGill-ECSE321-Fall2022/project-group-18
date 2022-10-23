package com.example.museum.repository;

import com.example.museum.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    public void clearDatabase() {
        customerRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadCustomer() {
        String customerUsername = "Customer";
        String customerPassword = "password";

        Customer customer = new Customer();
        customer.setUsername(customerUsername);
        customer.setPassword(customerPassword);

        customer = customerRepository.save(customer);
        int customerId = customer.getAccountID();

        customer = null;

        customer = customerRepository.findByAccountID(customerId);

        assertNotNull(customer);
        assertEquals(customerId, customer.getAccountID());
        assertEquals(customerUsername, customer.getUsername());
        assertEquals(customerPassword, customer.getPassword());
    }
}
