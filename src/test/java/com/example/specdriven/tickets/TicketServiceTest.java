package com.example.specdriven.tickets;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TicketServiceTest {

    private final TicketService ticketService = new TicketService();

    @Test
    void seedDataContainsSingleRideAndDayPassForEachMode() {
        List<Ticket> tickets = ticketService.findAll();

        assertThat(tickets).hasSize(8);

        Map<TransitMode, List<Ticket>> ticketsByMode = new EnumMap<>(TransitMode.class);
        for (TransitMode mode : TransitMode.values()) {
            ticketsByMode.put(mode, ticketService.findByTransitMode(mode));
        }

        assertThat(ticketsByMode)
                .allSatisfy((mode, modeTickets) -> assertThat(modeTickets)
                        .extracting(Ticket::getTicketType)
                        .containsExactlyInAnyOrder(TicketType.SINGLE_RIDE, TicketType.DAY_PASS));
    }

    @Test
    void filtersTicketsByTransitMode() {
        assertThat(ticketService.findByTransitMode(TransitMode.BUS))
                .extracting(Ticket::getName)
                .containsExactly("Bus Single Ride", "Bus Day Pass");
    }

    @Test
    void findsTicketByIdForCardNavigationTargets() {
        assertThat(ticketService.findById(1L))
                .hasValueSatisfying(ticket -> assertThat(ticket.getName()).isEqualTo("Bus Single Ride"));
    }
}
