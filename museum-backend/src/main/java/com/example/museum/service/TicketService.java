package com.example.museum.service;

import com.example.museum.exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Time;
import java.util.Iterator;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Transactional
    public TicketService createTicket(Ticket ticket){
        if(ticketRepository.findTicketByTicketID(ticket.getTicketID()) != null){
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

    public Ticket modifyTicketById(int id, Date day, Time openTime, Time closeTime){
        Iterator<Ticket> t = TicketRepository.findAll().iterator();
        while(t.hasNext()){
            Ticket curT = t.next();
            if(curT.getDay().toString().equals(day.toString())){
                throw new DatabaseException(HttpStatus.CONFLICT, "A Ticket with the given date already exists");
            }
        }
        Ticket Ticket = TicketRepository.findTicketByTicketID(id);
        Ticket.setDay(day);
        //I assume we need to save the new one to the database
        Ticket updatedTicket = TicketRepository.save(Ticket);
        return updatedTicket;
    }


}
