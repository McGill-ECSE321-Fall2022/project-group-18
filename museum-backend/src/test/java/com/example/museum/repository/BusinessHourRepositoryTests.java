package com.example.museum.repository;

import com.example.museum.model.BusinessHour;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Test of BusinessHourRepo
// Springboot testing setup
@SpringBootTest
public class BusinessHourRepositoryTests {

    // Injecting the businessHourRepository
    @Autowired
    private BusinessHourRepository businessHourRepository;

    // Clear the persistence data in database after each test
    @AfterEach
    public void clearDatabase() {
        businessHourRepository.deleteAll();
    }

    // The test method - read and write the object, its reference and its attributes
    @Test
    public void testPersistAndLoadBusinessHour() {
        Date day = new Date(2022,10,13);
        Time open = new Time(8, 0, 0);
        Time close = new Time(17, 0, 0);

        // Create an businessHour object with some attributes
        BusinessHour businessHour = new BusinessHour();
        businessHour.setDay(day);
        businessHour.setOpenTime(open);
        businessHour.setCloseTime(close);
        // Save the businessHour in its database, tests its writing
        businessHour = businessHourRepository.save(businessHour);
        int id = businessHour.getBusinessHourID();
        // Keep track with its automatically generated ID
        businessHour = null;
        // Read from the database using the id, test its reading
        businessHour = businessHourRepository.findBusinessHourByBusinessHourID(id);
        // Test if an object is returned
        assertNotNull(businessHour);
        // Test if the attributes read from DB is correct
        assertEquals(id, businessHour.getBusinessHourID());
        assertEquals(day, businessHour.getDay());
        assertEquals(open, businessHour.getOpenTime());
        assertEquals(close, businessHour.getCloseTime());
    }
}
