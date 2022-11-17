package com.example.museum.integration;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import com.example.museum.dto.TicketDto;
import com.example.museum.model.Ticket;
import com.example.museum.repository.TicketRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TicketIntegrationTest {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        ticketRepository.deleteAll();
    }

    @Test
    public void testCreateGetUpdateTicket() {
        int id = testCreateTicket();
//        testCreateInvalidTicket();
//        testGetTicket(id);
//        testUpdateTicket(id);
//        testGetAllTickets(id);
    }

    private int testCreateTicket() {
        final Date day = Date.valueOf("2022-12-13");
        final int price = 92;
        Ticket tiki = new Ticket();
        tiki.setPrice(price);
        tiki.setDay(day);
        final TicketDto ticketDto = new TicketDto(
                tiki);

        ResponseEntity<TicketDto> response = client.postForEntity("/ticket", ticketDto,
                TicketDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getTicketID() > 0);
        assertEquals(day, response.getBody().getDay());
        assertEquals(price, response.getBody().getPrice());

        return response.getBody().getTicketID();
    }

    public void testCreateInvalidTicket(){
        final Date day = Date.valueOf("2022-12-14");
        final int price = 45;
        final TicketDto ticketDto = new TicketDto(
                new Ticket(0, day, price));

        try{
            ResponseEntity<TicketDto> response = client.postForEntity("/ticket", ticketDto, TicketDto.class);
            //we should not hit this line - an exception should be called before this
            assertEquals(1,2);
        }catch(Exception e){
            ResponseEntity<String> response = client.postForEntity("/ticket", ticketDto, String.class);
            assertNotNull(response);
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        }


    }

    private void testGetTicket(int id) {
        final Date day = Date.valueOf("2022-12-14");
        final int price = 46;
        ResponseEntity<TicketDto> response = client.getForEntity("/ticket/" + id,
                TicketDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getTicketID());
        assertEquals(day, response.getBody().getDay());
        assertEquals(price, response.getBody().getPrice());
    }

    private void testUpdateTicket(int id) {
        ResponseEntity<TicketDto> response = client.getForEntity("/ticket/" + id,
                TicketDto.class);
        final int prevPrice = response.getBody().getPrice();
        final int updatedPrice = prevPrice + 10;
        final TicketDto ticketDto = new TicketDto(new Ticket(0,
                response.getBody().getDay(), updatedPrice));
        ResponseEntity<TicketDto> response2 = client.postForEntity("/ticket/update/" + id, ticketDto,
                TicketDto.class);

        assertNotNull(response2);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertNotNull(response2.getBody());
        assertEquals(id, response2.getBody().getTicketID());
        assertEquals(response.getBody().getDay(), response2.getBody().getDay());
        assertNotEquals(prevPrice, response2.getBody().getPrice());
    }

    private void testGetAllTickets(int id){
        final Date day = Date.valueOf("2022-11-08");
        final int price = 47;

        ResponseEntity<List<TicketDto>> responseEntity =
                client.exchange(
                        "/ticket/all",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<TicketDto>>() {}
                );
        List<TicketDto> response = responseEntity.getBody();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(day, response.get(0).getDay());
        assertEquals(id, response.get(0).getTicketID());
        assertEquals(price, response.get(0).getPrice());
    }

    @Test
    public void testGetAllTicketsEmpty(){
        ResponseEntity<List> response = client.getForEntity("/ticket/all", List.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetInvalidTicket() {
        ResponseEntity<String> response = client.getForEntity("/ticket/" + Integer.MAX_VALUE,
                String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Ticket not found", response.getBody());
    }


}