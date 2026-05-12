package com.example.specdriven.service;

import com.example.specdriven.domain.Ticket;
import com.example.specdriven.domain.TicketType;
import com.example.specdriven.domain.TransitMode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private static final List<Ticket> TICKETS = List.of(
        new Ticket(1L, "Bus Single Ride", "One-way trip on any city bus route.", TransitMode.BUS, TicketType.SINGLE_RIDE, new BigDecimal("2.50")),
        new Ticket(2L, "Bus Day Pass", "Unlimited bus rides for one day.", TransitMode.BUS, TicketType.DAY_PASS, new BigDecimal("7.00")),
        new Ticket(3L, "Train Single Ride", "One-way trip on any commuter train route.", TransitMode.TRAIN, TicketType.SINGLE_RIDE, new BigDecimal("4.50")),
        new Ticket(4L, "Train Day Pass", "Unlimited train rides for one day.", TransitMode.TRAIN, TicketType.DAY_PASS, new BigDecimal("12.00")),
        new Ticket(5L, "Metro Single Ride", "One-way trip on any metro line.", TransitMode.METRO, TicketType.SINGLE_RIDE, new BigDecimal("3.00")),
        new Ticket(6L, "Metro Day Pass", "Unlimited metro rides for one day.", TransitMode.METRO, TicketType.DAY_PASS, new BigDecimal("9.00")),
        new Ticket(7L, "Ferry Single Ride", "One-way trip on any ferry crossing.", TransitMode.FERRY, TicketType.SINGLE_RIDE, new BigDecimal("5.00")),
        new Ticket(8L, "Ferry Day Pass", "Unlimited ferry rides for one day.", TransitMode.FERRY, TicketType.DAY_PASS, new BigDecimal("14.00"))
    );

    public List<Ticket> findAll() {
        return TICKETS;
    }

    public List<Ticket> findByMode(TransitMode mode) {
        return TICKETS.stream().filter(t -> t.getTransitMode() == mode).toList();
    }

    public Optional<Ticket> findById(Long id) {
        return TICKETS.stream().filter(t -> t.getId().equals(id)).findFirst();
    }
}
