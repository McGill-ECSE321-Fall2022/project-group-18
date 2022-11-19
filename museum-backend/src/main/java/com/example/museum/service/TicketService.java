package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.Customer;
import com.example.museum.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.museum.model.Ticket;
import com.example.museum.repository.TicketRepository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Transactional
    public Ticket createTicket(Ticket ticket) {
        if(ticket != null){
            return createTicket(ticket.getDay(), ticket.getPrice());
        }
        return null;
    }

    @Transactional
    public Ticket createTicket(Date day, int price){
        Ticket ticket = new Ticket();
        ticket.setDay(day);
        ticket.setPrice(price);
        if(price < 0){
            throw new DatabaseException(HttpStatus.CONFLICT, "Ticket price can't have a negative value");
        }

        ticket = ticketRepository.save(ticket);
        return ticket;
    }

    @Transactional
    public Ticket getTicketById(int id){
        Ticket ticket = ticketRepository.findByTicketID(id);
        if(ticket == null){
            throw new DatabaseException(HttpStatus.NOT_FOUND, "Ticket not found");
        }
        return ticket;
    }


    public Ticket modifyTicketById(int id, Date day, int price){
        Iterator<Ticket> t = ticketRepository.findAll().iterator();
        Ticket ticket = ticketRepository.findByTicketID(id);
        ticket.setDay(day);
        ticket.setPrice(price);
        if(ticketRepository.findByTicketID(ticket.getTicketID()).getPrice() < 0){
            throw new DatabaseException(HttpStatus.CONFLICT, "Ticket price can't have a negative value");
        }
        //I assume we need to save the new one to the database
        Ticket updatedTicket = ticketRepository.save(ticket);
        return updatedTicket;
    }

    public List<Ticket> getAllTickets(){
        List<Ticket> ticketsList = new ArrayList<>();
        Iterator<Ticket> tickets = ticketRepository.findAll().iterator();
        while (tickets.hasNext()) {
            Ticket t = tickets.next();
            ticketsList.add(t);
        }
        return ticketsList;
    }

    public List<Ticket> getAllAvailableTickets() {
        List<Ticket> ticketsList = getAllTickets();
        Iterator<Customer> customers = customerRepository.findAll().iterator();
        while(customers.hasNext()){
            Customer cust = customers.next();
            for(Ticket custTicket: cust.getCustomerTickets())
            if(ticketsList.contains(custTicket)){
                ticketsList.remove(custTicket);
            }
        }
        return ticketsList;
    }
}
