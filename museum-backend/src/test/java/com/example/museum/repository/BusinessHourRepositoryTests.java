package com.example.museum.repository;

import com.example.museum.model.Artifact;
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
public class BusinessHourRepositoryTests {
    @Autowired
    private BusinessHourRepository businessHourRepository;

    @AfterEach
    public void clearDatabase() {
        businessHourRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadBusinessHour() {
        Date day = new Date(2022,10,13);
        Time open = new Time(8, 0, 0);
        Time close = new Time(17, 0, 0);

        BusinessHour businessHour = new BusinessHour();
        businessHour.setDay(day);
        businessHour.setOpenTime(open);
        businessHour.setCloseTime(close);

        businessHour = businessHourRepository.save(businessHour);
        int id = businessHour.getBusinessHourID();

        businessHour = null;

        businessHour = businessHourRepository.findBusinessHourByBusinessHourID(id);

        assertNotNull(businessHour);
        assertEquals(id, businessHour.getBusinessHourID());
        assertEquals(day, businessHour.getDay());
        assertEquals(open, businessHour.getOpenTime());
        assertEquals(close, businessHour.getCloseTime());
    }
}
