package com.example.museum.repository;

import com.example.museum.model.Owner;
import com.example.museum.model.Business;
import com.example.museum.model.BusinessHour;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class OwnerRepositoryTests {
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private BusinessHourRepository businessHourRepository;

    @AfterEach
    public void clearDatabase() {
        ownerRepository.deleteAll();
        businessRepository.deleteAll();
        businessHourRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadOwner() {
        // BusinessHour
        Date day = new Date(2023, 11, 10);
        Time open = new Time(8, 30, 0);
        Time close = new Time(16, 0, 0);
        BusinessHour businessHour = new BusinessHour();
        businessHour.setDay(day);
        businessHour.setOpenTime(open);
        businessHour.setCloseTime(close);
        businessHour = businessHourRepository.save(businessHour);
        int businessHourID = businessHour.getBusinessHourID();

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
        businessHour = null;

        owner = ownerRepository.findOwnerByAccountID(ownerId);
        business = businessRepository.findBusinessByBusinessID(businessID);
        businessHour = businessHourRepository.findBusinessHourByBusinessHourID(businessHourID);

        assertNotNull(owner);
        assertEquals(ownerId, owner.getAccountID());
        assertEquals(ownerUsername, owner.getUsername());
        assertEquals(ownerPassword, owner.getPassword());
        assertEquals(business, owner.getBusiness());
        assertEquals(businessID, business.getBusinessID());
        assertEquals(businessHourID, businessHour.getBusinessHourID());
    }
}
