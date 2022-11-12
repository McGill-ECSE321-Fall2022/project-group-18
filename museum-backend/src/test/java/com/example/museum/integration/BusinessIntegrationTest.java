package com.example.museum.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.Time;

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
import com.example.museum.model.Business;
import com.example.museum.model.BusinessHour;
import com.example.museum.repository.BusinessRepository;
import com.example.museum.repository.BusinessHourRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BusinessIntegrationTest {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private BusinessRepository businessRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        businessRepository.deleteAll();
    }

    @Test
    public void testCreateGetUpdateBusiness() {
        int id = testCreateBusiness();
        testGetBusiness(id);
    }

    private int testCreateBusiness() {
        final int ticketFee = 15;
        final Business business = new Business(0, ticketFee);
        final BusinessDto businessDto = new BusinessDto(business);

        ResponseEntity<BusinessDto> response = client.postForEntity("/business", businessDto, BusinessDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getBusinessID() > 0);
        assertEquals(ticketFee, response.getBody().getTicketFee());

        return response.getBody().getBusinessID();
    }

    private void testGetBusiness(int id) {
        final int ticketFee = 15;
        ResponseEntity<BusinessDto> response = client.getForEntity("/business/" + id, BusinessDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getBusinessID());
        assertEquals(ticketFee, response.getBody().getTicketFee());
    }

    @Test
    public void testCreateInvalidBusiness() {
        // Create with negative ticket fee
        final int ticketFee = -10;
        final Business business = new Business(0, ticketFee);
        final BusinessDto businessDto = new BusinessDto(business);
        ResponseEntity<String> response = client.postForEntity("/business", businessDto, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Ticket fee should be positive", response.getBody());
    }

    @Test
    public void testGetInvalidBusiness() {
        ResponseEntity<String> response = client.getForEntity("/business/" + Integer.MAX_VALUE, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Business not found", response.getBody());
    }
}
