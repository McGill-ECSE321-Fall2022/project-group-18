package com.example.museum.repository;

import com.example.museum.model.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Test of TicketRepository
// Springboot testing setup
@SpringBootTest
public class TicketRepositoryTests {
    // Injecting the Ticket Repository
    @Autowired
    private TicketRepository ticketRepository;

    // Clear the persistence data in database after each test
    @AfterEach
    public void clearDatabase() {
        ticketRepository.deleteAll();
    }

    // The test method - read and write the object, its reference and its attributes
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
        // Keep track of the automatically generated ID
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
