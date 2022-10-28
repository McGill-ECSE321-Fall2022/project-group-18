package com.example.museum.repository;

import com.example.museum.model.Ticket;
import org.springframework.data.repository.CrudRepository;

// extending the repository
public interface TicketRepository extends CrudRepository<Ticket, Integer> {
    // DAO implementation here
    // using ticketID to find Ticket object
    Ticket findByTicketID(int id);
}
