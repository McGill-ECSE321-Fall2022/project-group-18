package com.example.museum.repository;

import com.example.museum.model.Ticket;
import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket, Integer> {
    Ticket findByTicketID(int id);
}
