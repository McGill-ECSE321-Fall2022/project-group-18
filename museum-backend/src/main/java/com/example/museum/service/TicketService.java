package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.museum.model.Ticket;
import com.example.museum.repository.TicketRepository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Time;
import java.util.Iterator;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

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
            if(curT.getDay().toString().equals(curT.getDay().toString())){
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


}
