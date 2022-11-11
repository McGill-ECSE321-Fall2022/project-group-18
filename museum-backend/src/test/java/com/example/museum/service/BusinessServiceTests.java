package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Business;
import com.example.museum.model.BusinessHour;
import com.example.museum.repository.BusinessHourRepository;
import com.example.museum.repository.BusinessRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BusinessServiceTests {
    @Mock
    BusinessHourRepository businessHourRepository;
    @Mock
    BusinessRepository businessRepository;

    @InjectMocks
    BusinessHourService businessHourService;
    @InjectMocks
    BusinessService businessService;

    @Test
    public void testGetBusinessID(){
        final int businessHourID = 1;
        final Date day = Date.valueOf("2022-11-08");
        final Time startTime = Time.valueOf("08:29:00");
        final Time endTime = Time.valueOf("16:45:00");
        final BusinessHour testBusinessHour = new BusinessHour(businessHourID, day, startTime, endTime);

        final int businessID = 2;
        final int ticketFee = 15;
        final Business testBusiness = new Business(businessID, ticketFee);
        testBusiness.addBusinessHour(testBusinessHour);

        when(businessRepository.findBusinessByBusinessID(businessID)).thenAnswer((InvocationOnMock invocation) -> testBusiness);

        Business business = businessService.getBusinessByID(businessID);

        assertNotNull(business);
        assertEquals(business.getTicketFee(), testBusiness.getTicketFee());
        assertEquals(day, testBusiness.getBusinessHour(0).getDay());
        assertEquals(startTime, testBusiness.getBusinessHour(0).getOpenTime());
        assertEquals(endTime, testBusiness.getBusinessHour(0).getCloseTime());
    }

    @Test
    void testGetBusinessByInvalidID(){
        final int invalidID = 89214;

        when(businessRepository.findBusinessByBusinessID(invalidID)).thenAnswer((InvocationOnMock) -> null);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> businessService.getBusinessByID(invalidID));

        assertEquals("Business not found", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }
}
