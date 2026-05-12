package com.example.specdriven.domain;

public enum TicketType {
    SINGLE_RIDE("Single Ride"),
    DAY_PASS("Day Pass");

    public final String displayName;

    TicketType(String displayName) {
        this.displayName = displayName;
    }
}
