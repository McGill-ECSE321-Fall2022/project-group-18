package com.example.museum.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.example.museum.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.exceptions.RequestException;
import com.example.museum.repository.ArtifactRepository;
import com.example.museum.repository.CustomerRepository;
import com.example.museum.repository.DonationRepository;
import com.example.museum.repository.EmployeeRepository;
import com.example.museum.repository.LoanRepository;
import com.example.museum.repository.OwnerRepository;
import com.example.museum.repository.TicketRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {
    @Mock
    CustomerRepository customerRepository;
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    OwnerRepository ownerRepository;
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
    EmployeeService employeeService;
    @InjectMocks
    OwnerService ownerService;
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
        testDonation.addDonatedArtifact(testArtifact1);

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
        testCustomer.setCustomerTicketsList();
        testCustomer.setCustomerDonatedArtifactsList();
        testCustomer.setLoansList();

        testCustomer.addCustomerDonatedArtifact(testDonation);
        testCustomer.addCustomerTicket(testTicket);
        testCustomer.addLoan(testLoan);

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

    @Test
    void testCreateCustomer() {
        when(customerRepository.save(any(Customer.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
        final int customerID = 1;
        final String username = "customer1";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "Last";
        final int credit = 5;
        final Customer testCustomer = new Customer(customerID, username, password, firstName, lastName, credit);

        Customer returnedCustomer = customerService.createCustomer(testCustomer);

        // Testing customer
        assertNotNull(returnedCustomer);
        assertEquals(customerID, returnedCustomer.getAccountID());
        assertEquals(username, returnedCustomer.getUsername());
        assertEquals(password, returnedCustomer.getPassword());
        assertEquals(firstName, returnedCustomer.getFirstName());
        assertEquals(lastName, returnedCustomer.getLastName());
    }

    @Test
    void testCreateConflictingUsername() {
        final int customerID = 1;
        final String username = "customer1";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "User";
        final int credit = 5;
        final Customer testCustomer = new Customer(customerID, username, password, firstName, lastName, credit);
        List<Customer> customers = new ArrayList<>();
        customers.add(testCustomer);

        when(customerRepository.findAll())
                .thenAnswer((InvocationOnMock invocation) -> customers);

        final int customerID2 = 2;
        final String username2 = "customer1";
        final String password2 = "password";
        final String firstName2 = "Second";
        final String lastName2 = "User";
        final int credit2 = 10;
        final Customer testCustomer2 = new Customer(customerID2, username2, password2, firstName2, lastName2, credit2);

        Exception ex = assertThrows(DatabaseException.class, () -> customerService.createCustomer(testCustomer2));
        verify(customerRepository, times(0)).save(testCustomer2);
    }

    @Test
    void testInvalidLoginCustomer() {
        final int customerID = 1;
        final String username = "customer1";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "Last";
        final int credit = 5;
        final Customer testCustomer = new Customer(customerID, username, password, firstName, lastName, credit);

        Exception ex = assertThrows(DatabaseException.class, () -> customerService.loginCustomer(testCustomer));
    }

    @Test
    void testCustomerLogin(){
        final int customerID = 1;
        final String username = "customer";
        final String password = "password";
        final String firstName = "First";
        final String lastName = "Last";
        final int credit = 10;
        final Customer testCustomer = new Customer(customerID, username, password, firstName, lastName, credit);
        final Customer storedCustomer = new Customer(customerID, username, password, firstName, lastName, credit);
        List<Customer> customers = new ArrayList<>();
        customers.add(storedCustomer);

        when(customerRepository.findAll()).thenAnswer((InvocationOnMock) -> customers);

        assertDoesNotThrow(() -> customerService.loginCustomer(testCustomer));
    }
}
