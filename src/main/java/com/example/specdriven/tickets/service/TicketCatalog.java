package com.example.specdriven.tickets.service;

import com.example.specdriven.tickets.domain.Ticket;
import com.example.specdriven.tickets.domain.TicketType;
import com.example.specdriven.tickets.domain.TransitMode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class TicketCatalog {
    private final List<Ticket> seededTickets;

    public TicketCatalog() {
        List<Ticket> tickets = new ArrayList<>();
        // Seed data for UC-001 business rules BR-01 and BR-02.
        tickets.add(new Ticket("bus-single", "Bus Single Ride", TransitMode.BUS, TicketType.SINGLE_RIDE, new BigDecimal("2.80")));
        tickets.add(new Ticket("bus-day", "Bus Day Pass", TransitMode.BUS, TicketType.DAY_PASS, new BigDecimal("7.50")));
        tickets.add(new Ticket("train-single", "Train Single Ride", TransitMode.TRAIN, TicketType.SINGLE_RIDE, new BigDecimal("3.40")));
        tickets.add(new Ticket("train-day", "Train Day Pass", TransitMode.TRAIN, TicketType.DAY_PASS, new BigDecimal("9.50")));
        tickets.add(new Ticket("metro-single", "Metro Single Ride", TransitMode.METRO, TicketType.SINGLE_RIDE, new BigDecimal("2.60")));
        tickets.add(new Ticket("metro-day", "Metro Day Pass", TransitMode.METRO, TicketType.DAY_PASS, new BigDecimal("8.50")));
        tickets.add(new Ticket("ferry-single", "Ferry Single Ride", TransitMode.FERRY, TicketType.SINGLE_RIDE, new BigDecimal("4.20")));
        tickets.add(new Ticket("ferry-day", "Ferry Day Pass", TransitMode.FERRY, TicketType.DAY_PASS, new BigDecimal("12.00")));
        this.seededTickets = List.copyOf(tickets);
    }

    public List<Ticket> findAll() {
        return seededTickets;
    }

    public List<Ticket> findByMode(Optional<TransitMode> mode) {
        if (mode.isEmpty()) {
            return findAll();
        }
        TransitMode required = mode.get();
        return seededTickets.stream().filter(t -> t.mode() == required).toList();
    }

    public Optional<Ticket> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        String normalized = id.toLowerCase(Locale.ROOT);
        return seededTickets.stream().filter(t -> t.id().equals(normalized)).findFirst();
    }
}

