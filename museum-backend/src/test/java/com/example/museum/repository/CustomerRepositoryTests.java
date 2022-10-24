package com.example.museum.repository;

import com.example.museum.model.*;

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

        boolean loanable2 = false;
        Artifact.ArtType artType2 = Artifact.ArtType.Painting;
        String artName2 = "Portrait of Dr. Gachet";
        Artifact artifact2 = new Artifact();
        artifact2.setLoanable(loanable2);
        artifact2.setName(artName2);
        artifact2.setType(artType2);

        artifact2 = artifactRepository.save(artifact2);
        int artID2 = artifact2.getArtID();

        // Donation
        Donation donation = new Donation();
        donation.setNewDonationArtifactsList();
        donation.addDonatedArtifact(artifact);
        donation = donationRepository.save(donation);
        int donationID = donation.getDonationID();

        // Loan Request
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setNewRequestedArtifactsList();
        loanRequest.addRequestedArtifact(artifact2);
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

        customer.addLoanedArtifact(loanedArtifact);
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

        assertEquals(loanedArtifactID, customer.getLoanedArtifact(0).getArtID());

        assertEquals(loanRequestID, customer.getLoanRequest().getRequestID());

        assertEquals(ticketID, customer.getCustomerTicket(0).getTicketID());
    }
}
