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
    private ArtifactRepository artifactRepository;
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private LoanRepository loanRepository;
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
        loanRepository.deleteAll();
        artifactRepository.deleteAll();
        ticketRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadCustomer() {
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

        // Loan
        Loan loan = new Loan();
        loan.setNewrequestedArtifactsList();
        loan.addRequestedArtifact(artifact2);
        loan = loanRepository.save(loan);
        int loanID = loan.getRequestID();

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
        customer.setLoansList();

        customer.addCustomerDonatedArtifact(donation);
        customer.addCustomerTicket(ticket);
        customer.addLoan(loan);

        customer = customerRepository.save(customer);
        int customerId = customer.getAccountID();

        customer = null;
        ticket = null;
        loan = null;
        donation = null;
        artifact = null;

        customer = customerRepository.findByAccountID(customerId);

        assertNotNull(customer);
        assertEquals(customerId, customer.getAccountID());
        assertEquals(customerUsername, customer.getUsername());
        assertEquals(customerPassword, customer.getPassword());

        assertEquals(donationID, customer.getCustomerDonatedArtifact(0).getDonationID());

        assertEquals(loanID, customer.getLoan(0).getRequestID());

        assertEquals(ticketID, customer.getCustomerTicket(0).getTicketID());
    }
}
