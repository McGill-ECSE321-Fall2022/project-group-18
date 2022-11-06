package com.example.museum.controller;

import com.example.museum.model.Customer;
import com.example.museum.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createCustomer(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(body);
            Customer customer = mapper.readValue(body, Customer.class);
            Customer persistedCustomer = customerService.createCustomer(customer);
            return new ResponseEntity<>(persistedCustomer, HttpStatus.CREATED);
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("error");
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{id}")
    public ResponseEntity getCustomer(@PathVariable(name = "id") int id) {
        return customerService.retrieveCustomer(id)
                .map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
