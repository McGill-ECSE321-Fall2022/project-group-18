package com.example.museum.dto;

import com.example.museum.model.Customer;
import com.example.museum.model.Donation;
import com.example.museum.model.Loan;
import com.example.museum.model.Ticket;

import java.util.*;

public class CustomerDto {
    private int accountID;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private List<DonationDto> customerDonatedArtifacts;
    private List<LoanDto> loans;
    private List<TicketDto> customerTickets;

    public CustomerDto(Customer customer) {
        this.accountID = customer.getAccountID();
        this.username = customer.getUsername();
        this.password = customer.getPassword();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.customerDonatedArtifacts = new ArrayList<>();

        for (Donation don : customer.getCustomerDonatedArtifacts()) {
            this.customerDonatedArtifacts.add(new DonationDto(don));
        }

        this.loans = new ArrayList<>();

        for (Loan lo : customer.getLoans()) {
            this.loans.add(new LoanDto(lo));
        }

        this.customerTickets = new ArrayList<>();

        for (Ticket ti : customer.getCustomerTickets()) {
            this.customerTickets.add(new TicketDto(ti));
        }
    }

    public CustomerDto() {
    }

    public int getAccountID() {
        return this.accountID;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public List<DonationDto> getCustomerDonatedArtifact() {
        return this.customerDonatedArtifacts;
    }

    public List<LoanDto> getLoans() {
        return this.loans;
    }

    public List<TicketDto> getCustomerTickets() {
        return this.customerTickets;
    }

    public Customer toModel() {
        Customer customer = new Customer();
        customer.setAccountID(this.accountID);
        customer.setUsername(this.username);
        customer.setPassword(this.password);
        for (DonationDto don : this.getCustomerDonatedArtifact()) {
            customer.addCustomerDonatedArtifact(don.toModel());
        }
        for (LoanDto lo : this.getLoans()) {
            customer.addLoan(lo.toModel());
        }
        for (TicketDto ti : this.getCustomerTickets()) {
            customer.addCustomerTicket(ti.toModel());
        }

        return customer;
    }
}
