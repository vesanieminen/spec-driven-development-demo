package com.example.specdriven.domain;

import java.math.BigDecimal;

public class Ticket {
    private final Long id;
    private final String name;
    private final String description;
    private final TransitMode transitMode;
    private final TicketType ticketType;
    private final BigDecimal price;

    public Ticket(Long id, String name, String description, TransitMode transitMode, TicketType ticketType, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.transitMode = transitMode;
        this.ticketType = ticketType;
        this.price = price;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public TransitMode getTransitMode() { return transitMode; }
    public TicketType getTicketType() { return ticketType; }
    public BigDecimal getPrice() { return price; }
}
