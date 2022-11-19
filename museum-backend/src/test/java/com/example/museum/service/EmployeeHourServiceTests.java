package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.EmployeeHour;
import com.example.museum.repository.EmployeeHourRepository;
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
public class EmployeeHourServiceTests {

    @Mock
    EmployeeHourRepository employeeHourRepository;

    @InjectMocks
    EmployeeHourService employeeHourService;

    @Test
    public void testGetEmployeeHourID(){
        final int id = 1;
        final Date day = Date.valueOf("2022-11-08");
        final Time startTime = Time.valueOf("08:29:00");
        final Time endTime = Time.valueOf("16:45:00");
        final EmployeeHour testEmployeeHour = new EmployeeHour(id, day, startTime, endTime);

        
        when(employeeHourRepository.findEmployeeHourByEmployeeHourID(id)).thenAnswer((InvocationOnMock invocation) -> testEmployeeHour);

        EmployeeHour employeeHour = employeeHourService.getEmployeeHourById(id);

        assertNotNull(employeeHour);
        assertEquals(employeeHour.getDay(), testEmployeeHour.getDay());
        assertEquals(employeeHour.getStartTime(), testEmployeeHour.getStartTime());
        assertEquals(employeeHour.getEndTime(), testEmployeeHour.getEndTime());
    }

    @Test
    void testGetEmployeeHourByInvalidID(){
        final int invalidID = 999999;

        when(employeeHourRepository.findEmployeeHourByEmployeeHourID(invalidID)).thenAnswer((InvocationOnMock) -> null);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> employeeHourService.getEmployeeHourById(invalidID));

        assertEquals("EmployeeHour not found", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void testCreateEmployeeHour(){
        
        when(employeeHourRepository.save(any(EmployeeHour.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        final Date day = Date.valueOf("2022-09-11");
        final Time startTime = Time.valueOf("08:00:01");
        final Time endTime = Time.valueOf("15:06:00");
        EmployeeHour bh = new EmployeeHour();
        bh.setDay(day);
        bh.setStartTime(startTime);
        bh.setEndTime(endTime);
        EmployeeHour returnedEmployeeHour = employeeHourService.createEmployeeHour(bh);

        
        assertNotNull(returnedEmployeeHour);
        assertEquals(day, returnedEmployeeHour.getDay());
        assertEquals(startTime, returnedEmployeeHour.getStartTime());
        assertEquals(endTime, returnedEmployeeHour.getEndTime());

    
        verify(employeeHourRepository, times(1)).save(bh);
    }


    @Test
    
    void testDateUniqueEmployeeHourField(){
        
        final Date day1 = Date.valueOf("2022-11-08");
        final Time startTime1 = Time.valueOf("08:29:00");
        final Time endTime1 = Time.valueOf("16:45:00");
        final EmployeeHour testEmployeeHour1 = new EmployeeHour();
        testEmployeeHour1.setDay(day1);
        testEmployeeHour1.setStartTime(startTime1);
        testEmployeeHour1.setEndTime(endTime1);
        when(employeeHourRepository.save(testEmployeeHour1)).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
        EmployeeHour returnedEmployeeHour1 = employeeHourService.createEmployeeHour(testEmployeeHour1);

        final Date day2 = Date.valueOf("2022-11-08");
        final Time startTime2 = Time.valueOf("08:45:00");
        final Time endTime2 = Time.valueOf("16:55:00");
        final EmployeeHour testEmployeeHour2 = new EmployeeHour();
        testEmployeeHour2.setDay(day2);
        testEmployeeHour2.setStartTime(startTime2);
        testEmployeeHour2.setEndTime(endTime2);
 
        when(employeeHourRepository.save(testEmployeeHour2)).thenAnswer((InvocationOnMock invocation) -> null);
        EmployeeHour returnedEmployeeHour2 = employeeHourService.createEmployeeHour(testEmployeeHour2);
        assertNull(returnedEmployeeHour2);

        verify(employeeHourRepository, times(1)).save(testEmployeeHour1);
    }
}

