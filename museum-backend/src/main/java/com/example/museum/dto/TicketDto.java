package com.example.museum.dto;

import com.example.museum.model.Ticket;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.sql.Time;

public class TicketDto {

    private int ticketID;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date day;
    private int price;

    public TicketDto(Ticket ticket){
        this.ticketID = ticket.getTicketID();
        this.day = ticket.getDay();
        this.price = ticket.getPrice();
    }

    public TicketDto(){}

    public int getTicketID(){
        return ticketID;
    }

    public Date getDay(){
        return day;
    }

    public int getPrice(){ return price; }

    public Ticket toModel(){
        Ticket ticket = new Ticket();
        ticket.setTicketID(this.ticketID);
        ticket.setDay(this.getDay());
        ticket.setPrice(this.getPrice());
        return ticket;
    }
}

