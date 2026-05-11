package com.example.specdriven.tickets;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TicketService {

    private static final Comparator<Ticket> DISPLAY_ORDER =
            Comparator.<Ticket>comparingInt(t -> t.getTransitMode().ordinal())
                    .thenComparingInt(t -> t.getTicketType().ordinal());

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll().stream()
                .sorted(DISPLAY_ORDER)
                .toList();
    }

    public List<Ticket> findByMode(TransitMode mode) {
        if (mode == null) {
            return findAll();
        }
        return ticketRepository.findByTransitMode(mode).stream()
                .sorted(DISPLAY_ORDER)
                .toList();
    }
}
