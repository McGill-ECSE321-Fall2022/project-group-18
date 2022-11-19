package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.exceptions.RequestException;
import com.example.museum.model.Business;
import com.example.museum.model.BusinessHour;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BusinessServiceTests {
    @Mock
    BusinessRepository businessRepository;

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

    @Test
    void testCreateBusiness(){
        when(businessRepository.save(any(Business.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
        final int ticketFee = 69;
        final BusinessHour hour1 = new BusinessHour(1,Date.valueOf("2022-09-11"), Time.valueOf("08:00:00"), Time.valueOf("15:00:00"));
        final BusinessHour hour2 = new BusinessHour(2,Date.valueOf("2022-10-11"), Time.valueOf("08:05:00"), Time.valueOf("15:05:00"));
        Business business = new Business();
        business.setTicketFee(ticketFee);
        business.addBusinessHour(hour1);
        business.addBusinessHour(hour2);

        Business returnedBusiness = businessService.createBusiness(business);

        assertNotNull(returnedBusiness);
        assertEquals(ticketFee, returnedBusiness.getTicketFee());
        for(int i = 0; i < business.getBusinessHours().size(); i ++){
            assertEquals(business.getBusinessHour(i).getDay(), returnedBusiness.getBusinessHour(i).getDay());
            assertEquals(business.getBusinessHour(i).getOpenTime(), returnedBusiness.getBusinessHour(i).getOpenTime());
            assertEquals(business.getBusinessHour(i).getCloseTime(), returnedBusiness.getBusinessHour(i).getCloseTime());
        }
    }

    @Test
    void testCreateBusinessWithNegativeFee(){
        final int ticketFee = -1;
        Business business = new Business();
        business.setTicketFee(ticketFee);

        Exception ex = assertThrows(RequestException.class, () -> businessService.createBusiness(business));

        verify(businessRepository, times(0)).save(business);
    }
}
