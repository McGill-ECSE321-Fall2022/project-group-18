package com.example.museum.repository;

import com.example.museum.model.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TicketRepositoryTests {
    @Autowired
    private TicketRepository ticketRepository;

    @AfterEach
    public void clearDatabase() {
        ticketRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadTicket() {
        // Create object
        Date date = new Date(2022, 10, 24);
        int price = 20;
        Ticket mondayOctoberTenth = new Ticket();
        mondayOctoberTenth.setDay(date);
        mondayOctoberTenth.setPrice(price);

        // Save object
        mondayOctoberTenth = ticketRepository.save(mondayOctoberTenth);
        int id = mondayOctoberTenth.getTicketID();

        mondayOctoberTenth = null;

        // Read object from database
        mondayOctoberTenth = ticketRepository.findByTicketID(id);

        // Assert that object has correct attributes
        assertNotNull(mondayOctoberTenth);
        assertEquals(id, mondayOctoberTenth.getTicketID());
        assertEquals(date, mondayOctoberTenth.getDay());
        assertEquals(price, mondayOctoberTenth.getPrice());
    }
}
