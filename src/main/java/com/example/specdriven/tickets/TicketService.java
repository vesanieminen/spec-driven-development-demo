package com.example.specdriven.tickets;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final List<Ticket> tickets = List.of(
            new Ticket(
                    1L,
                    "Bus Single Ride",
                    "One-way trip on any city bus route.",
                    TransitMode.BUS,
                    TicketType.SINGLE_RIDE,
                    new BigDecimal("2.50")),
            new Ticket(
                    2L,
                    "Bus Day Pass",
                    "Unlimited bus rides until end of service day.",
                    TransitMode.BUS,
                    TicketType.DAY_PASS,
                    new BigDecimal("7.00")),
            new Ticket(
                    3L,
                    "Train Single Ride",
                    "One-way regional train trip within the service area.",
                    TransitMode.TRAIN,
                    TicketType.SINGLE_RIDE,
                    new BigDecimal("4.50")),
            new Ticket(
                    4L,
                    "Train Day Pass",
                    "Unlimited regional train trips for one service day.",
                    TransitMode.TRAIN,
                    TicketType.DAY_PASS,
                    new BigDecimal("12.00")),
            new Ticket(
                    5L,
                    "Metro Single Ride",
                    "One-way metro trip across all central stations.",
                    TransitMode.METRO,
                    TicketType.SINGLE_RIDE,
                    new BigDecimal("3.00")),
            new Ticket(
                    6L,
                    "Metro Day Pass",
                    "Unlimited metro rides across all central stations.",
                    TransitMode.METRO,
                    TicketType.DAY_PASS,
                    new BigDecimal("9.00")),
            new Ticket(
                    7L,
                    "Ferry Single Ride",
                    "One-way harbor ferry crossing.",
                    TransitMode.FERRY,
                    TicketType.SINGLE_RIDE,
                    new BigDecimal("5.00")),
            new Ticket(
                    8L,
                    "Ferry Day Pass",
                    "Unlimited harbor ferry crossings for one service day.",
                    TransitMode.FERRY,
                    TicketType.DAY_PASS,
                    new BigDecimal("14.00")));

    public List<Ticket> findAll() {
        return tickets;
    }

    public List<Ticket> findByTransitMode(TransitMode transitMode) {
        return tickets.stream()
                .filter(ticket -> ticket.getTransitMode() == transitMode)
                .toList();
    }

    public Optional<Ticket> findById(Long id) {
        return tickets.stream()
                .filter(ticket -> ticket.getId().equals(id))
                .findFirst();
    }
}
