package com.example.specdriven.tickets.ui;

import com.example.specdriven.tickets.domain.Ticket;
import com.example.specdriven.tickets.domain.TransitMode;
import com.example.specdriven.tickets.service.TicketCatalog;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Transit Tickets")
public class BrowseTicketsView extends VerticalLayout {
    private final TicketCatalog ticketCatalog;
    private final Map<String, TransitMode> modeByKey = new LinkedHashMap<>();

    private String selectedFilter = "all";
    private final Div filterBar = new Div();
    private final Div grid = new Div();

    public BrowseTicketsView(TicketCatalog ticketCatalog) {
        this.ticketCatalog = ticketCatalog;

        setPadding(false);
        setSpacing(false);
        addClassName("qt-page");

        modeByKey.put("all", null);
        modeByKey.put("bus", TransitMode.BUS);
        modeByKey.put("train", TransitMode.TRAIN);
        modeByKey.put("metro", TransitMode.METRO);
        modeByKey.put("ferry", TransitMode.FERRY);

        Div content = new Div();
        content.addClassName("qt-content");

        content.add(buildHeader());
        content.add(buildFilterBar());
        content.add(buildGrid());

        add(content);
        refresh();
    }

    private Div buildHeader() {
        Div header = new Div();
        header.addClassName("qt-page-header");

        H1 title = new H1("Transit Tickets");
        title.addClassName("qt-page-title");

        Paragraph subtitle = new Paragraph("Find and purchase your ride");
        subtitle.addClassName("qt-page-subtitle");

        header.add(title, subtitle);
        return header;
    }

    private Div buildFilterBar() {
        filterBar.addClassName("qt-filter-bar");
        modeByKey.forEach((key, mode) -> {
            Span tab = new Span(labelForFilter(key, mode));
            tab.addClassName("qt-filter-tab");
            tab.getElement().setAttribute("role", "button");
            tab.getElement().setAttribute("tabindex", "0");
            tab.addClickListener(e -> {
                selectedFilter = key;
                refresh();
            });
            filterBar.add(tab);
        });
        return filterBar;
    }

    private Div buildGrid() {
        grid.addClassName("qt-ticket-grid");
        return grid;
    }

    private void refresh() {
        updateActiveFilterStyles();
        List<Ticket> visible = ticketCatalog.findByMode(Optional.ofNullable(modeByKey.get(selectedFilter)));
        grid.removeAll();
        for (Ticket ticket : visible) {
            grid.add(buildTicketCard(ticket));
        }
    }

    private void updateActiveFilterStyles() {
        filterBar.getChildren().forEach(component -> component.removeClassName("is-active"));
        int index = 0;
        for (String key : modeByKey.keySet()) {
            if (key.equals(selectedFilter)) {
                filterBar.getComponentAt(index).addClassName("is-active");
                break;
            }
            index++;
        }
    }

    private Div buildTicketCard(Ticket ticket) {
        Div card = new Div();
        card.addClassName("qt-ticket-card");
        card.addClassName(ticket.mode().cssClass());
        card.getElement().setAttribute("role", "link");
        card.getElement().setAttribute("tabindex", "0");

        Span emoji = new Span(ticket.mode().emoji());
        emoji.addClassName("qt-ticket-emoji");

        Span name = new Span(ticket.name());
        name.addClassName("qt-ticket-name");

        Div badges = new Div();
        badges.addClassName("qt-badge-row");

        Span modeBadge = new Span(ticket.mode().label());
        modeBadge.addClassNames("qt-badge", "qt-badge--mode");

        Span typeBadge = new Span(ticket.type().label());
        typeBadge.addClassNames("qt-badge", "qt-badge--type");

        badges.add(modeBadge, typeBadge);

        Span price = new Span(formatEuro(ticket.price()));
        price.addClassName("qt-ticket-price");

        card.add(emoji, name, badges, price);
        card.addClickListener(e -> UI.getCurrent().navigate(TicketDetailView.class, ticket.id()));
        return card;
    }

    private static String formatEuro(java.math.BigDecimal amount) {
        if (amount == null) {
            return "€0.00";
        }
        return "€" + amount.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
    }

    private static String labelForFilter(String key, TransitMode mode) {
        if ("all".equals(key)) {
            return "All";
        }
        return mode.label();
    }
}

