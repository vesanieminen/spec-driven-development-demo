package com.example.specdriven.tickets;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private TransitMode transitMode;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    private BigDecimal price;

    public enum TransitMode {
        BUS, TRAIN, METRO, FERRY
    }

    public enum TicketType {
        SINGLE_RIDE, DAY_PASS
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TransitMode getTransitMode() { return transitMode; }
    public void setTransitMode(TransitMode transitMode) { this.transitMode = transitMode; }

    public TicketType getTicketType() { return ticketType; }
    public void setTicketType(TicketType ticketType) { this.ticketType = ticketType; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
