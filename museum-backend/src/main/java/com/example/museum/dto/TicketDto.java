package com.example.museum.dto;

import com.example.museum.model.Ticket;

import java.sql.Date;
import java.sql.Time;

public class TicketDto {

    private int TicketID;
    private Date day;
    private int price;

    public TicketDto(Ticket Ticket){
        this.TicketID = Ticket.getTicketID();
        this.day = Ticket.getDay();
        this.price = Ticket.getPrice();
    }

    public TicketDto(){}

    public int getTicketID(){
        return TicketID;
    }

    public Date getDay(){
        return day;
    }

    public int getPrice(){ return price; }

    public Ticket toModel(){
        Ticket Ticket = new Ticket();
        Ticket.setTicketID(this.TicketID);
        Ticket.setDay(this.getDay());
        return Ticket;
    }
}

