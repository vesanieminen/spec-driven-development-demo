package com.example.specdriven.views;

import com.example.specdriven.domain.Ticket;
import com.example.specdriven.domain.TransitMode;
import com.example.specdriven.service.TicketService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route("")
@PageTitle("Quick Transit")
public class BrowseView extends VerticalLayout {

    private final TicketService ticketService;
    private final Div grid = new Div();
    private TransitMode activeMode = null;

    public BrowseView(TicketService ticketService) {
        this.ticketService = ticketService;
        setPadding(false);
        setSpacing(false);
        addClassName("browse-page");

        Div pageContainer = new Div();
        pageContainer.addClassName("page-container");

        pageContainer.add(buildHeader());
        pageContainer.add(buildFilterBar());

        grid.addClassName("ticket-grid");
        pageContainer.add(grid);

        add(pageContainer);
        renderGrid(ticketService.findAll());
    }

    private Div buildHeader() {
        Div header = new Div();
        header.addClassName("browse-header");

        H1 title = new H1("Transit Tickets");
        title.addClassName("browse-title");

        Paragraph subtitle = new Paragraph("Find and purchase your ride");
        subtitle.addClassName("browse-subtitle");

        header.add(title, subtitle);
        return header;
    }

    private Div buildFilterBar() {
        Div bar = new Div();
        bar.addClassName("filter-bar");

        bar.add(filterButton("All", null, bar));
        for (TransitMode mode : TransitMode.values()) {
            bar.add(filterButton(mode.displayName(), mode, bar));
        }

        return bar;
    }


    private NativeButton filterButton(String label, TransitMode mode, Div bar) {
        NativeButton btn = new NativeButton(label);
        btn.addClassName("filter-btn");
        if (mode == null) {
            btn.addClassName("active");
        }
        btn.addClickListener(e -> {
            activeMode = mode;
            bar.getChildren()
                .filter(c -> c instanceof NativeButton)
                .map(c -> (NativeButton) c)
                .forEach(b -> b.removeClassName("active"));
            btn.addClassName("active");
            renderGrid(mode == null ? ticketService.findAll() : ticketService.findByMode(mode));
        });
        return btn;
    }

    private void renderGrid(List<Ticket> tickets) {
        grid.removeAll();
        tickets.forEach(ticket -> grid.add(buildTicketCard(ticket)));
    }

    private Div buildTicketCard(Ticket ticket) {
        Div card = new Div();
        card.addClassName("ticket-card");
        card.addClassName("mode-" + ticket.getTransitMode().cssClass);

        Div icon = new Div();
        icon.addClassName("ticket-card-icon");
        icon.setText(ticket.getTransitMode().emoji);

        Div name = new Div();
        name.addClassName("ticket-card-name");
        name.setText(ticket.getName());

        Div badges = new Div();
        badges.addClassName("ticket-card-badges");

        Span modeBadge = new Span(ticket.getTransitMode().displayName());
        modeBadge.addClassNames("badge", "badge-mode", ticket.getTransitMode().cssClass);

        Span typeBadge = new Span(ticket.getTicketType().displayName);
        typeBadge.addClassNames("badge", "badge-type");

        badges.add(modeBadge, typeBadge);

        Div price = new Div();
        price.addClassName("ticket-card-price");
        price.addClassName("mode-" + ticket.getTransitMode().cssClass);
        price.setText("$" + ticket.getPrice());

        card.add(icon, name, badges, price);

        card.getElement().addEventListener("click", e ->
            UI.getCurrent().navigate("ticket/" + ticket.getId())
        );

        return card;
    }
}
