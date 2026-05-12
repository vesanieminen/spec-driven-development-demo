package com.example.specdriven.tickets;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route("")
public class TicketBrowseView extends Div {

    private final TicketService ticketService;
    private Ticket.TransitMode activeFilter = null;
    private final Div gridContainer = new Div();
    private final List<Div> filterButtons = new ArrayList<>();

    public TicketBrowseView(TicketService ticketService) {
        this.ticketService = ticketService;
        setSizeFull();

        Div page = new Div();
        page.addClassName("page-container");

        Div header = new Div();
        header.addClassName("browse-header");
        H1 title = new H1("Transit Tickets");
        title.addClassName("browse-title");
        Paragraph subtitle = new Paragraph("Find and purchase your ride");
        subtitle.addClassName("browse-subtitle");
        header.add(title, subtitle);

        Div filterBar = new Div();
        filterBar.addClassName("filter-bar");

        String[][] modes = {
            {"All", null},
            {"Bus", "BUS"},
            {"Train", "TRAIN"},
            {"Metro", "METRO"},
            {"Ferry", "FERRY"}
        };

        for (String[] mode : modes) {
            String label = mode[0];
            String modeKey = mode[1];
            Div btn = new Div();
            btn.addClassName("filter-btn");
            btn.setText(label);
            if (modeKey == null) btn.addClassName("active");
            filterButtons.add(btn);
            btn.addClickListener(e -> {
                filterButtons.forEach(b -> b.removeClassName("active"));
                btn.addClassName("active");
                activeFilter = modeKey != null ? Ticket.TransitMode.valueOf(modeKey) : null;
                renderTickets();
            });
            filterBar.add(btn);
        }

        gridContainer.addClassName("ticket-grid");
        renderTickets();

        page.add(header, filterBar, gridContainer);
        add(page);
    }

    private void renderTickets() {
        gridContainer.removeAll();
        List<Ticket> tickets = activeFilter == null
                ? ticketService.findAll()
                : ticketService.findByMode(activeFilter);
        for (Ticket ticket : tickets) {
            gridContainer.add(buildCard(ticket));
        }
    }

    private Div buildCard(Ticket ticket) {
        Div card = new Div();
        card.addClassNames("ticket-card", "mode-" + ViewHelper.modeClass(ticket.getTransitMode()));

        Span icon = new Span(ViewHelper.modeEmoji(ticket.getTransitMode()));
        icon.addClassName("ticket-card-icon");

        Span name = new Span(ticket.getName());
        name.addClassName("ticket-card-name");

        Div badges = ViewHelper.badgeRow(ticket);

        Span price = new Span(ViewHelper.formatPrice(ticket.getPrice()));
        price.addClassNames("ticket-card-price", "mode-" + ViewHelper.modeClass(ticket.getTransitMode()));

        card.add(icon, name, badges, price);
        card.addClickListener(e -> UI.getCurrent().navigate("ticket/" + ticket.getId()));
        return card;
    }
}
