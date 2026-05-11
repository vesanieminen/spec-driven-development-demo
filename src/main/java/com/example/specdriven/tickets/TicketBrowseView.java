package com.example.specdriven.tickets;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.text.NumberFormat;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Route("")
@PageTitle("Transit Tickets")
public class TicketBrowseView extends Div {

    private static final NumberFormat PRICE_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);

    private final TicketService ticketService;
    private final Div grid = new Div();
    private final Map<TransitMode, NativeButton> modeButtons = new EnumMap<>(TransitMode.class);
    private NativeButton allButton;
    private NativeButton activeButton;

    public TicketBrowseView(TicketService ticketService) {
        this.ticketService = ticketService;
        addClassName("browse-page");

        add(buildDesktopNav());

        Div container = new Div();
        container.addClassName("page-container");

        container.add(buildHeader());
        container.add(buildFilterBar());

        grid.addClassName("ticket-grid");
        container.add(grid);

        add(container);

        selectFilter(allButton, null);
    }

    private Div buildDesktopNav() {
        Div nav = new Div();
        nav.addClassName("desktop-nav");

        Span brand = new Span("QUICK TRANSIT");
        brand.addClassName("desktop-nav-brand");
        nav.add(brand);

        Div links = new Div();
        links.addClassName("desktop-nav-links");

        Anchor browse = new Anchor("/", "Browse");
        browse.addClassNames("desktop-nav-link", "active");
        browse.getElement().setAttribute("router-link", true);
        links.add(browse);

        nav.add(links);
        return nav;
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

        allButton = createFilterButton("All", () -> selectFilter(allButton, null));
        bar.add(allButton);

        for (TransitMode mode : TransitMode.values()) {
            NativeButton button = createFilterButton(mode.getLabel(), () -> selectFilter(modeButtons.get(mode), mode));
            modeButtons.put(mode, button);
            bar.add(button);
        }
        return bar;
    }

    private NativeButton createFilterButton(String label, Runnable onClick) {
        NativeButton button = new NativeButton(label);
        button.addClassName("filter-btn");
        button.addClickListener(e -> onClick.run());
        return button;
    }

    private void selectFilter(NativeButton button, TransitMode mode) {
        if (activeButton != null) {
            activeButton.removeClassName("active");
        }
        activeButton = button;
        activeButton.addClassName("active");
        renderTickets(mode);
    }

    private void renderTickets(TransitMode mode) {
        List<Ticket> tickets = ticketService.findByMode(mode);
        grid.removeAll();
        tickets.forEach(t -> grid.add(buildCard(t)));
    }

    private Div buildCard(Ticket ticket) {
        String modeClass = ticket.getTransitMode().getCssClass();

        Div card = new Div();
        card.addClassNames("ticket-card", modeClass);
        card.getElement().setAttribute("role", "button");
        card.getElement().setAttribute("tabindex", "0");
        card.getElement().setAttribute("aria-label",
                ticket.getName() + ", " + PRICE_FORMAT.format(ticket.getPrice()));
        card.addClickListener(e -> UI.getCurrent().navigate("ticket/" + ticket.getId()));

        Span icon = new Span(ticket.getTransitMode().getEmoji());
        icon.addClassName("ticket-card-icon");

        Div name = new Div();
        name.setText(ticket.getName());
        name.addClassName("ticket-card-name");

        Div badges = new Div();
        badges.addClassName("ticket-card-badges");

        Span modeBadge = new Span(ticket.getTransitMode().getLabel());
        modeBadge.addClassNames("badge", "badge-mode", modeClass);

        Span typeBadge = new Span(ticket.getTicketType().getLabel());
        typeBadge.addClassNames("badge", "badge-type");

        badges.add(modeBadge, typeBadge);

        Div price = new Div();
        price.setText(PRICE_FORMAT.format(ticket.getPrice()));
        price.addClassNames("ticket-card-price", modeClass);

        card.add(icon, name, badges, price);
        return card;
    }
}
