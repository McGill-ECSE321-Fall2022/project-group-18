package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.exceptions.RequestException;
import com.example.museum.model.*;
import com.example.museum.repository.*;
import com.example.museum.service.utils.ServiceUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    @Transactional
    public Customer getCustomerByID(int id) {
        Customer customer = customerRepository.findByAccountID(id);
        if (customer == null) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        return customer;
    }

    @Transactional
    public void loginCustomer(Customer customerRequest) {
        Iterator<Customer> customers = customerRepository.findAll().iterator();

        while (customers.hasNext()) {
            Customer customer = customers.next();
            if (customer.getUsername().equals(customerRequest.getUsername())) {
                if (customer.getPassword().equals(customerRequest.getPassword())) {
                    return;
                } else {
                    throw new RequestException(HttpStatus.BAD_REQUEST, "Wrong password");
                }
            }
        }

        throw new DatabaseException(HttpStatus.NOT_FOUND, "Customer does not exist");
    }

    @Transactional
    public Iterator<Customer> getAllCustomers() {
        Iterator<Customer> customers = customerRepository.findAll().iterator();
        return customers;
    }

    @Transactional
    public Customer createCustomer(Customer customerRequest) {
        if (customerRepository.findByAccountID(customerRequest.getAccountID()) != null) {
            throw new DatabaseException(HttpStatus.CONFLICT, "A customer with the given id already exists.");
        }

        if (ServiceUtils.conflictingUsername(customerRequest.getUsername(), customerRequest.getAccountID(), customerRepository, employeeRepository,
                ownerRepository)) {
            throw new DatabaseException(HttpStatus.CONFLICT, "A customer with the given username already exists.");
        }
        Customer customer = customerRepository.save(customerRequest);
        return customer;
    }

    // private boolean conflictingUsername(String username) {
    // for (Customer c : customerRepository.findAll()) {
    // if (c.getPassword().equals(username))
    // return true;
    // }
    // for (Employee e : employeeRepository.findAll()) {
    // if (e.getPassword().equals(username))
    // return true;
    // }
    // for (Owner o : ownerRepository.findAll()) {
    // if (o.getPassword().equals(username))
    // return true;
    // }
    // return false;
    // }

    public Customer modifyCustomerByID(int id, String username, String password, String firstName, String lastName) {
        Customer customer = customerRepository.findByAccountID(id);
        if(customer == null){
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Customer not found");
        }
        if (ServiceUtils.conflictingUsername(username, id, customerRepository, employeeRepository,
                ownerRepository)) {
            throw new DatabaseException(HttpStatus.CONFLICT, "An customer with the given username already exists.");
        }
        customer.setUsername(username);
        customer.setPassword(password);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        Customer updatedCustomer = customerRepository.save(customer);
        return updatedCustomer;
    }

    @Transactional
    public void deleteCustomerByID(int id) {
        Customer customer = customerRepository.findByAccountID(id);
        if (customer == null) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        customerRepository.deleteById(id);
    }
}
