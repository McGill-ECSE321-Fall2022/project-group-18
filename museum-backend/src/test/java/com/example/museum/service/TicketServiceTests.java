package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Customer;
import com.example.museum.model.Ticket;
import com.example.museum.repository.CustomerRepository;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TicketServiceTests {

    @Mock
    TicketRepository ticketRepository;
    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    TicketService ticketService;

    @Test
    public void testGetTicketID(){
        final int id = 1;
        final Date day = Date.valueOf("2022-11-08");
        final Ticket testTicket = new Ticket(id, day, 25);
        when(ticketRepository.findByTicketID(id)).thenAnswer((InvocationOnMock invocation) -> testTicket);

        Ticket Ticket = ticketService.getTicketById(id);

        assertNotNull(Ticket);
        assertEquals(Ticket.getDay(), testTicket.getDay());
    }

    @Test
    public void testGetTicketByInvalidID(){
        final int invalidID = 2;

        when(ticketRepository.findByTicketID(invalidID)).thenAnswer((InvocationOnMock) -> null);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> ticketService.getTicketById(invalidID));

        assertEquals("Ticket not found", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    public void testCreateTicket(){
        //mock the database and return the 0th argument (which is the Ticket object)
        when(ticketRepository.save(any(Ticket.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        final Date day = Date.valueOf("2022-11-11");
        Ticket returnedTicket = ticketService.createTicket(day, 34);

        //checking fields - just like persistence testing
        assertNotNull(returnedTicket);
        assertEquals(day, returnedTicket.getDay());

        //the most important part - making sure we actually performed a save operation
        verify(ticketRepository, times(1)).save(returnedTicket);
    }

    @Test
        //test that only one Ticket can exist for a given date (checked manually with an exception - and not in the database)
    void testDateUniqueTicketField(){
        Date day = Date.valueOf("2022-11-12");
        Ticket existingTicket = new Ticket();
        existingTicket.setDay(day);
        ArrayList<Ticket> existingTickets = new ArrayList<Ticket>();
        existingTickets.add(existingTicket);

        when(ticketRepository.findByTicketID(any(Integer.class))).thenReturn(null);
        when(ticketRepository.findAll()).thenReturn(existingTickets);


        DatabaseException exception = assertThrows(DatabaseException.class, () -> ticketService.createTicket(day, 21));
        assertEquals("A Ticket with the given date already exists", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());

    }

    @Test
    void testGetAllAvailableTickets(){
        int id1 = 1;
        Date day1 = Date.valueOf("2020-10-10");
        int fee1 = 11;
        Ticket ticket1 = new Ticket(1, day1, fee1);
        int id2 = 2;
        Date day2 = Date.valueOf("2020-11-11");
        int fee2 = 22;
        Ticket ticket2 = new Ticket(2, day2, fee2);
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket1);
        tickets.add(ticket2);

        Customer customer = new Customer();
        customer.addCustomerTicket(ticket1);
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);

        when(ticketRepository.findAll()).thenReturn(tickets);
        when(customerRepository.findAll()).thenReturn(customers);

        List<Ticket> availableTickets = ticketService.getAllAvailableTickets();

        //the customer has ticket with id1, so we should only see the second ticket returned in the list
        assertNotNull(availableTickets);
        assertEquals(1, availableTickets.size());
        assertEquals(id2, availableTickets.get(0).getTicketID());
        assertEquals(day2, availableTickets.get(0).getDay());
        assertEquals(fee2, availableTickets.get(0).getPrice());

        verify(customerRepository, times(1)).findAll();
        verify(ticketRepository, times(1)).findAll();
    }
}

