package com.example.museum.repository;

import com.example.museum.model.Owner;
import com.example.museum.model.Business;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class OwnerRepositoryTests {
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private BusinessRepository businessRepository;

    @AfterEach
    public void clearDatabase() {
        ownerRepository.deleteAll();
        businessRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadOwner() {
        // Business
        int ticketFee = 10;
        Business business = new Business();
        business.setTicketFee(ticketFee);
        business = businessRepository.save(business);
        int businessID = business.getBusinessID();

        String ownerUsername = "Owner";
        String ownerPassword = "password";

        Owner owner = new Owner();
        owner.setUsername(ownerUsername);
        owner.setPassword(ownerPassword);
        owner.setBusiness(business);

        owner = ownerRepository.save(owner);
        int ownerId = owner.getAccountID();

        owner = null;
        business = null;

        owner = ownerRepository.findByAccountID(ownerId);
        business = businessRepository.findBusinessByBusinessID(businessID);

        assertNotNull(owner);
        assertEquals(ownerId, owner.getAccountID());
        assertEquals(ownerUsername, owner.getUsername());
        assertEquals(ownerPassword, owner.getPassword());
        assertEquals(business, owner.getBusiness());
        assertEquals(businessID, business.getBusinessID());
    }
}
