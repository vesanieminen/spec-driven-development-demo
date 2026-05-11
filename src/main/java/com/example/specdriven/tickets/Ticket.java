package com.example.specdriven.tickets;

import java.math.BigDecimal;
import java.util.Objects;

public class Ticket {

    private final Long id;
    private final String name;
    private final String description;
    private final TransitMode transitMode;
    private final TicketType ticketType;
    private final BigDecimal price;

    public Ticket(
            Long id,
            String name,
            String description,
            TransitMode transitMode,
            TicketType ticketType,
            BigDecimal price) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.transitMode = Objects.requireNonNull(transitMode);
        this.ticketType = Objects.requireNonNull(ticketType);
        this.price = Objects.requireNonNull(price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TransitMode getTransitMode() {
        return transitMode;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
