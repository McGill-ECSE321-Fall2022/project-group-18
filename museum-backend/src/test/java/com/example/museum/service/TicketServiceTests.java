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
import java.util.ArrayList;

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
        final Ticket testTicket = new Ticket(id, day, 25);
        when(TicketRepository.findByTicketID(id)).thenAnswer((InvocationOnMock invocation) -> testTicket);

        Ticket Ticket = TicketService.getTicketById(id);

        assertNotNull(Ticket);
        assertEquals(Ticket.getDay(), testTicket.getDay());
    }

    @Test
    void testGetTicketByInvalidID(){
        final int invalidID = 2;

        when(TicketRepository.findByTicketID(invalidID)).thenAnswer((InvocationOnMock) -> null);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> TicketService.getTicketById(invalidID));

        assertEquals("Ticket not found", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void testCreateTicket(){
        //mock the database and return the 0th argument (which is the Ticket object)
        when(TicketRepository.save(any(Ticket.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        final Date day = Date.valueOf("2022-11-11");
        Ticket returnedTicket = TicketService.createTicket(day, 34);

        //checking fields - just like persistence testing
        assertNotNull(returnedTicket);
        assertEquals(day, returnedTicket.getDay());

        //the most important part - making sure we actually performed a save operation
        verify(TicketRepository, times(1)).save(returnedTicket);
    }

    @Test
        //test that only one Ticket can exist for a given date (checked manually with an exception - and not in the database)
    void testDateUniqueTicketField(){
        Date day = Date.valueOf("2022-11-12");
        Ticket existingTicket = new Ticket();
        existingTicket.setDay(day);
        ArrayList<Ticket> existingTickets = new ArrayList<Ticket>();
        existingTickets.add(existingTicket);

        when(TicketRepository.findByTicketID(any(Integer.class))).thenReturn(null);
        when(TicketRepository.findAll()).thenReturn(existingTickets);


        DatabaseException exception = assertThrows(DatabaseException.class, () -> TicketService.createTicket(day, 21));
        assertEquals("A Ticket with the given date already exists", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());

    }
}

