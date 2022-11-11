package com.example.museum.controller;

import com.example.museum.dto.CustomerDto;
import com.example.museum.model.Customer;
import com.example.museum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @PostMapping("/customer")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto request) {
        Customer customerToCreate = request.toModel();
        Customer createdCustomer = customerService.createCustomer(customerToCreate);
        CustomerDto response = new CustomerDto(createdCustomer);
        return new ResponseEntity<CustomerDto>(response, HttpStatus.CREATED);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable int id, @RequestBody CustomerDto request) {
        Customer customer = customerService.getCustomerByID(id);
        return new ResponseEntity<CustomerDto>(new CustomerDto(customer), HttpStatus.OK);
    }

    @PutMapping("/customer/{id}")
    public ResponseEntity<CustomerDto> updatedCustomer(@PathVariable int id, @RequestBody CustomerDto request) {
        Customer updatedCustomer = customerService.modifyCustomerByID(id, request.getUsername(), request.getPassword());
        CustomerDto response = new CustomerDto(updatedCustomer);
        return new ResponseEntity<CustomerDto>(response, HttpStatus.OK);
    }
}
