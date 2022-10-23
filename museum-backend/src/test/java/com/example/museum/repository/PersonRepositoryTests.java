package com.example.museum.repository;

import com.example.museum.model.Person;
import com.example.museum.model.Owner;
import com.example.museum.model.Customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PersonRepositoryTests {
    @Autowired
    private PersonRepository personRepository;

    @AfterEach
    public void clearDatabase() {
        personRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadPerson() {
        String customerUsername = "Customer";
        String customerPassword = "password";

        Person customer = new Customer();
        customer.setUsername(customerUsername);
        customer.setPassword(customerPassword);

        customer = personRepository.save(customer);
        int customerId = customer.getAccountID();

        customer = null;

        customer = personRepository.findPersonByAccountID(customerId);

        assertNotNull(customer);
        assertEquals(customerId, customer.getAccountID());
        assertEquals(customerUsername, customer.getUsername());
        assertEquals(customerPassword, customer.getPassword());
    }
}
