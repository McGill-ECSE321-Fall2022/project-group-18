package com.example.museum.service;

import com.example.museum.dto.TicketDto;
import com.example.museum.exceptions.DatabaseException;
import com.example.museum.model.BusinessHour;
import com.example.museum.model.Customer;
import com.example.museum.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.museum.model.Ticket;
import com.example.museum.repository.TicketRepository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Time;
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
    public Ticket createTicket(Date day, int price){
        Ticket ticket = new Ticket();
        ticket.setDay(day);
        ticket.setPrice(price);
        if(ticketRepository.findByTicketID(ticket.getTicketID()) != null){
            throw new DatabaseException(HttpStatus.CONFLICT, "A ticket with the given id already exists.");
        }

        Iterator<Ticket> t = ticketRepository.findAll().iterator();
        while(t.hasNext()){
            Ticket curT = t.next();
            if(curT.getDay().toString().equals(day.toString())){
                throw new DatabaseException(HttpStatus.CONFLICT, "A Ticket with the given date already exists");
            }
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
        while(t.hasNext()){
            Ticket curT = t.next();
            if(curT.getDay().toString().equals(day.toString())){
                throw new DatabaseException(HttpStatus.CONFLICT, "A Ticket with the given date already exists");
            }
        }
        Ticket Ticket = ticketRepository.findByTicketID(id);
        Ticket.setDay(day);
        Ticket.setPrice(price);
        //I assume we need to save the new one to the database
        Ticket updatedTicket = ticketRepository.save(Ticket);
        return updatedTicket;
    }


    //TODO: test once aymen updates customer stuff
    public List<Ticket> getAllAvailableTickets() {
        List<Ticket> ticketsList = new ArrayList<>();
        Iterator<Ticket> tickets = ticketRepository.findAll().iterator();
        while (tickets.hasNext()) {
            Ticket t = tickets.next();
            ticketsList.add(t);
        }
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