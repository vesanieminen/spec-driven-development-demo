package com.example.specdriven.tickets;

public enum TicketType {
    SINGLE_RIDE("Single Ride"),
    DAY_PASS("Day Pass");

    private final String label;

    TicketType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
