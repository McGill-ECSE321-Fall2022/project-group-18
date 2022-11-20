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

import javax.transaction.Transactional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private LoanRepository loanRepository;


    // find customer using id
    @Transactional
    public Customer getCustomerByID(int id) {
        Customer customer = customerRepository.findByAccountID(id);
        if (customer == null) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        return customer;
    }

    // verify if the input account information matches the one in database
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

    // create a non-duplicated customer
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

    // update the customer info using id to identiy
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

    // delete a customer account by id
    @Transactional
    public void deleteCustomerByID(int id) {
        Customer customer = customerRepository.findByAccountID(id);
        if (customer == null) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        customerRepository.deleteById(id);
    }

    // a convenient method to check if customer and loan exists
    private Customer checkLoanAndCustomer(int customerID, int loanID) {
        Customer customer = customerRepository.findByAccountID(customerID);
        if (customer == null) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Customer not found");
        }
        if (!loanRepository.existsById(loanID)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Loan not found");
        }
        return customer;
    }

    // add a created loan to the customer
    @Transactional
    public Customer addLoanToCustomer(int customerID, int loanID) {
        Customer customer = checkLoanAndCustomer(customerID, loanID);
        Loan loan = loanRepository.findLoanRequestByRequestID(loanID);
        customer.addLoan(loan);
        customer = customerRepository.save(customer);
        return customer;
    }

    // approve a loan of a customer by add the customer credit
    @Transactional
    public Customer approveLoanOfCustomer(int customerID, int loanID) {
        Customer customer = checkLoanAndCustomer(customerID, loanID);
        Loan loan = loanRepository.findLoanRequestByRequestID(loanID);
        if (!customer.getLoans().contains(loan)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "This loan does not belong to this customer");
        }
        int customerCredit = customer.getCredit();
        customerCredit += loan.getLoanFee();
        customer.setCredit(customerCredit);
        customer = customerRepository.save(customer);
        return customer;
    }

    // delete a loan's association from customer by removing it from customer's loan list
    @Transactional
    public Customer deleteLoanFromCustomer(int customerID, int loanID) {
        Customer customer = checkLoanAndCustomer(customerID, loanID);
        Loan loan = loanRepository.findLoanRequestByRequestID(loanID);
        if (!customer.getLoans().contains(loan)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "This loan does not belong to this customer");
        }
        customer.removeLoan(loan);
        customer = customerRepository.save(customer);
        return customer;
    }
}
