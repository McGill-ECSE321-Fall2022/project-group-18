package com.example.museum.repository;

import com.example.museum.model.Customer;
import com.example.museum.model.LoanRequest;
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
    @Autowired
    private LoanRequestRepository loanRequestRepository;

    @AfterEach
    public void clearDatabase() {
        customerRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadCustomer() {
        LoanRequest loanRequest = new LoanRequest();

        loanRequest = loanRequest.save(loanRequest);
        int loanRequestId = loanRequest.getRequestID();

        String customerUsername = "Customer";
        String customerPassword = "password";

        Customer customer = new Customer();
        customer.setUsername(customerUsername);
        customer.setPassword(customerPassword);
        customer.setLoanRequest(loanRequest);

        customer = customerRepository.save(customer);
        int customerId = customer.getAccountID();

        customer = null;
        loanRequest = null;

        customer = customerRepository.findCustomerByAccountID(customerId);
        loanRequest = loanRequestRepository.findLoanRequestByRequestID(loanRequestId);

        assertNotNull(customer);
        assertEquals(customerId, customer.getAccountID());
        assertEquals(customerUsername, customer.getUsername());
        assertEquals(customerPassword, customer.getPassword());
    }
}
