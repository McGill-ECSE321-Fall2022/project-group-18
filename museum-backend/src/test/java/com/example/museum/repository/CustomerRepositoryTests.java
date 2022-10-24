package com.example.museum.repository;

import com.example.museum.model.Customer;
import com.example.museum.model.Donation;
import com.example.museum.model.Ticket;
import com.example.museum.model.LoanRequest;
import com.example.museum.model.LoanedArtifact;
import com.example.museum.model.Artifact;

import java.sql.Date;

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
    private LoanedArtifactRepository loanedArtifactRepository;
    @Autowired
    private ArtifactRepository artifactRepository;
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private LoanRequestRepository loanRequestRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @AfterEach
    public void clearDatabase() {
        // customer->donation->artifact
        // customer->loanRequest->loanedArtifact
        // customer->loanedArtifact === should not be set if this loaned Artifact does not belong to customer yet
        // customer->loanedArtifact2 === this is ok as loanedArtifact logically cannot be loanedArtifact2
        // customer->ticket
        customerRepository.deleteAll();
        donationRepository.deleteAll();
        loanRequestRepository.deleteAll();
        loanedArtifactRepository.deleteAll();
        artifactRepository.deleteAll();
        ticketRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadCustomer() {
        // LoanedArtifact
        int loanFee = 1000;
        LoanedArtifact loanedArtifact = new LoanedArtifact();
        loanedArtifact.setLoanFee(loanFee);

        loanedArtifact = loanedArtifactRepository.save(loanedArtifact);
        int loanedArtifactID = loanedArtifact.getArtID();

        int loanFee2 = 2000;
        LoanedArtifact loanedArtifact2 = new LoanedArtifact();
        loanedArtifact2.setLoanFee(loanFee2);

        loanedArtifact2 = loanedArtifactRepository.save(loanedArtifact2);
        int loanedArtifactID2 = loanedArtifact2.getArtID();

        // Artifact
        boolean loanable = true;
        String name = "Mona Lisa";
        Artifact.ArtType artType = Artifact.ArtType.Painting;
        Artifact artifact = new Artifact();
        artifact.setLoanable(loanable);
        artifact.setName(name);
        artifact.setType(artType);
        artifact = artifactRepository.save(artifact);
        int artID1 = artifact.getArtID();

        // Donation
        Donation donation = new Donation();
        donation.setNewDonationArtifactsList();
        donation.addDonatedArtifact(artifact);
        donation = donationRepository.save(donation);
        int donationID = donation.getDonationID();

        // Loan Request
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setNewRequestedArtifactsList();
        loanRequest.addRequestedArtifact(loanedArtifact);
        loanRequest = loanRequestRepository.save(loanRequest);
        int loanRequestID = loanRequest.getRequestID();

        // Ticket
        Date day = new Date(2022, 10, 13);
        int price = 10;
        Ticket ticket = new Ticket();
        ticket.setDay(day);
        ticket.setPrice(price);
        ticket = ticketRepository.save(ticket);
        int ticketID = ticket.getTicketID();

        String customerUsername = "Customer";
        String customerPassword = "password";

        Customer customer = new Customer();
        customer.setUsername(customerUsername);
        customer.setPassword(customerPassword);

        customer.setCustomerTicketsList();
        customer.setCustomerDonatedArtifactsList();
        customer.setLoanedArtifactsList();

        customer.setLoanRequest(loanRequest);

        customer.addLoanedArtifact(loanedArtifact2);
        customer.addCustomerDonatedArtifact(donation);
        customer.addCustomerTicket(ticket);

        customer = customerRepository.save(customer);
        int customerId = customer.getAccountID();

        customer = null;
        ticket = null;
        loanRequest = null;
        donation = null;
        artifact = null;
        loanedArtifact = null;

        customer = customerRepository.findByAccountID(customerId);

        assertNotNull(customer);
        assertEquals(customerId, customer.getAccountID());
        assertEquals(customerUsername, customer.getUsername());
        assertEquals(customerPassword, customer.getPassword());

        assertEquals(donationID, customer.getCustomerDonatedArtifact(0).getDonationID());

        assertEquals(loanedArtifactID2, customer.getLoanedArtifact(0).getArtID());

        assertEquals(loanRequestID, customer.getLoanRequest().getRequestID());
        assertEquals(loanedArtifactID, customer.getLoanRequest().getRequestedArtifact(0).getArtID());

        assertEquals(ticketID, customer.getCustomerTicket(0).getTicketID());
    }
}
