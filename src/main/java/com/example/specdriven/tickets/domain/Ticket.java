package com.example.specdriven.tickets.domain;

import java.math.BigDecimal;

public record Ticket(
        String id,
        String name,
        TransitMode mode,
        TicketType type,
        BigDecimal price
) {
}

