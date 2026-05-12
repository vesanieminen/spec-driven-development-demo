package com.example.specdriven.tickets;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

class ViewHelper {

    static String modeEmoji(Ticket.TransitMode mode) {
        return switch (mode) {
            case BUS -> "🚌";
            case TRAIN -> "🚆";
            case METRO -> "🚇";
            case FERRY -> "⛴️";
        };
    }

    static String modeName(Ticket.TransitMode mode) {
        return switch (mode) {
            case BUS -> "Bus";
            case TRAIN -> "Train";
            case METRO -> "Metro";
            case FERRY -> "Ferry";
        };
    }

    static String typeName(Ticket.TicketType type) {
        return switch (type) {
            case SINGLE_RIDE -> "Single Ride";
            case DAY_PASS -> "Day Pass";
        };
    }

    static String modeClass(Ticket.TransitMode mode) {
        return mode.name().toLowerCase();
    }

    static String formatPrice(BigDecimal price) {
        NumberFormat fmt = NumberFormat.getCurrencyInstance(Locale.US);
        return fmt.format(price);
    }

    static String formatDateTime(LocalDateTime dt) {
        return dt.format(DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a"));
    }

    static Div badgeRow(Ticket ticket) {
        Div row = new Div();
        Span modeBadge = new Span(modeName(ticket.getTransitMode()));
        modeBadge.addClassNames("badge", "badge-mode", modeClass(ticket.getTransitMode()));
        Span typeBadge = new Span(typeName(ticket.getTicketType()));
        typeBadge.addClassNames("badge", "badge-type");
        row.add(modeBadge, typeBadge);
        return row;
    }

    static Div confirmationCodeBlock(String code) {
        Div box = new Div();
        box.addClassName("confirmation-code-box");
        Span label = new Span("Confirmation Code");
        label.addClassName("confirmation-code-label");
        Span value = new Span(code);
        value.addClassName("confirmation-code-value");
        box.add(label, value);
        return box;
    }

    static Div detailsList(PurchaseOrder order) {
        Div list = new Div();
        list.addClassName("details-list");
        Ticket ticket = order.getTicket();
        list.add(
            detailRow("Ticket", ticket.getName()),
            detailRow("Mode", modeName(ticket.getTransitMode()) + " · " + typeName(ticket.getTicketType())),
            detailRow("Quantity", String.valueOf(order.getQuantity())),
            detailRow("Total", formatPrice(order.getTotalPrice())),
            detailRow("Card", "**** " + order.getCardLastFour()),
            detailRow("Purchased", formatDateTime(order.getPurchasedAt()))
        );
        return list;
    }

    static Component detailRow(String label, String value) {
        Div row = new Div();
        row.addClassName("details-row");
        Span lbl = new Span(label);
        lbl.addClassName("details-row-label");
        Span val = new Span(value);
        val.addClassName("details-row-value");
        row.add(lbl, val);
        return row;
    }
}
