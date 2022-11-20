package com.example.museum.integration;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.museum.dto.ArtifactDto;
import com.example.museum.dto.DonationDto;
import com.example.museum.dto.LoanDto;
import com.example.museum.model.Artifact;
import com.example.museum.model.Donation;
import com.example.museum.model.Loan;
import com.example.museum.repository.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.museum.dto.CustomerDto;
import com.example.museum.dto.TicketDto;
import com.example.museum.model.Customer;
import com.example.museum.model.Ticket;

import java.sql.Array;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private TestRestTemplate client;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TicketRepository ticketRepository;


    @Autowired
    private ArtifactRepository artifactRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        customerRepository.deleteAll();
        ticketRepository.deleteAll();
        loanRepository.deleteAll();
        artifactRepository.deleteAll();
        donationRepository.deleteAll();
    }

    @Test
    public void testCreateGetUpdateCustomer() {
        Ticket ticket = createTicket();
        int id = testCreateCustomer(ticket);
        testGetCustomer(id);
        testGetCustomerTickets(id);
        testLoginCustomer();
        testUpdateCustomer(id);
        testInvalidLoginCustomer();
        testCreateInvalidCustomer();
    }

    @Test
    public void testCreateGetDeleteCustomer() {
        Ticket ticket = createTicket();
        int id = testCreateCustomer(ticket);
        testDeleteCustomer(id);
    }


    @Test
    public void testCreateCustomerAndLoan() {
        Ticket ticket = createTicket();
        int id = testCreateCustomer(ticket);
        List<Artifact> createArtifactList = createArtifact();
        Loan loan = createLoan(createArtifactList);
        testAddLoanToCustomer(id, loan.getRequestID());
        testGetLoansFromCustomer(id, loan);
        testApproveLoansForCustomer(id, loan);
        testRemoveLoanFromCustomer(id, loan.getRequestID());
    }

    @Test
    public void testCreateCustomerWithInvalidLonaID() {
        Ticket ticket = createTicket();
        int id = testCreateCustomer(ticket);
        List<Artifact> createArtifactList = createArtifact();
        Loan loan = createLoan(createArtifactList);
        testAddInvalidLoanToCustomer(id, loan.getRequestID()+1);
    }

    @Test
    public void testCreateCustomerAndGetInvalidLoan() {
        Ticket ticket = createTicket();
        int id = testCreateCustomer(ticket);
        List<Artifact> createArtifactList = createArtifact();
        Loan loan = createLoan(createArtifactList);
        testAddLoanToCustomer(id, loan.getRequestID());
        testRemoveInvalidLoanFromCustomer(id, loan.getRequestID() + 1);
    }

    @Test
    public void testCreateCustomerAndApproveInvalidLoan() {
        Ticket ticket = createTicket();
        int id = testCreateCustomer(ticket);
        List<Artifact> createArtifactList = createArtifact();
        Loan loan = createLoan(createArtifactList);
        testAddLoanToCustomer(id, loan.getRequestID());
        testGetLoansFromCustomer(id, loan);
        testApproveInvalidLoansForCustomer(id, loan);
    }



    private Ticket createTicket() {
        final Date day = Date.valueOf("2022-11-08");
        final int price = 25;
        final Ticket ticket = new Ticket(0, day, price);
        final TicketDto ticketDto = new TicketDto(ticket);

        ResponseEntity<TicketDto> response = client.postForEntity("/ticket", ticketDto, TicketDto.class);

        return response.getBody().toModel();
    }

    private List<Artifact> createArtifacts() {
        final String name1 = "Mona Lisa";
        final Artifact.ArtType type1 = Artifact.ArtType.Painting;
        final boolean loanable1 = true;
        final boolean loaned1 = false;
        final int loanFee1 = 100;
        Artifact artifact1 = new Artifact();
        artifact1.setName(name1);
        artifact1.setType(type1);
        artifact1.setLoanable(loanable1);
        artifact1.setLoaned(loaned1);
        artifact1.setLoanFee(loanFee1);
        ArtifactDto artifactDto1 = new ArtifactDto(artifact1);
        ResponseEntity<ArtifactDto> response1 = client.postForEntity("/artifact", artifactDto1, ArtifactDto.class);

        List<Artifact> artifactList = new ArrayList<>();
        artifactList.add(response1.getBody().toModel());

        final String name2 = "David";
        final Artifact.ArtType type2 = Artifact.ArtType.Sculpture;
        final boolean loanable2 = true;
        final boolean loaned2 = false;
        final int loanFee2 = 120;
        Artifact artifact2 = new Artifact();
        artifact2.setName(name2);
        artifact2.setType(type2);
        artifact2.setLoanable(loanable2);
        artifact2.setLoaned(loaned2);
        artifact2.setLoanFee(loanFee2);
        ArtifactDto artifactDto2 = new ArtifactDto(artifact2);
        ResponseEntity<ArtifactDto> response2 = client.postForEntity("/artifact", artifactDto2, ArtifactDto.class);
        artifactList.add(response2.getBody().toModel());
        return artifactList;
    }


    private int testCreateCustomer(Ticket ticket) {
        final String username = "customer1";
        final String password = "password";
        final String firstName = "Customer";
        final String lastName = "Account";
        final int credit = 10;
        final Customer customer = new Customer(0, username, password, firstName, lastName, credit);
        customer.addCustomerTicket(ticket);
        final CustomerDto customerDto = new CustomerDto(customer);

        ResponseEntity<CustomerDto> response = client.postForEntity("/customer", customerDto, CustomerDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getAccountID() > 0);
        assertEquals(username, response.getBody().getUsername());
        assertEquals(password, response.getBody().getPassword());
        assertEquals(firstName, response.getBody().getFirstName());
        assertEquals(lastName, response.getBody().getLastName());
        assertEquals(credit, response.getBody().getCredit());
        assertEquals(1, response.getBody().getCustomerTickets().size());
        assertEquals(ticket.getTicketID(), response.getBody().getCustomerTickets().get(0).getTicketID());
        assertEquals(ticket.getDay(), response.getBody().getCustomerTickets().get(0).getDay());
        assertEquals(ticket.getPrice(), response.getBody().getCustomerTickets().get(0).getPrice());

        return response.getBody().getAccountID();
    }

    private void testLoginCustomer() {
        final String username = "customer1";
        final String password = "password";
        final String firstName = "Customer";
        final String lastName = "Account";
        final int credit = 10;
        final CustomerDto customerDto = new CustomerDto(
                new Customer(0, username, password, firstName, lastName, credit));

        ResponseEntity<String> response = client.postForEntity("/customer/login", customerDto, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successful login", response.getBody());
    }

    private void testUpdateCustomer(int id) {
        Customer customer = customerRepository.findByAccountID(id);
        String newFirstName = "aNewCustomerName";
        customer.setFirstName(newFirstName);
        CustomerDto customerDto = new CustomerDto(customer);
        ResponseEntity<CustomerDto> response = client.postForEntity("/customer/"+id+"/update", customerDto, CustomerDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newFirstName, response.getBody().getFirstName());
    }

    private void testInvalidLoginCustomer() {
        final String username = "customer1";
        final String password = "wrongPassword";
        final String firstName = "Customer";
        final String lastName = "Account";
        final int credit = 10;
        final CustomerDto customerDto = new CustomerDto(
                new Customer(0, username, password, firstName, lastName, credit));

        ResponseEntity<String> response = client.postForEntity("/customer/login", customerDto, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Wrong password", response.getBody());
    }

    private void testGetCustomer(int id) {
        final String username = "customer1";
        final String password = "password";
        final String firstName = "Customer";
        final String lastName = "Account";
        final int credit = 10;
        ResponseEntity<CustomerDto> response = client.getForEntity("/customer/" + id, CustomerDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getAccountID());
        assertEquals(username, response.getBody().getUsername());
        assertEquals(password, response.getBody().getPassword());
        assertEquals(firstName, response.getBody().getFirstName());
        assertEquals(lastName, response.getBody().getLastName());
        assertEquals(credit, response.getBody().getCredit());
    }

    private void testGetCustomerTickets(int id) {
        final Date day = Date.valueOf("2022-11-08");
        final int price = 25;

        ResponseEntity<List<TicketDto>> response = client.exchange(
                "/customer/" + id + "/tickets",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TicketDto>>() {
                });

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(day, response.getBody().get(0).getDay());
        assertEquals(price, response.getBody().get(0).getPrice());
    }

    private void testCreateInvalidCustomer() {
        // Create user with existing username
        final String username = "customer1";
        final String password = "password";
        final String firstName = "SecondCustomer";
        final String lastName = "Account";
        final int credit = 20;
        final CustomerDto customerDto = new CustomerDto(
                new Customer(0, username, password, firstName, lastName, credit));

        try {
            ResponseEntity<CustomerDto> response = client.postForEntity("/customer", customerDto, CustomerDto.class);
            assertEquals(1, 2);
        } catch (Exception e) {
            ResponseEntity<String> response = client.postForEntity("/customer", customerDto, String.class);
            assertNotNull(response);
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        }
    }


    private List<Artifact> createArtifact() {
        final String name1 = "Mona Lisa";
        final Artifact.ArtType type1 = Artifact.ArtType.Painting;
        final boolean loanable1 = true;
        final boolean loaned1 = false;
        final int loanFee1 = 100;
        Artifact artifact1 = new Artifact();
        artifact1.setName(name1);
        artifact1.setType(type1);
        artifact1.setLoanable(loanable1);
        artifact1.setLoaned(loaned1);
        artifact1.setLoanFee(loanFee1);
        ArtifactDto artifactDto1 = new ArtifactDto(artifact1);
        ResponseEntity<ArtifactDto> response1 = client.postForEntity("/artifact", artifactDto1, ArtifactDto.class);

        List<Artifact> artifactList = new ArrayList<>();
        artifactList.add(response1.getBody().toModel());

        final String name2 = "David";
        final Artifact.ArtType type2 = Artifact.ArtType.Sculpture;
        final boolean loanable2 = true;
        final boolean loaned2 = false;
        final int loanFee2 = 120;
        Artifact artifact2 = new Artifact();
        artifact2.setName(name2);
        artifact2.setType(type2);
        artifact2.setLoanable(loanable2);
        artifact2.setLoaned(loaned2);
        artifact2.setLoanFee(loanFee2);
        ArtifactDto artifactDto2 = new ArtifactDto(artifact2);
        ResponseEntity<ArtifactDto> response2 = client.postForEntity("/artifact", artifactDto2, ArtifactDto.class);
        artifactList.add(response2.getBody().toModel());
        return artifactList;
    }

    private Loan createLoan(List<Artifact> artifactList) {
        List<Integer> artifactIDList = new ArrayList<>();
        for (Artifact artifact: artifactList) {
            artifactIDList.add(artifact.getArtID());
        }
        String artifactIDStr = "?artifactIDList=";
        for (int i = 0; i < artifactIDList.size(); i++) {
            artifactIDStr = artifactIDStr + artifactIDList.get(i).toString();
            if (i == artifactIDList.size()-1) {
                continue;
            }
            artifactIDStr = artifactIDStr + ",";
        }
        ResponseEntity<Integer> response = client.getForEntity("/loan" + artifactIDStr, Integer.class);
        assertNotNull(response.getBody());
        Loan loan = loanRepository.findLoanRequestByRequestID(response.getBody());
        return loan;
    }

    private void testAddLoanToCustomer(int customerID, int loanID) {
        String addLoanParam = "/customer/" + customerID + "/loans/add?loanID=" + loanID;
        ResponseEntity<List> response = client.getForEntity(addLoanParam, List.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains(loanID));
    }

    private void testGetLoansFromCustomer(int customerID, Loan loan) {
        String getLoanParam = "/customer/" + customerID + "/loans";
        ResponseEntity<List> response = client.getForEntity(getLoanParam, List.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        Loan loanFromGet = loanRepository.findLoanRequestByRequestID((Integer) response.getBody().get(0));
        assertEquals(loan.getRequestID(), loanFromGet.getRequestID());
        assertEquals(loan.getLoanFee(), loanFromGet.getLoanFee());
        assertEquals(loan.getApproved(), loanFromGet.getApproved());
        assertEquals(loan.getRequestedArtifacts().size(), loanFromGet.getRequestedArtifacts().size());
    }

    private void testRemoveLoanFromCustomer(int customerID, int loanID) {
        String addLoanParam = "/customer/" + customerID + "/loans/delete?loanID=" + loanID;
        ResponseEntity<List> response = client.getForEntity(addLoanParam, List.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().contains(loanID));
    }

    private void testAddInvalidLoanToCustomer(int customerID, int loanID) {
        String addLoanParam = "/customer/" + customerID + "/loans/add?loanID=" + loanID;
        ResponseEntity<String> response = client.getForEntity(addLoanParam, String.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Loan not found", response.getBody());
    }

    private void testRemoveInvalidLoanFromCustomer(int customerID, int loanID) {
        String deleteLoanParam = "/customer/" + customerID + "/loans/delete?loanID=" + loanID;
        ResponseEntity<String> response = client.getForEntity(deleteLoanParam, String.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Loan not found", response.getBody());
    }
    @Test
    void testGetCustomerEmptyDonationLists() {
        Donation donation1 = new Donation();
        Donation returnedDonation1 = donationRepository.save(donation1);
        Donation donation2 = new Donation();
        Donation returnedDonation2 = donationRepository.save(donation2);

        final String username = "customer11";
        final String password = "password1";
        final String firstName = "SecondCustomer1";
        final String lastName = "Account1";
        final int credit = 21;
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword(password);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setCredit(credit);
        customer.addCustomerDonatedArtifact(returnedDonation1);
        customer.addCustomerDonatedArtifact(returnedDonation2);
        Customer returnedCustomer = customerRepository.save(customer);

        CustomerDto customerDto = new CustomerDto(returnedCustomer);

        ResponseEntity<List<DonationDto>> responseEntity = client.exchange(
                "/customer/" + returnedCustomer.getAccountID() + "/donations",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DonationDto>>() {
                });
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<DonationDto> response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(returnedDonation1.getDonationID(), response.get(0).getDonationID());
        assertEquals(returnedDonation1.getDonatedArtifacts().size(), response.get(0).getArtifactList().size());
        assertEquals(returnedDonation2.getDonationID(), response.get(1).getDonationID());
        assertEquals(returnedDonation2.getDonatedArtifacts().size(), response.get(1).getArtifactList().size());


    }

    private void testApproveLoansForCustomer(int customerID, Loan loan) {
        Customer customer = customerRepository.findByAccountID(customerID);
        int previousCredit = customer.getCredit();
        String approveLoanParam = "/customer/" + customerID + "/loans/approve?loanID=" + loan.getRequestID();
        ResponseEntity<CustomerDto> response = client.getForEntity(approveLoanParam, CustomerDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(loan.getLoanFee() + previousCredit, response.getBody().getCredit());
    }

    private void testApproveInvalidLoansForCustomer(int customerID, Loan loan) {
        int invalidLoanID = loan.getRequestID() + 1;
        String approveLoanParam = "/customer/" + customerID + "/loans/approve?loanID=" + invalidLoanID;
        ResponseEntity<String> response = client.getForEntity(approveLoanParam, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Loan not found", response.getBody());
    }

    private Ticket createAnotherTicket() {
        final Date day = Date.valueOf("2022-11-10");
        final int price = 25;
        final Ticket ticket = new Ticket(0, day, price);
        final TicketDto ticketDto = new TicketDto(ticket);

        ResponseEntity<TicketDto> response = client.postForEntity("/ticket", ticketDto, TicketDto.class);

        return response.getBody().toModel();
    }

    private void testDeleteCustomer(int id) {
        ResponseEntity<String> response = client.getForEntity("/customer/"+id+"/delete", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Customer deleted successfully.", response.getBody());
    }
}
