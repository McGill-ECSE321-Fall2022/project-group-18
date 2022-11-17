package com.example.museum.controller;

import com.example.museum.dto.BusinessHourDto;
import com.example.museum.dto.TicketDto;
import com.example.museum.model.Business;
import com.example.museum.model.BusinessHour;
import com.example.museum.model.Ticket;
import com.example.museum.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TicketController {

    @Autowired
    TicketService TicketService;

    @PostMapping("/Ticket")
    public ResponseEntity<TicketDto> createTicket(@RequestBody TicketDto ticket) {
        Ticket createdTicket = TicketService.createTicket(ticket.getDay(), ticket.getPrice());
        TicketDto response = new TicketDto(createdTicket);
        return new ResponseEntity<TicketDto>(response, HttpStatus.CREATED);
    }

    //tested
    @PutMapping("/Ticket/update/{id}")
    public ResponseEntity<TicketDto> updateTicket(@PathVariable(name = "id") int id, @RequestBody TicketDto request){
        Ticket updatedTicket = TicketService.modifyTicketById(id, request.getDay(),null , request.getPrice(), -1);
        TicketDto response = new TicketDto(updatedTicket);
        return new ResponseEntity<TicketDto>(response, HttpStatus.OK);
    }

    @GetMapping("/Ticket/{id}")
    public ResponseEntity<TicketDto> getTicketByTicketID(@PathVariable int id) {
        Ticket Ticket = TicketService.getTicketById(id);
        return new ResponseEntity<TicketDto>(new TicketDto(Ticket), HttpStatus.OK);
    }

    @GetMapping("Ticket/all")
    public ResponseEntity<List<TicketDto>> getAllTickets(){
        List<Ticket> tickets = TicketService.getAllAvailableTickets();
        List<TicketDto> ticketDtoList = new ArrayList<>();
        for(Ticket t : tickets){
            TicketDto ticketDto = new TicketDto(t);
            ticketDtoList.add(ticketDto);
        }
        return new ResponseEntity<List<TicketDto>>(ticketDtoList, HttpStatus.OK);
    }
}
