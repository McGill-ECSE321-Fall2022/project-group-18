package com.example.museum.service;

import com.example.museum.model.*;
import com.example.museum.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    private CustomerRepository customerRepository;
    private EmployeeRepository employeeRepository;
    private OwnerRepository ownerRepository;

    public LoginService(@Autowired CustomerRepository customerRepository, @Autowired EmployeeRepository employeeRepository, @Autowired OwnerRepository ownerRepository) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.ownerRepository = ownerRepository;
    }

    //registers a new customer
    //restrictions:
    // - username must not conflict with any other user
    // - both username and password must contain 8+ characters (and all must be non-white space)
    //TODO: require username and password to be 8 characters long and non-white space
    public Customer createCustomer(Customer customer) throws Exception {
        try{
            String username = customer.getUsername();
            if(!conflictingUsername(username)){
                Customer createdCustomer = customerRepository.save(customer);
                return createdCustomer;
            }
        }catch(Exception e){
            System.out.println(e.getStackTrace());
            System.out.println("Please enter a new username. Current username is already taken");
        }
        return null;
    }

    // Checks all current users for password conflicts with a new password
    // Returns true if there is a conflict and false if there is no conflict
    private boolean conflictingUsername (String username) {
        for(Customer c : customerRepository.findAll()){
            if(c.getPassword().equals(username)) return true;
        }
        for(Employee e : employeeRepository.findAll()){
            if(e.getPassword().equals(username)) return true;
        }
        for(Owner o : ownerRepository.findAll()){
            if(o.getPassword().equals(username)) return true;
        }
        return false;
    }

    public Optional<Customer> retrieveCustomer(int id){
        return customerRepository.findById(id);
    }
}
