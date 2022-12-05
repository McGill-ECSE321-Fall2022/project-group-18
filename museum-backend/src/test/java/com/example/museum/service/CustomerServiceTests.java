package com.example.museum.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.example.museum.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.example.museum.dto.CustomerDto;
import com.example.museum.exceptions.DatabaseException;
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

                when(customerRepository.findByAccountID(customerID))
                                .thenAnswer((InvocationOnMock invocation) -> testCustomer);

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
                assertEquals(customer.getCustomerTicket(0).getTicketID(),
                                testCustomer.getCustomerTicket(0).getTicketID());
        }

        @Test
        void testGetCustomerByInvalidID() {
                final int invalidID = 11111;

                when(customerRepository.findByAccountID(invalidID)).thenAnswer((InvocationOnMock) -> null);

                DatabaseException ex = assertThrows(DatabaseException.class,
                                () -> customerService.getCustomerByID(invalidID));

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
    void testCreateAndModifyCustomer(){
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
        when(customerRepository.findByAccountID(customerID)).thenAnswer((InvocationOnMock invocation) -> returnedCustomer);
        CustomerDto customerDto = new CustomerDto(new Customer(customerID, "newCustomerName", "123456", "First2", "Last2", credit));
        Customer updatedCustomer = customerService.modifyCustomerByID(customerID, customerDto);
        assertNotNull(updatedCustomer);
        assertEquals("newCustomerName", updatedCustomer.getUsername());
        assertEquals("123456", updatedCustomer.getPassword());
        assertEquals("First2", updatedCustomer.getFirstName());
        assertEquals("Last2", updatedCustomer.getLastName());
    }

    @Test
    void testCreateAndModifyCustomerWithInvalidID(){
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
        when(customerRepository.findByAccountID(customerID)).thenAnswer((InvocationOnMock invocation) -> null);
        CustomerDto customerDto = new CustomerDto(new Customer(customerID, "newCustomerName", "123456", "First2", "Last2", credit));
        DatabaseException ex = assertThrows(DatabaseException.class, () -> customerService.modifyCustomerByID(customerID, customerDto));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Customer not found", ex.getMessage());
    }

    @Test
    void testCreateAndDeleteCustomer() {
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
        when(customerRepository.findByAccountID(customerID)).thenAnswer((InvocationOnMock invocation) -> returnedCustomer);
        customerService.deleteCustomerByID(returnedCustomer.getAccountID());
        assertEquals(0, customerRepository.count());
        assertFalse(customerRepository.existsById(returnedCustomer.getAccountID()));
    }

    @Test
    void testCreateAndDeleteCustomerWithInvalidID() {
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
        when(customerRepository.findByAccountID(customerID)).thenAnswer((InvocationOnMock invocation) -> null);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> customerService.deleteCustomerByID(returnedCustomer.getAccountID()));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Customer not found", ex.getMessage());
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
                final Customer testCustomer2 = new Customer(customerID2, username2, password2, firstName2, lastName2,
                                credit2);

                Exception ex = assertThrows(DatabaseException.class,
                                () -> customerService.createCustomer(testCustomer2));
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
        void testCustomerLogin() {
                final int customerID = 1;
                final String username = "customer";
                final String password = "password";
                final String firstName = "First";
                final String lastName = "Last";
                final int credit = 10;
                final Customer testCustomer = new Customer(customerID, username, password, firstName, lastName, credit);
                final Customer storedCustomer = new Customer(customerID, username, password, firstName, lastName,
                                credit);
                List<Customer> customers = new ArrayList<>();
                customers.add(storedCustomer);

                when(customerRepository.findAll()).thenAnswer((InvocationOnMock) -> customers);

                assertDoesNotThrow(() -> customerService.loginCustomer(testCustomer));
        }

    @Test
    void testAddLoanToCustomer() {
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
        when(customerRepository.findByAccountID(customerID)).thenAnswer((InvocationOnMock invocation) -> returnedCustomer);

        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
//        List<Artifact> testArtifactList = new ArrayList<>();
//        testArtifactList.add(testArtifact1);
//        testArtifactList.add(testArtifact2);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int requestID = 3;
        final int loanFee = 123;
        final boolean approval = false;
        final Loan testLoan = new Loan();
        testLoan.setRequestID(requestID);
        testLoan.setLoanFee(loanFee);
        testLoan.setApproved(approval);
        testLoan.setNewrequestedArtifactsList();
        testLoan.addRequestedArtifact(testArtifact1);
        testLoan.addRequestedArtifact(testArtifact2);

        when(loanRepository.findLoanRequestByRequestID(requestID)).thenAnswer((InvocationOnMock invocation) -> testLoan);
        when(loanRepository.existsById(requestID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(loanService.getLoanByID(requestID)).thenAnswer((InvocationOnMock invocation) -> testLoan);

        Customer customer = customerService.addLoanToCustomer(returnedCustomer.getAccountID(), testLoan.getRequestID());
        assertNotNull(customer);
        assertEquals(1, customer.getLoans().size());
        assertEquals(requestID, customer.getLoan(0).getRequestID());
        assertEquals(loanFee, customer.getLoan(0).getLoanFee());
        assertEquals(approval, customer.getLoan(0).getApproved());
        assertEquals(testLoan.getRequestedArtifacts().size(), customer.getLoan(0).getRequestedArtifacts().size());
    }

    @Test
    void testAddInvalidLoanToCustomer() {
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

        when(customerRepository.findByAccountID(customerID)).thenAnswer((InvocationOnMock invocation) -> returnedCustomer);

        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
//        List<Artifact> testArtifactList = new ArrayList<>();
//        testArtifactList.add(testArtifact1);
//        testArtifactList.add(testArtifact2);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int requestID = 3;
        final int loanFee = 123;
        final boolean approval = false;
        final Loan testLoan = new Loan();
        testLoan.setRequestID(requestID);
        testLoan.setLoanFee(loanFee);
        testLoan.setApproved(approval);
        testLoan.setNewrequestedArtifactsList();
        testLoan.addRequestedArtifact(testArtifact1);
        testLoan.addRequestedArtifact(testArtifact2);

        when(loanRepository.existsById(requestID)).thenAnswer((InvocationOnMock invocation) -> false);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> customerService.addLoanToCustomer(returnedCustomer.getAccountID(), requestID));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Loan not found", ex.getMessage());
    }

    @Test
    void testRemoveLoanFromCustomer() {
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
        when(customerRepository.findByAccountID(customerID)).thenAnswer((InvocationOnMock invocation) -> returnedCustomer);

        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
//        List<Artifact> testArtifactList = new ArrayList<>();
//        testArtifactList.add(testArtifact1);
//        testArtifactList.add(testArtifact2);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int requestID = 3;
        final int loanFee = 123;
        final boolean approval = false;
        final Loan testLoan = new Loan();
        testLoan.setRequestID(requestID);
        testLoan.setLoanFee(loanFee);
        testLoan.setApproved(approval);
        testLoan.setNewrequestedArtifactsList();
        testLoan.addRequestedArtifact(testArtifact1);
        testLoan.addRequestedArtifact(testArtifact2);

        when(loanRepository.findLoanRequestByRequestID(requestID)).thenAnswer((InvocationOnMock invocation) -> testLoan);
        when(loanRepository.existsById(requestID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(loanService.getLoanByID(requestID)).thenAnswer((InvocationOnMock invocation) -> testLoan);

        Customer customer = customerService.addLoanToCustomer(returnedCustomer.getAccountID(), testLoan.getRequestID());
        Customer removeLoancustomer = customerService.deleteLoanFromCustomer(returnedCustomer.getAccountID(), testLoan.getRequestID());

        assertNotNull(removeLoancustomer);
        assertEquals(0, customer.getLoans().size());
        assertTrue(loanRepository.existsById(requestID));
    }

    @Test
    void testRemoveInvalidLoanFromCustomer() {
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
        when(customerRepository.findByAccountID(customerID)).thenAnswer((InvocationOnMock invocation) -> returnedCustomer);

        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
//        List<Artifact> testArtifactList = new ArrayList<>();
//        testArtifactList.add(testArtifact1);
//        testArtifactList.add(testArtifact2);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int requestID = 3;
        final int loanFee = 123;
        final boolean approval = false;
        final Loan testLoan = new Loan();
        testLoan.setRequestID(requestID);
        testLoan.setLoanFee(loanFee);
        testLoan.setApproved(approval);
        testLoan.setNewrequestedArtifactsList();
        testLoan.addRequestedArtifact(testArtifact1);
        testLoan.addRequestedArtifact(testArtifact2);

        when(loanRepository.findLoanRequestByRequestID(requestID)).thenAnswer((InvocationOnMock invocation) -> testLoan);
        when(loanRepository.existsById(requestID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(loanService.getLoanByID(requestID)).thenAnswer((InvocationOnMock invocation) -> testLoan);

        Customer customer = customerService.addLoanToCustomer(returnedCustomer.getAccountID(), testLoan.getRequestID());
        when(loanRepository.existsById(requestID)).thenAnswer((InvocationOnMock invocation) -> false);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> customerService.deleteLoanFromCustomer(returnedCustomer.getAccountID(), testLoan.getRequestID()));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Loan not found", ex.getMessage());
    }

    @Test
    void testApproveLoansOfCustomer() {
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
        when(customerRepository.findByAccountID(customerID)).thenAnswer((InvocationOnMock invocation) -> returnedCustomer);

        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
//        List<Artifact> testArtifactList = new ArrayList<>();
//        testArtifactList.add(testArtifact1);
//        testArtifactList.add(testArtifact2);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int requestID = 3;
        final int loanFee = 123;
        final boolean approval = false;
        final Loan testLoan = new Loan();
        testLoan.setRequestID(requestID);
        testLoan.setLoanFee(loanFee);
        testLoan.setApproved(approval);
        testLoan.setNewrequestedArtifactsList();
        testLoan.addRequestedArtifact(testArtifact1);
        testLoan.addRequestedArtifact(testArtifact2);

        when(loanRepository.findLoanRequestByRequestID(requestID)).thenAnswer((InvocationOnMock invocation) -> testLoan);
        when(loanRepository.existsById(requestID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(loanService.getLoanByID(requestID)).thenAnswer((InvocationOnMock invocation) -> testLoan);

        Customer customer = customerService.addLoanToCustomer(returnedCustomer.getAccountID(), testLoan.getRequestID());
        int previousCustomerCredit = customer.getCredit();
        Customer approveLoancustomer = customerService.approveLoanOfCustomer(returnedCustomer.getAccountID(), testLoan.getRequestID());

        assertNotNull(approveLoancustomer);
        assertEquals(previousCustomerCredit+loanFee, approveLoancustomer.getCredit());
    }

    @Test
    void testApproveLoansOfCustomerWithInvalidLoanID() {
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
        when(customerRepository.findByAccountID(customerID)).thenAnswer((InvocationOnMock invocation) -> returnedCustomer);

        final int artifact1ID = 1;
        final int artifact2ID = 2;
        final Artifact.ArtType artifact1Type = Artifact.ArtType.Painting;
        final Artifact.ArtType artifact2Type = Artifact.ArtType.Sculpture;
        final String artifact1Name = "Mona Lisa";
        final String artifact2Name = "David";
        final boolean artifact1Loanable = true;
        final boolean artifact2Loanable = true;
        final boolean artifact1Loaned = false;
        final boolean artifact2Loaned = false;
        final int artifact1LoanFee = 100;
        final int artifact2LoanFee = 200;
        Artifact testArtifact1 = new Artifact(artifact1ID, artifact1Name, artifact1Type, artifact1Loanable, artifact1Loaned, artifact1LoanFee);
        Artifact testArtifact2 = new Artifact(artifact2ID, artifact2Name, artifact2Type, artifact2Loanable, artifact2Loaned, artifact2LoanFee);
//        List<Artifact> testArtifactList = new ArrayList<>();
//        testArtifactList.add(testArtifact1);
//        testArtifactList.add(testArtifact2);
        HashSet<Integer> testArtifactIDSet = new HashSet<>();
        testArtifactIDSet.add(testArtifact1.getArtID());
        testArtifactIDSet.add(testArtifact2.getArtID());

        final int requestID = 3;
        final int loanFee = 123;
        final boolean approval = false;
        final Loan testLoan = new Loan();
        testLoan.setRequestID(requestID);
        testLoan.setLoanFee(loanFee);
        testLoan.setApproved(approval);
        testLoan.setNewrequestedArtifactsList();
        testLoan.addRequestedArtifact(testArtifact1);
        testLoan.addRequestedArtifact(testArtifact2);

        when(loanRepository.findLoanRequestByRequestID(requestID)).thenAnswer((InvocationOnMock invocation) -> testLoan);
        when(loanRepository.existsById(requestID)).thenAnswer((InvocationOnMock invocation) -> true);
        when(loanService.getLoanByID(requestID)).thenAnswer((InvocationOnMock invocation) -> testLoan);

        Customer customer = customerService.addLoanToCustomer(returnedCustomer.getAccountID(), testLoan.getRequestID());
        when(loanRepository.existsById(requestID)).thenAnswer((InvocationOnMock invocation) -> false);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> customerService.approveLoanOfCustomer(returnedCustomer.getAccountID(), testLoan.getRequestID()));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Loan not found", ex.getMessage());
    }
}
