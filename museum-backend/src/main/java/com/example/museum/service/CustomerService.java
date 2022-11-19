package com.example.museum.service;

import com.example.museum.model.*;
import com.example.museum.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;
    private EmployeeRepository employeeRepository;
    private OwnerRepository ownerRepository;

    public CustomerService(@Autowired CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(Customer customer) throws Exception {
        try {
            String username = customer.getUsername();
            if (!conflictingUsername(username)) {
                Customer createdCustomer = customerRepository.save(customer);
                return createdCustomer;
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            System.out.println("Please enter a new username. Current username is already taken");
        }

        return null;
    }

    private boolean conflictingUsername(String username) {
        for (Customer c : customerRepository.findAll()) {
            if (c.getPassword().equals(username))
                return true;
        }
        for (Employee e : employeeRepository.findAll()) {
            if (e.getPassword().equals(username))
                return true;
        }
        for (Owner o : ownerRepository.findAll()) {
            if (o.getPassword().equals(username))
                return true;
        }
        return false;
    }

    public Optional<Customer> retrieveCustomer(int id) {
        return customerRepository.findById(id);
    }
}
