package com.example.museum.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.museum.dto.BusinessDto;
import com.example.museum.dto.OwnerDto;
import com.example.museum.model.Business;
import com.example.museum.model.Owner;
import com.example.museum.repository.OwnerRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OwnerIntegrationTest {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private OwnerRepository ownerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        ownerRepository.deleteAll();
    }

    @Test
    public void testCreateGetUpdateOwner() {
        Business business = createBusiness();
        int id = testCreateOwner(business);
        testGetOwner(id);
        testLoginOwner(business);
        testInvalidLoginOwner(business);
        testCreateInvalidOwner(business);
        testUpdateInvalidOwner();
        testUpdateOwner(id);
    }

    private Business createBusiness() {
        final int ticketFee = 10;
        final Business business = new Business(0, ticketFee);
        final BusinessDto businessDto = new BusinessDto(business);

        ResponseEntity<BusinessDto> businessResponse = client.postForEntity("/business", businessDto,
                BusinessDto.class);
        return businessResponse.getBody().toModel();
    }

    private int testCreateOwner(Business business) {
        final String username = "owner1";
        final String password = "password";
        final String firstName = "Owner";
        final String lastName = "Account";
        final OwnerDto ownerDto = new OwnerDto(
                new Owner(0, username, password, business, firstName, lastName));

        ResponseEntity<OwnerDto> response = client.postForEntity("/owner", ownerDto, OwnerDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getAccountID() > 0);
        assertEquals(username, response.getBody().getUsername());
        assertEquals(password, response.getBody().getPassword());
        assertEquals(firstName, response.getBody().getFirstName());
        assertEquals(lastName, response.getBody().getLastName());

        return response.getBody().getAccountID();
    }

    private void testLoginOwner(Business business) {
        final String username = "owner1";
        final String password = "password";
        final String firstName = "Owner";
        final String lastName = "Account";
        final OwnerDto ownerDto = new OwnerDto(
                new Owner(0, username, password, business, firstName, lastName));

        ResponseEntity<String> response = client.postForEntity("/owner/login", ownerDto, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successful login", response.getBody());
    }

    private void testInvalidLoginOwner(Business business) {
        final String username = "owner1";
        final String password = "wrongPassword";
        final String firstName = "Owner";
        final String lastName = "Account";
        final OwnerDto ownerDto = new OwnerDto(
                new Owner(0, username, password, business, firstName, lastName));

        ResponseEntity<String> response = client.postForEntity("/owner/login", ownerDto, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Wrong password", response.getBody());
    }

    private void testGetOwner(int id) {
        final String username = "owner1";
        final String password = "password";
        final String firstName = "Owner";
        final String lastName = "Account";
        ResponseEntity<OwnerDto> response = client.getForEntity("/owner/" + id, OwnerDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getAccountID());
        assertEquals(username, response.getBody().getUsername());
        assertEquals(password, response.getBody().getPassword());
        assertEquals(firstName, response.getBody().getFirstName());
        assertEquals(lastName, response.getBody().getLastName());
    }

    private void testCreateInvalidOwner(Business business) {
        // Create user with existing username
        final String username = "owner1";
        final String password = "password";
        final String firstName = "SecondOwner";
        final String lastName = "Account";
        final OwnerDto ownerDto = new OwnerDto(
                new Owner(0, username, password, business, firstName, lastName));

        try {
            ResponseEntity<OwnerDto> response = client.postForEntity("/owner", ownerDto, OwnerDto.class);
            assertEquals(1, 2);
        } catch (Exception e) {
            ResponseEntity<String> response = client.postForEntity("/owner", ownerDto, String.class);
            assertNotNull(response);
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        }
    }

    void testUpdateInvalidOwner(){
        final int id = Integer.MAX_VALUE;
        final String username = "owner1";
        final String password = "password";
        final String firstName = "Owner";
        final String lastName = "Account";
        final Business business = new Business();
        final Owner owner = new Owner(id, username, password, business, firstName, lastName);
        final OwnerDto ownerDto = new OwnerDto(owner);

        try {
            ResponseEntity<OwnerDto> response = client.postForEntity("/owner/update/" + id, ownerDto, OwnerDto.class);
            assertEquals(1, 2);
        } catch (Exception e) {
            ResponseEntity<String> response = client.postForEntity("/owner/update/" + id, ownerDto, String.class);
            assertNotNull(response);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }


    }

    void testUpdateOwner(int ownerId){
        final int id = ownerId;
        final String username = "owner123";
        final String password = "password123";
        final String firstName = "Marwan";
        final String lastName = "Kanaan";
        final Business business = new Business();
        final int ticketFee = 10;
        business.setTicketFee(ticketFee);
        final Owner owner = new Owner(id, username, password, business, firstName, lastName);
        final OwnerDto ownerDto = new OwnerDto(owner);

        ResponseEntity<OwnerDto> response = client.postForEntity("/owner/update/" + id, ownerDto, OwnerDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getAccountID());
        assertEquals(ticketFee, response.getBody().getBusiness().getTicketFee());
    }
}
