package com.example.museum.repository;

import com.example.museum.model.Owner;
import com.example.museum.model.Business;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Test of OwnerRepository
// Springboot testing setup
@SpringBootTest
public class OwnerRepositoryTests {
    // Injecting the OwnerRepository
    @Autowired
    private OwnerRepository ownerRepository;
    // Injecting the BusinessRepository
    @Autowired
    private BusinessRepository businessRepository;

    // Clear the persistence data in database after each test
    @AfterEach
    public void clearDatabase() {
        ownerRepository.deleteAll();
        businessRepository.deleteAll();
    }

    // The test method - read and write the object, its reference and its attributes
    @Test
    public void testPersistAndLoadOwner() {
        // Business
        int ticketFee = 10;
        // Create a new Business object with some attributes
        Business business = new Business();
        business.setTicketFee(ticketFee);
        // Save the business in its database, tests its writing
        business = businessRepository.save(business);
        // Keep track with its automatically generated ID
        int businessID = business.getBusinessID();

        String ownerUsername = "Owner";
        String ownerPassword = "password";
        // Create an Owner object with some attributes and associations
        Owner owner = new Owner();
        owner.setUsername(ownerUsername);
        owner.setPassword(ownerPassword);
        owner.setBusiness(business);
        // Save the owner in its database, tests its writing
        owner = ownerRepository.save(owner);
        // Keep track with its automatically generated ID
        int ownerId = owner.getAccountID();

        owner = null;
        business = null;
        // Read from the database using the id, test its reading
        owner = ownerRepository.findByAccountID(ownerId);
        // Test if an object is returned
        assertNotNull(owner);
        // Test if the attributes read from DB is correct
        assertEquals(ownerId, owner.getAccountID());
        assertEquals(ownerUsername, owner.getUsername());
        assertEquals(ownerPassword, owner.getPassword());
        // Test if the associations are correct
        assertNotNull(owner.getBusiness());
        assertEquals(businessID, owner.getBusiness().getBusinessID());
        assertEquals(ticketFee, owner.getBusiness().getTicketFee());
    }
}
