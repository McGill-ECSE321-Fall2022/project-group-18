package com.example.museum.controller;

import com.example.museum.dto.CustomerDto;
import com.example.museum.dto.DonationDto;
import com.example.museum.dto.LoanDto;
import com.example.museum.dto.TicketDto;
import com.example.museum.model.Customer;
import com.example.museum.model.Loan;
import com.example.museum.service.*;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:8087")
@RestController
public class CustomerController {
    @Autowired
    CustomerService customerService;

    // create customer using received CustomerDto
    @PostMapping("/customer")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto request) {
        Customer customerToCreate = request.toModel();
        Customer createdCustomer = customerService.createCustomer(customerToCreate);
        CustomerDto response = new CustomerDto(createdCustomer);
        return new ResponseEntity<CustomerDto>(response, HttpStatus.CREATED);
    }

    // check if the input login information is correct
    @PostMapping("/customer/login")
    public ResponseEntity<Integer> loginCustomer(@RequestBody CustomerDto request) {
        Customer customerToLogin = request.toModel();
        int id = customerService.loginCustomer(customerToLogin);
        return new ResponseEntity<Integer>(id, HttpStatus.OK);
    }

    // get the CustomerDto using account id
    @GetMapping("/customer/{id}")
    public ResponseEntity<CustomerDto> getCustomerByAccountID(@PathVariable int id) {
        Customer customer = customerService.getCustomerByID(id);
        return new ResponseEntity<CustomerDto>(new CustomerDto(customer), HttpStatus.OK);
    }

    // get donations of a customer
    @GetMapping("/customer/{id}/donations")
    public ResponseEntity<List<DonationDto>> getCustomerDonations(@PathVariable int id) {
        Customer customer = customerService.getCustomerByID(id);
        CustomerDto customerDto = new CustomerDto(customer);

        List<DonationDto> donationDtos = customerDto.getCustomerDonatedArtifacts();

        return new ResponseEntity<List<DonationDto>>(donationDtos, HttpStatus.OK);
    }

    // get loans of a customer
    @GetMapping("/customer/{id}/loans")
    public ResponseEntity<List<Integer>> getCustomerLoans(@PathVariable int id) {
        Customer customer = customerService.getCustomerByID(id);
        List<Integer> loanIDList = new ArrayList<>();
        for (Loan loan : customer.getLoans()) {
            loanIDList.add(loan.getRequestID());
        }
        return new ResponseEntity<List<Integer>>(loanIDList, HttpStatus.OK);
    }

    // add a created loan for a customer
    @GetMapping("/customer/{id}/loans/add")
    public ResponseEntity<List<Integer>> addLoanToCustomer(@PathVariable int id, @RequestParam int loanID) {
        Customer customer = customerService.addLoanToCustomer(id, loanID);
        List<Integer> loanIDList = new ArrayList<>();
        for (Loan loan : customer.getLoans()) {
            loanIDList.add(loan.getRequestID());
        }
        return new ResponseEntity<List<Integer>>(loanIDList, HttpStatus.OK);
    }

    // set when a loan for customer is approved, increase credit of a customer
    @GetMapping("/customer/{id}/loans/approve")
    public ResponseEntity<CustomerDto> approveLoanOfCustomer(@PathVariable int id, @RequestParam int loanID) {
        Customer customer = customerService.approveLoanOfCustomer(id, loanID);
        return new ResponseEntity<CustomerDto>(new CustomerDto(customer), HttpStatus.OK);
    }

    // delete a loan from customer when loan is rejected or returned
    @GetMapping("/customer/{id}/loans/delete")
    public ResponseEntity<List<Integer>> deleteLoanFromCustomer(@PathVariable int id, @RequestParam int loanID) {
        Customer customer = customerService.deleteLoanFromCustomer(id, loanID);
        List<Integer> loanIDList = new ArrayList<>();
        for (Loan loan : customer.getLoans()) {
            loanIDList.add(loan.getRequestID());
        }
        return new ResponseEntity<List<Integer>>(loanIDList, HttpStatus.OK);
    }

    // get all tickets of a customer
    @GetMapping("/customer/{id}/tickets")
    public ResponseEntity<List<TicketDto>> getCustomerTickets(@PathVariable int id) {
        Customer customer = customerService.getCustomerByID(id);
        CustomerDto customerDto = new CustomerDto(customer);

        List<TicketDto> ticketDtos = customerDto.getCustomerTickets();

        return new ResponseEntity<List<TicketDto>>(ticketDtos, HttpStatus.OK);
    }

    // update information for customer of tickets and donations
    @PostMapping("/customer/{id}/update")
    public ResponseEntity<CustomerDto> updatedCustomer(@PathVariable int id, @RequestBody CustomerDto request) {
        Customer updatedCustomer = customerService.modifyCustomerByID(id, request);
        CustomerDto response = new CustomerDto(updatedCustomer);
        return new ResponseEntity<CustomerDto>(response, HttpStatus.OK);
    }

    // delete a customer
    @GetMapping("/customer/{id}/delete")
    public ResponseEntity<String> deleteCustomer(@PathVariable int id) {
        customerService.deleteCustomerByID(id);
        return new ResponseEntity<String>("Customer deleted successfully.", HttpStatus.OK);
    }
}
