package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.BusinessHour;
import com.example.museum.repository.BusinessHourRepository;
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
public class BusinessHourServiceTests {

    @Mock
    BusinessHourRepository businessHourRepository;

    @InjectMocks
    BusinessHourService businessHourService;

    @Test
    public void testGetBusinessHourID(){
        final int id = 1;
        final Date day = Date.valueOf("2022-11-08");
        final Time startTime = Time.valueOf("08:29:00");
        final Time endTime = Time.valueOf("16:45:00");
        final BusinessHour testBusinessHour = new BusinessHour(id, day, startTime, endTime);

        // very important line - we write down what our repo method SHOULD return (in this case, testBusinessHour should be returned)
        // if we get this wrong, it will be a bit confusing since the actual service would work, but this "hardcoded"
        // mock of the repo method would return something that the real repo method would not have
        when(businessHourRepository.findBusinessHourByBusinessHourID(id)).thenAnswer((InvocationOnMock invocation) -> testBusinessHour);

        BusinessHour businessHour = businessHourService.getBusinessHourById(id);

        assertNotNull(businessHour);
        assertEquals(businessHour.getDay(), testBusinessHour.getDay());
        assertEquals(businessHour.getCloseTime(), testBusinessHour.getCloseTime());
        assertEquals(businessHour.getOpenTime(), testBusinessHour.getOpenTime());
    }

    @Test
    void testGetBusinessHourByInvalidID(){
        final int invalidID = 696969;

        when(businessHourRepository.findBusinessHourByBusinessHourID(invalidID)).thenAnswer((InvocationOnMock) -> null);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> businessHourService.getBusinessHourById(invalidID));

        assertEquals("BusinessHour not found", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void testCreateBusinessHour(){
        //mock the database and return the 0th argument (which is the BusinessHour object)
        when(businessHourRepository.save(any(BusinessHour.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        final Date day = Date.valueOf("2022-09-11");
        final Time openTime = Time.valueOf("08:00:01");
        final Time closeTime = Time.valueOf("15:06:00");
        BusinessHour bh = new BusinessHour();
        bh.setDay(day);
        bh.setOpenTime(openTime);
        bh.setCloseTime(closeTime);
        BusinessHour returnedBusinessHour = businessHourService.createBusinessHour(bh);

        //checking fields - just like persistence testing
        assertNotNull(returnedBusinessHour);
        assertEquals(day, returnedBusinessHour.getDay());
        assertEquals(openTime, returnedBusinessHour.getOpenTime());
        assertEquals(closeTime, returnedBusinessHour.getCloseTime());

        //the most important part - making sure we actually performed a save operation
        verify(businessHourRepository, times(1)).save(bh);
    }

    @Test
    //test that only one business hour can exist for a given date (checked manually with an exception - and not in the database)
    void testDateUniqueBusinessHourField(){
        //this test does not work because idk how to represent an iterable here and to make it into a mock... or whatever
    /*
        Iterator<BusinessHour> bHours = businessHourRepository.findAll().iterator();
        while(bHours.hasNext()){
            BusinessHour bh = bHours.next();
            if(bh.getDay().toString().equals(businessHour.getDay().toString())){
                throw new DatabaseException(HttpStatus.CONFLICT, "A BusinessHour with the given date already exists");
            }
        }
     */
//      when(businessHourRepository.findAll(any(Object))).thenAnswer((InvocationOnMock invocation) -> testBusinessHour);
        final Date day1 = Date.valueOf("2022-11-08");
        final Time startTime1 = Time.valueOf("08:29:00");
        final Time endTime1 = Time.valueOf("16:45:00");
        final BusinessHour testBusinessHour1 = new BusinessHour();
        testBusinessHour1.setDay(day1);
        testBusinessHour1.setOpenTime(startTime1);
        testBusinessHour1.setCloseTime(endTime1);
        when(businessHourRepository.save(testBusinessHour1)).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
        BusinessHour returnedBusinessHour1 = businessHourService.createBusinessHour(testBusinessHour1);

        final Date day2 = Date.valueOf("2022-11-08");
        final Time startTime2 = Time.valueOf("08:45:00");
        final Time endTime2 = Time.valueOf("16:55:00");
        final BusinessHour testBusinessHour2 = new BusinessHour();
        testBusinessHour2.setDay(day2);
        testBusinessHour2.setOpenTime(startTime2);
        testBusinessHour2.setCloseTime(endTime2);
        //this is definitely cheating - but I see no way to represent iterables and do this legitimately
        when(businessHourRepository.save(testBusinessHour2)).thenAnswer((InvocationOnMock invocation) -> null);
//        Iterable<BusinessHour> resultBHour = new Iterable<BusinessHour>();
//        when(businessHourRepository.findAll()).thenAnswer(() -> new Iterable<BusinessHour>)
        BusinessHour returnedBusinessHour2 = businessHourService.createBusinessHour(testBusinessHour2);
        assertNull(returnedBusinessHour2);

        verify(businessHourRepository, times(1)).save(testBusinessHour1);
    }
}
