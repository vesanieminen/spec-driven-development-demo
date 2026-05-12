package com.example.specdriven.tickets;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public List<Ticket> findAll() {
        return repository.findAll();
    }

    public List<Ticket> findByMode(Ticket.TransitMode mode) {
        return repository.findByTransitMode(mode);
    }

    public Optional<Ticket> findById(Long id) {
        return repository.findById(id);
    }
}
