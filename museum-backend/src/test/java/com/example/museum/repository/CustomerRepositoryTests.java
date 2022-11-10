package com.example.museum.repository;

import com.example.museum.model.*;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Test of CustomerRepo
// Springboot testing setup
@SpringBootTest
public class CustomerRepositoryTests {
    // Injecting the customerRepository
    @Autowired
    private CustomerRepository customerRepository;
    // Injecting the artifactRepository
    @Autowired
    private ArtifactRepository artifactRepository;
    // Injecting the donationRepository
    @Autowired
    private DonationRepository donationRepository;
    // Injecting hte loanRepository
    @Autowired
    private LoanRepository loanRepository;
    // Injecting the ticketRepository
    @Autowired
    private TicketRepository ticketRepository;

    // Clear the persistence data in database after each test
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

    // The test method - read and write the object, its reference and its attributes
    @Test
    public void testPersistAndLoadCustomer() {
        // Artifact
        boolean loaned = true;
        boolean loanable = true;
        String name = "Mona Lisa";
        Artifact.ArtType artType = Artifact.ArtType.Painting;
        // Create an artifact object with some attributes
        Artifact artifact = new Artifact();
        artifact.setLoanable(loanable);
        artifact.setName(name);
        artifact.setType(artType);
        artifact.setLoaned(loaned);
        // Save the artifact in its database, tests its writing
        artifact = artifactRepository.save(artifact);
        // Keep track with its automatically generated ID
        int artID1 = artifact.getArtID();

        boolean loaned2 = false;
        boolean loanable2 = false;
        Artifact.ArtType artType2 = Artifact.ArtType.Painting;
        String artName2 = "Portrait of Dr. Gachet";
        // Create an artifact object with some attributes
        Artifact artifact2 = new Artifact();
        artifact2.setLoanable(loanable2);
        artifact2.setName(artName2);
        artifact2.setType(artType2);
        artifact2.setLoaned(loaned2);
        // Save the artifact in its database, tests its writing
        artifact2 = artifactRepository.save(artifact2);
        // Keep track with its automatically generated ID
        int artID2 = artifact2.getArtID();

        // Donation
        // Create a new donation object with some attributes (associations with artifacts)
        Donation donation = new Donation();
        donation.setNewDonationArtifactsList();
        donation.addDonatedArtifact(artifact);
        // Save the donation in its database, tests its writing
        donation = donationRepository.save(donation);
        // Keep track with its automatically generated ID
        int donationID = donation.getDonationID();

        // Loan
        // Create a loan object with some attributes
        Loan loan = new Loan();
        loan.setNewrequestedArtifactsList();
        loan.addRequestedArtifact(artifact2);
        // Save the loan in its database, tests its writing
        loan = loanRepository.save(loan);
        // Keep track with its automatically generated ID
        int loanID = loan.getRequestID();

        // Ticket
        Date day = new Date(2022, 10, 13);
        int price = 10;
        // Create a ticket object with some attributes
        Ticket ticket = new Ticket();
        ticket.setDay(day);
        ticket.setPrice(price);
        // Save the ticket in its database, tests its writing
        ticket = ticketRepository.save(ticket);
        // Keep track with its automatically generated ID
        int ticketID = ticket.getTicketID();

        String customerUsername = "Customer";
        String customerPassword = "password";
        String firstName = "Bob";
        String lastName = "Vance";
        int credit = 69;
        // Create a customer object with its attributes and associations
        Customer customer = new Customer();
        customer.setUsername(customerUsername);
        customer.setPassword(customerPassword);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setCredit(credit);

        customer.setCustomerTicketsList();
        customer.setCustomerDonatedArtifactsList();
        customer.setLoansList();

        customer.addCustomerDonatedArtifact(donation);
        customer.addCustomerTicket(ticket);
        customer.addLoan(loan);
        // Save the customer in its databse, tests its writing
        customer = customerRepository.save(customer);
        // Keep traci with its automatically generated ID
        int customerId = customer.getAccountID();

        customer = null;
        ticket = null;
        loan = null;
        donation = null;
        artifact = null;
        // Read from the database using the id, test its reading
        customer = customerRepository.findByAccountID(customerId);
        // Test if an object is returned
        assertNotNull(customer);
        // Test if the attributes read from DB is correct
        assertEquals(customerId, customer.getAccountID());
        assertEquals(customerUsername, customer.getUsername());
        assertEquals(customerPassword, customer.getPassword());
        assertEquals(firstName, customer.getFirstName());
        assertEquals(lastName, customer.getLastName());
        assertEquals(credit, customer.getCredit());
        // Test if the associations are correct
        assertEquals(donationID, customer.getCustomerDonatedArtifact(0).getDonationID());
        assertEquals(artID1, customer.getCustomerDonatedArtifact(0).getDonatedArtifact(0).getArtID());
        assertEquals(loanID, customer.getLoan(0).getRequestID());
        assertEquals(artID2, customer.getLoan(0).getRequestedArtifact(0).getArtID());
        assertEquals(ticketID, customer.getCustomerTicket(0).getTicketID());
    }
}
