package com.example.museum.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Artifact;
import com.example.museum.model.Customer;
import com.example.museum.model.Donation;
import com.example.museum.model.Loan;
import com.example.museum.model.Ticket;
import com.example.museum.repository.ArtifactRepository;
import com.example.museum.repository.CustomerRepository;
import com.example.museum.repository.DonationRepository;
import com.example.museum.repository.LoanRepository;
import com.example.museum.repository.TicketRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {
    @Mock
    CustomerRepository customerRepository;
    @Mock
    ArtifactRepository artifactRepository;
    @Mock
    DonationRepository donationRepository;
    @Mock
    LoanRepository loanRepository;
    @Mock
    TicketRepository ticketRepository;

    @InjectMocks
    CustomerService customerService;
    @InjectMocks
    ArtifactService artifactService;
    @InjectMocks
    DonationService donationService;
    @InjectMocks
    LoanService loanService;
    @InjectMocks
    TicketService ticketService;

    @Test
    public void testGetCustomerID() {
        // Artifact
        final int art1ID = 1;
        final boolean loaned1 = true;
        final boolean loanable1 = true;
        final int loanFee1 = 10;
        final String name1 = "Mona Lisa";
        final Artifact.ArtType artType1 = Artifact.ArtType.Painting;
        final Artifact testArtifact1 = new Artifact(art1ID, name1, artType1, loanable1, loaned1, loanFee1);

        final int art2ID = 2;
        final boolean loaned2 = true;
        final boolean loanable2 = true;
        final int loanFee2 = 15;
        final String name2 = "Portrait of Dr. Gachet";
        final Artifact.ArtType artType2 = Artifact.ArtType.Painting;
        final Artifact testArtifact2 = new Artifact(art2ID, name2, artType2, loanable2, loaned2, loanFee2);

        // Donation
        final int donationId = 3;
        final Donation testDonation = new Donation(art2ID);
        testDonation.addDonatedArtifact(testArtifact2);

        // Loan
        final int loanId = 4;
        final int loanFee = 10;
        final boolean approved = false;
        final Loan testLoan = new Loan();
        testLoan.setRequestID(loanId);
        testLoan.setNewrequestedArtifactsList();
        testLoan.addRequestedArtifact(testArtifact2);
        testLoan.setLoanFee(loanFee);
        testLoan.setApproved(approved);

        // Ticket
        final int ticketId = 5;
        final Date day = new Date(2022, 10, 13);
        final int price = 10;
        final Ticket testTicket = new Ticket(ticketId, day, price);

        // Customer
        final int customerID = 6;
        final String username = "customer1";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "Last";
        final int credit = 5;
        final Customer testCustomer = new Customer(customerID, username, password, firstName, lastName, credit);

        when(customerRepository.findByAccountID(customerID)).thenAnswer((InvocationOnMock invocation) -> testCustomer);

        Customer customer = customerService.getCustomerByID(customerID);

        // Testing customer
        assertNotNull(customer);
        assertEquals(customerID, customer.getAccountID());
        assertEquals(username, customer.getUsername());
        assertEquals(password, customer.getPassword());
        assertEquals(firstName, customer.getFirstName());
        assertEquals(lastName, customer.getLastName());
        // Testing associations
        assertEquals(customer.getCustomerDonatedArtifact(0).getDonationID(),
                testCustomer.getCustomerDonatedArtifact(0).getDonationID());
        assertEquals(customer.getLoan(0).getRequestID(), testCustomer.getLoan(0).getRequestID());
        assertEquals(customer.getLoan(0).getRequestedArtifact(0).getArtID(),
                testCustomer.getLoan(0).getRequestedArtifact(0).getArtID());
        assertEquals(customer.getCustomerTicket(0).getTicketID(), testCustomer.getCustomerTicket(0).getTicketID());
    }

    @Test
    void testGetCustomerByInvalidID() {
        final int invalidID = 11111;

        when(customerRepository.findByAccountID(invalidID)).thenAnswer((InvocationOnMock) -> null);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> customerService.getCustomerByID(invalidID));

        assertEquals("Customer not found", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }
}
