package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Ticket;
import com.example.museum.repository.TicketRepository;
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
public class TicketServiceTests {

    @Mock
    TicketRepository TicketRepository;

    @InjectMocks
    TicketService TicketService;

    @Test
    public void testGetTicketID(){
        final int id = 1;
        final Date day = Date.valueOf("2022-11-08");
        final Ticket testTicket = new Ticket(id, day);
        when(TicketRepository.findTicketByTicketID(id)).thenAnswer((InvocationOnMock invocation) -> testTicket);

        Ticket Ticket = TicketService.getTicketById(id);

        assertNotNull(Ticket);
        assertEquals(Ticket.getDay(), testTicket.getDay());
    }

    @Test
    void testGetTicketByInvalidID(){
        final int invalidID = 2;

        when(TicketRepository.findTicketByTicketID(invalidID)).thenAnswer((InvocationOnMock) -> null);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> TicketService.getTicketById(invalidID));

        assertEquals("Ticket not found", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void testCreateTicket(){
        //mock the database and return the 0th argument (which is the Ticket object)
        when(TicketRepository.save(any(Ticket.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        final Date day = Date.valueOf("2022-11-11");
        Ticket t = new Ticket();
        t.setDay(day);
        Ticket returnedTicket = TicketService.createTicket(t);

        //checking fields - just like persistence testing
        assertNotNull(returnedTicket);
        assertEquals(day, returnedTicket.getDay());

        //the most important part - making sure we actually performed a save operation
        verify(TicketRepository, times(1)).save(t);
    }

    @Test
        //test that only one Ticket can exist for a given date (checked manually with an exception - and not in the database)
    void testDateUniqueTicketField(){
        final Date day1 = Date.valueOf("2022-11-12”);
        final Ticket testTicket1 = new Ticket();
        testTicket1.setDay(day1);
        Ticket returnedTicket1 = TicketService.createTicket(testTicket1);

        final Date day2 = Date.valueOf("2022-11-12”);
        final Ticket testTicket2 = new Ticket();
        testTicket2.setDay(day2);
        DatabaseException exception = assertThrows(DatabaseException.class, () -> TicketService.createTicket(testTicket2));
        assertEquals("A Ticket with the given date already exists", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());

        verify(TicketRepository, times(1)).save(testTicket1);
        verify(TicketRepository, times(0)).save(testTicket2);
    }
}

