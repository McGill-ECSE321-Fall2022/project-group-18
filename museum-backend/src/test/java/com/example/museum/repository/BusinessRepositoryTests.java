package com.example.museum.repository;

import com.example.museum.model.Business;
import com.example.museum.model.BusinessHour;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.sql.Date;
import java.sql.Time;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Test of BusinessRepo
// Springboot testing setup
@SpringBootTest
public class BusinessRepositoryTests {
    // Injecting the businessRepository
    @Autowired
    private BusinessRepository businessRepository;
    // Injecting the businessHourRepository
    @Autowired
    private BusinessHourRepository businessHourRepository;

    // Clear the persistence data in database after each test
    @AfterEach
    public void clearDatabase() {
        businessRepository.deleteAll();
        businessHourRepository.deleteAll();
    }

    // The test method - read and write the object, its reference and its attributes
    @Test
    public void testPersistAndLoadBusiness() {
        //BusinessHour
        Date day = new Date(2023,11,10);
        Time open = new Time(8, 30, 0);
        Time close = new Time(16, 0, 0);
        // Create an businessHour object with some attributes
        BusinessHour businessHour = new BusinessHour();
        businessHour.setDay(day);
        businessHour.setOpenTime(open);
        businessHour.setCloseTime(close);
        // Save the businessHour in its database, tests its writing
        businessHour = businessHourRepository.save(businessHour);
        // Keep track with its automatically generated ID
        int businessHourID = businessHour.getBusinessHourID();

        //Business
        int ticketFee = 10;
        // Create an businessHour object with some attributes
        Business business = new Business();
        business.setTicketFee(ticketFee);
        business.addBusinessHour(businessHour);
        // Save the business in its database, tests its writing
        business = businessRepository.save(business);
        // Keep track with its automatically generated ID
        int businessID = business.getBusinessID();

        businessHour = null;
        business = null;
        // Read from the database using the id, test its reading
        business = businessRepository.findBusinessByBusinessID(businessID);
        // Test if an object is returned
        assertNotNull(business);
        // Test if the attributes read from DB is correct
        assertEquals(businessID, business.getBusinessID());
        List<BusinessHour> businessHourList = business.getBusinessHours();

        assertEquals(1, businessHourList.size());
        // Test if the associations are correct
        assertNotNull(business.getBusinessHour(0));
        assertEquals(day, business.getBusinessHour(0).getDay());
        assertEquals(open, business.getBusinessHour(0).getOpenTime());
        assertEquals(close, business.getBusinessHour(0).getCloseTime());
        assertNotNull(business.getBusinessHour(0).getBusinessHourID());
        assertEquals(businessHourID, business.getBusinessHour(0).getBusinessHourID());
    }
}
