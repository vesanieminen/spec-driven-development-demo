package com.example.specdriven.tickets.ui;

import com.example.specdriven.tickets.domain.Ticket;
import com.example.specdriven.tickets.service.TicketCatalog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "ticket", layout = MainLayout.class)
@PageTitle("Ticket")
public class TicketDetailView extends VerticalLayout implements HasUrlParameter<String> {
    private final TicketCatalog ticketCatalog;
    private final Div content = new Div();

    public TicketDetailView(TicketCatalog ticketCatalog) {
        this.ticketCatalog = ticketCatalog;
        setPadding(false);
        setSpacing(false);
        addClassName("qt-page");

        content.addClassName("qt-content");
        add(content);
    }

    @Override
    public void setParameter(BeforeEvent event, String ticketId) {
        content.removeAll();

        Anchor back = new Anchor("/", "\u2190 Back to Browse");
        back.addClassName("qt-back-link");

        content.add(back);

        Ticket ticket = ticketCatalog.findById(ticketId).orElse(null);
        if (ticket == null) {
            content.add(new H2("Ticket not found"));
            content.add(new Paragraph("The selected ticket does not exist."));
            return;
        }

        // Minimal placeholder view so UC-001 card navigation works end-to-end.
        H2 title = new H2(ticket.name());
        title.addClassName("qt-detail-title");
        Paragraph meta = new Paragraph(ticket.mode().label() + " · " + ticket.type().label());
        meta.addClassName("qt-detail-meta");
        content.add(title, meta);
    }
}

