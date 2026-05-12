package com.example.specdriven.tickets;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    private final TicketRepository repository;

    public DataSeeder(TicketRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        seed("Bus Single Ride", "One-way trip on any city bus route",
                Ticket.TransitMode.BUS, Ticket.TicketType.SINGLE_RIDE, "2.50");
        seed("Bus Day Pass", "Unlimited bus travel for one day",
                Ticket.TransitMode.BUS, Ticket.TicketType.DAY_PASS, "7.00");
        seed("Train Single Ride", "One-way trip on any commuter train line",
                Ticket.TransitMode.TRAIN, Ticket.TicketType.SINGLE_RIDE, "4.50");
        seed("Train Day Pass", "Unlimited train travel for one day",
                Ticket.TransitMode.TRAIN, Ticket.TicketType.DAY_PASS, "12.00");
        seed("Metro Single Ride", "One-way trip on the metro/subway system",
                Ticket.TransitMode.METRO, Ticket.TicketType.SINGLE_RIDE, "3.00");
        seed("Metro Day Pass", "Unlimited metro travel for one day",
                Ticket.TransitMode.METRO, Ticket.TicketType.DAY_PASS, "9.00");
        seed("Ferry Single Ride", "One-way crossing on any ferry route",
                Ticket.TransitMode.FERRY, Ticket.TicketType.SINGLE_RIDE, "5.00");
        seed("Ferry Day Pass", "Unlimited ferry travel for one day",
                Ticket.TransitMode.FERRY, Ticket.TicketType.DAY_PASS, "14.00");
    }

    private void seed(String name, String description, Ticket.TransitMode mode,
                      Ticket.TicketType type, String price) {
        Ticket ticket = new Ticket();
        ticket.setName(name);
        ticket.setDescription(description);
        ticket.setTransitMode(mode);
        ticket.setTicketType(type);
        ticket.setPrice(new BigDecimal(price));
        repository.save(ticket);
    }
}
