package com.example.museum.controller;

import com.example.museum.dto.TicketDto;
import com.example.museum.model.Ticket;
import com.example.museum.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:8087")
@RestController
public class TicketController {

    @Autowired
    TicketService TicketService;

    //creates a ticket and returns a dto of this ticket upon completion
    @PostMapping("/ticket")
    public ResponseEntity<TicketDto> createTicket(@RequestBody TicketDto ticket) {
        Ticket createdTicket = TicketService.createTicket(ticket.getDay(), ticket.getPrice());
        TicketDto response = new TicketDto(createdTicket);
        return new ResponseEntity<TicketDto>(response, HttpStatus.CREATED);
    }

    // updates a ticket by passing the id of the ticket to update, a ticket dto to override the old ticket
    // returns this ticket dto upon completion
    @PostMapping("/ticket/update/{id}")
    public ResponseEntity<TicketDto> updateTicket(@PathVariable(name = "id") int id, @RequestBody TicketDto request) {
        Ticket updatedTicket = TicketService.modifyTicketById(id, request.getDay(), request.getPrice());
        TicketDto response = new TicketDto(updatedTicket);
        return new ResponseEntity<TicketDto>(response, HttpStatus.OK);
    }

    //get a ticket by id
    @GetMapping("/ticket/{id}")
    public ResponseEntity<TicketDto> getTicketByTicketID(@PathVariable int id) {
        Ticket Ticket = TicketService.getTicketById(id);
        return new ResponseEntity<TicketDto>(new TicketDto(Ticket), HttpStatus.OK);
    }

    //get all tickets that are not in a customer's possession
    //returns a list of ticket dtos
    @GetMapping("/ticket/all")
    public ResponseEntity<List<TicketDto>> getAllAvailableTickets() {
        List<Ticket> tickets = TicketService.getAllAvailableTickets();
        List<TicketDto> ticketDtoList = new ArrayList<>();
        for (Ticket t : tickets) {
            TicketDto ticketDto = new TicketDto(t);
            ticketDtoList.add(ticketDto);
        }
        return new ResponseEntity<List<TicketDto>>(ticketDtoList, HttpStatus.OK);
    }
}
