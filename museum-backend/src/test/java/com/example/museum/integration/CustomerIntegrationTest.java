package com.example.museum.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.museum.dto.ArtifactDto;
import com.example.museum.dto.BusinessHourDto;
import com.example.museum.dto.DonationDto;
import com.example.museum.dto.LoanDto;
import com.example.museum.model.Artifact;
import com.example.museum.model.Donation;
import com.example.museum.model.Loan;
import com.example.museum.repository.DonationRepository;
import com.example.museum.repository.LoanRepository;
import com.example.museum.repository.TicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.museum.dto.CustomerDto;
import com.example.museum.dto.TicketDto;
import com.example.museum.model.Customer;
import com.example.museum.model.Ticket;
import com.example.museum.repository.CustomerRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        customerRepository.deleteAll();
    }

    @Test
    public void testCreateGetUpdateCustomer() {
        Ticket ticket = createTicket();
        int id = testCreateCustomer(ticket);
        testGetCustomer(id);
        testGetCustomerTickets(id);
        testLoginCustomer();
        testInvalidLoginCustomer();
        testCreateInvalidCustomer();
    }

    // @Test
    public void getAllCustomerLoans() {
        List<Artifact> artifacts = createArtifacts();
        Loan loan = createLoan(artifacts);

        final String username = "customer";
        final String password = "password";
        final String firstName = "Customer";
        final String lastName = "Account";
        final int credit = 5;
        final Customer customer = new Customer(0, username, password, firstName, lastName, credit);
        customer.addLoan(loan);
        final CustomerDto customerDto = new CustomerDto(customer);

        ResponseEntity<CustomerDto> customerResponse = client.postForEntity("/customer", customerDto,
                CustomerDto.class);

        ResponseEntity<List<LoanDto>> response = client.exchange(
                "/customer/" + customerResponse.getBody().getAccountID() + "/tickets",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<LoanDto>>() {
                });

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(loan.getRequestID(), response.getBody().get(0).getLoanId());
        assertEquals(loan.getApproved(), response.getBody().get(0).getLoanApproval());
        assertEquals(loan.getLoanFee(), response.getBody().get(0).getLoanFee());
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

    private Loan createLoan(List<Artifact> artifactList) {
        List<Integer> artifactIDList = new ArrayList<>();

        int artifactFeeSum = 0;

        for (Artifact artifact : artifactList) {
            artifactIDList.add(artifact.getArtID());
            artifactFeeSum += artifact.getLoanFee();
        }
        String artifactIDStr = "?artifactIDList=";
        for (int i = 0; i < artifactIDList.size(); i++) {
            artifactIDStr = artifactIDStr + artifactIDList.get(i).toString();
            if (i == artifactIDList.size() - 1) {
                break;
            }
            artifactIDStr = artifactIDStr + ",";
        }
        System.out.println("ID LIST:" + artifactIDStr);
        ResponseEntity<LoanDto> response = client.postForEntity("/loan" + artifactIDStr, null, LoanDto.class);
        return response.getBody().toModel();
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
}
