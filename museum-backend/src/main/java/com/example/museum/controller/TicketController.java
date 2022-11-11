package com.example.museum.controller;

import com.example.museum.dto.TicketDto;
import com.example.museum.model.Business;
import com.example.museum.model.Ticket;
import com.example.museum.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TicketController {

    @Autowired
    TicketService TicketService;

    @PostMapping("/Ticket")
    public ResponseEntity<TicketDto> createTicket(@RequestBody TicketDto request) {
        Ticket TicketToCreate = request.toModel();
        Ticket createdTicket = TicketService.createTicket(null, -1);
        TicketDto response = new TicketDto(createdTicket);
        return new ResponseEntity<TicketDto>(response, HttpStatus.CREATED);
    }

    //tested
    @PutMapping("/Ticket/update/{id}")
    public ResponseEntity<TicketDto> updateTicket(@PathVariable int id, @RequestBody TicketDto request){
        Ticket updatedTicket = TicketService.modifyTicketById(id, request.getDay(), request.getPrice());
        TicketDto response = new TicketDto(updatedTicket);
        return new ResponseEntity<TicketDto>(response, HttpStatus.OK);
    }

    @GetMapping("/Ticket/{id}")
    public ResponseEntity<TicketDto> getEventByName(@PathVariable int id) {
        Ticket Ticket = TicketService.getTicketById(id);
        return new ResponseEntity<TicketDto>(new TicketDto(Ticket), HttpStatus.OK);
    }
}
