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
    void testGetTicketByInvalidID(){
        final int invalidID = 3178;

        when(ticketRepository.findByTicketID(invalidID)).thenAnswer((InvocationOnMock) -> null);

        DatabaseException ex = assertThrows(DatabaseException.class, () -> ticketService.getTicketById(invalidID));

        assertEquals("Ticket not found", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void testCreateTicket(){
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
    //create a ticket using the second createTicket service method (takes in a ticket object instead of the ticket's parameters)
    void testCreateTicket2(){
        when(ticketRepository.save(any(Ticket.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        final Date day = Date.valueOf("2022-11-11");
        final int price = 34;
        Ticket ticket = new Ticket();
        ticket.setDay(day);
        ticket.setPrice(price);

        Ticket returnedTicket = ticketService.createTicket(ticket);

        //checking fields - just like persistence testing
        assertNotNull(returnedTicket);
        assertEquals(day, returnedTicket.getDay());
        assertEquals(price, returnedTicket.getPrice());

        //the most important part - making sure we actually performed a save operation
        verify(ticketRepository, times(1)).save(returnedTicket);
    }

    @Test
    void testCreateTicketInvalidPrice(){
        //mock the database and return the 0th argument (which is the Ticket object)
//        when(ticketRepository.findByTicketID(anyInt())).thenReturn(null);

        final Date day = Date.valueOf("2022-11-11");
        final int price = -5;

        //checking fields - just like persistence testing
        assertThrows(DatabaseException.class, () -> ticketService.createTicket(day, price));

    }

    @Test
    //test that we get only the tickets that are NOT in a customer's possession
    void testGetAllAvailableTickets(){
        final Ticket ticket1 = new Ticket(1,Date.valueOf("2022-09-11"), 40);
        final Ticket ticket2 = new Ticket(2,Date.valueOf("2022-10-11"), 30);
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket1);
        tickets.add(ticket2);

        List<Customer> customers = new ArrayList<>();

        when(ticketRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> tickets);
        when(customerRepository.findAll()).thenAnswer((InvocationOnMock) -> customers);

        List<Ticket> returnedTickets = ticketService.getAllAvailableTickets();

        assertNotNull(returnedTickets);
        assertEquals(2, returnedTickets.size());
        assertEquals(ticket1.getTicketID(), returnedTickets.get(0).getTicketID());
        assertEquals(ticket2.getTicketID(), returnedTickets.get(1).getTicketID());
    }

    @Test
    //test to get all tickets - even if customer owns tickets (this should be irrelevant but the test simply ensures that this assumption was correct)
    void testGetAllTicketsWhenCustomersOwnTickets(){
        final Ticket ticket1 = new Ticket(1,Date.valueOf("2022-09-11"), 40);
        final Ticket ticket2 = new Ticket(2,Date.valueOf("2022-10-11"), 30);
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket1);
        tickets.add(ticket2);

        Customer customer = new Customer();
        customer.addCustomerTicket(ticket2);
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);

        when(ticketRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> tickets);
        when(customerRepository.findAll()).thenAnswer((InvocationOnMock) -> customers);

        List<Ticket> returnedTickets = ticketService.getAllAvailableTickets();

        assertNotNull(returnedTickets);
        assertEquals(1, returnedTickets.size());
        assertEquals(ticket1.getTicketID(), returnedTickets.get(0).getTicketID());
    }

    @Test
    //test that we get all tickets - even when some are in a customer's possession
    void testGetAllTickets(){
        final Ticket ticket1 = new Ticket(1,Date.valueOf("2022-09-11"), 40);
        final Ticket ticket2 = new Ticket(2,Date.valueOf("2022-10-11"), 30);
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket1);
        tickets.add(ticket2);

        Customer customer = new Customer();
        customer.addCustomerTicket(ticket2);
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);

        when(ticketRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> tickets);

        List<Ticket> returnedTickets = ticketService.getAllTickets();

        assertNotNull(returnedTickets);
        assertEquals(2, returnedTickets.size());
        assertEquals(ticket1.getTicketID(), returnedTickets.get(0).getTicketID());
        assertEquals(ticket2.getTicketID(), returnedTickets.get(1).getTicketID());
    }

    @Test
    //generic test to update ticket fields
    void testUpdateTicket(){
        final int id = 1;
        final Ticket ticket1 = new Ticket(id,Date.valueOf("2022-12-12"), 50);
        List<Ticket> tick = new ArrayList<>();
        tick.add(ticket1);

        when(ticketRepository.findByTicketID(id)).thenAnswer((InvocationOnMock invocation) -> ticket1);
        when(ticketRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> tick);
        when(ticketRepository.save(any(Ticket.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        final Date day = Date.valueOf("2022-11-12");
        final int price = 51;

        Ticket returnedTicket = ticketService.modifyTicketById(id, day, price);

        assertNotNull(returnedTicket);
        assertEquals(day, returnedTicket.getDay());
        assertEquals(price, returnedTicket.getPrice());

        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    //test to update ticket price
    void testUpdateTicketPrice(){
        final int id = 1;
        final Ticket ticket1 = new Ticket(id,Date.valueOf("2022-12-12"), 50);
        List<Ticket> tick = new ArrayList<>();
        tick.add(ticket1);

        when(ticketRepository.findByTicketID(id)).thenAnswer((InvocationOnMock invocation) -> ticket1);
        when(ticketRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> tick);
        when(ticketRepository.save(any(Ticket.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        final Date day = Date.valueOf("2022-12-12");
        final int price = 51;

        Ticket returnedTicket = ticketService.modifyTicketById(id, day, price);

        assertNotNull(returnedTicket);
        assertEquals(day, returnedTicket.getDay());
        assertEquals(price, returnedTicket.getPrice());

        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    //test to ensure no ticket can have a negative price
    void testUpdateTicketNegativePrice(){
        final int id = 1;
        final Ticket ticket1 = new Ticket(id,Date.valueOf("2022-12-12"), 50);
        List<Ticket> tick = new ArrayList<>();
        tick.add(ticket1);

        when(ticketRepository.findByTicketID(id)).thenAnswer((InvocationOnMock invocation) -> ticket1);
        when(ticketRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> tick);

        final Date day = Date.valueOf("2022-12-12");
        final int price = -10;

        Exception ex = assertThrows(DatabaseException.class, () -> ticketService.modifyTicketById(id, day, price));

        assertEquals("Ticket price can't have a negative value", ex.getMessage());

        verify(ticketRepository, times(0)).save(any(Ticket.class));
    }



    @Test
    //test to update a ticket's date
    void testUpdateTicketDate(){
        final int id = 1;
        final Ticket ticket1 = new Ticket(id,Date.valueOf("2022-12-12"), 50);
        List<Ticket> tick = new ArrayList<>();
        tick.add(ticket1);

        when(ticketRepository.findByTicketID(id)).thenAnswer((InvocationOnMock invocation) -> ticket1);
        when(ticketRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> tick);
        when(ticketRepository.save(any(Ticket.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        final Date day = Date.valueOf("2022-12-10");
        final int price = 50;

        Ticket returnedTicket = ticketService.modifyTicketById(id, day, price);

        assertNotNull(returnedTicket);
        assertEquals(day, returnedTicket.getDay());
        assertEquals(price, returnedTicket.getPrice());

        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

}

