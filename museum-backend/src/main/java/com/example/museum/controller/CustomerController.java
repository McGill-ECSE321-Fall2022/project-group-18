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

    @PostMapping("/customer/login")
    public ResponseEntity<String> loginCustomer(@RequestBody CustomerDto request) {
        Customer customerToLogin = request.toModel();
        customerService.loginCustomer(customerToLogin);
        return new ResponseEntity<String>("Successful login", HttpStatus.OK);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<CustomerDto> getCustomerByAccountID(@PathVariable int id) {
        Customer customer = customerService.getCustomerByID(id);
        return new ResponseEntity<CustomerDto>(new CustomerDto(customer), HttpStatus.OK);
    }

    @GetMapping("/customer/{id}/donations")
    public ResponseEntity<List<DonationDto>> getCustomerDonations(@PathVariable int id) {
        Customer customer = customerService.getCustomerByID(id);
        CustomerDto customerDto = new CustomerDto(customer);

        List<DonationDto> donationDtos = customerDto.getCustomerDonatedArtifacts();

        return new ResponseEntity<List<DonationDto>>(donationDtos, HttpStatus.OK);
    }

    @GetMapping("/customer/{id}/loans")
    public ResponseEntity<List<Integer>> getCustomerLoans(@PathVariable int id) {
        Customer customer = customerService.getCustomerByID(id);
        List<Integer> loanIDList= new ArrayList<>();
        for (Loan loan: customer.getLoans()) {
            loanIDList.add(loan.getRequestID());
        }
        return new ResponseEntity<List<Integer>>(loanIDList, HttpStatus.OK);
    }

    @GetMapping("/customer/{id}/loans/add")
    public ResponseEntity<List<Integer>> addLoanToCustomer(@PathVariable int id, @RequestParam int loanID) {
        Customer customer = customerService.addLoanToCustomer(id, loanID);
        List<Integer> loanIDList = new ArrayList<>();
        for (Loan loan: customer.getLoans()) {
            loanIDList.add(loan.getRequestID());
        }
        return new ResponseEntity<List<Integer>>(loanIDList, HttpStatus.OK);
    }

    @GetMapping("/customer/{id}/loans/delete")
    public ResponseEntity<List<Integer>> deleteLoanFromCustomer(@PathVariable int id, @RequestParam int loanID) {
        Customer customer = customerService.deleteLoanFromCustomer(id, loanID);
        List<Integer> loanIDList = new ArrayList<>();
        for (Loan loan: customer.getLoans()) {
            loanIDList.add(loan.getRequestID());
        }
        return new ResponseEntity<List<Integer>>(loanIDList, HttpStatus.OK);
    }

    @GetMapping("/customer/{id}/tickets")
    public ResponseEntity<List<TicketDto>> getCustomerTickets(@PathVariable int id) {
        Customer customer = customerService.getCustomerByID(id);
        CustomerDto customerDto = new CustomerDto(customer);

        List<TicketDto> ticketDtos = customerDto.getCustomerTickets();

        return new ResponseEntity<List<TicketDto>>(ticketDtos, HttpStatus.OK);
    }

    @PostMapping("/customer/{id}")
    public ResponseEntity<CustomerDto> updatedCustomer(@PathVariable int id, @RequestBody CustomerDto request) {
        Customer updatedCustomer = customerService.modifyCustomerByID(id, request.getUsername(), request.getPassword(),
                request.getFirstName(), request.getLastName());
        CustomerDto response = new CustomerDto(updatedCustomer);
        return new ResponseEntity<CustomerDto>(response, HttpStatus.OK);
    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable int id) {
        customerService.deleteCustomerByID(id);
        return new ResponseEntity<String>("Customer deleted successfully.", HttpStatus.OK);
    }
}
