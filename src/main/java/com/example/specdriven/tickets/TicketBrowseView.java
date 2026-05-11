package com.example.specdriven.tickets;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Route("")
@PageTitle("Transit Tickets")
public class TicketBrowseView extends Div {

    private final TicketService ticketService;
    private final Div ticketGrid = new Div();
    private final NativeButton allButton = new NativeButton("All");
    private final Map<TransitMode, NativeButton> modeButtons = new EnumMap<>(TransitMode.class);
    private TransitMode selectedMode;

    public TicketBrowseView(TicketService ticketService) {
        this.ticketService = ticketService;
        addClassName("ticket-app");

        Div content = new Div();
        content.addClassName("page-content");
        ticketGrid.addClassName("ticket-grid");

        content.add(createPageHeader(), createFilterBar(), ticketGrid);
        add(createDesktopNav(), content);

        selectMode(null);
    }

    private Div createDesktopNav() {
        Div nav = new Div();
        nav.addClassName("desktop-nav");

        Div inner = new Div();
        inner.addClassName("desktop-nav__inner");

        Span brand = new Span("QUICK TRANSIT");
        brand.addClassName("desktop-nav__brand");

        Div links = new Div();
        links.addClassName("desktop-nav__links");

        Anchor tickets = new Anchor("/", "Tickets");
        tickets.addClassNames("desktop-nav__link", "active");
        tickets.getElement().setAttribute("aria-current", "page");

        Anchor lookup = new Anchor("/lookup", "Lookup");
        lookup.addClassName("desktop-nav__link");

        links.add(tickets, lookup);
        inner.add(brand, links);
        nav.add(inner);
        return nav;
    }

    private Div createPageHeader() {
        Div header = new Div();
        header.addClassName("page-header");

        H1 title = new H1("Transit Tickets");
        title.addClassName("page-title");

        Paragraph subtitle = new Paragraph("Find and purchase your ride");
        subtitle.addClassName("page-subtitle");

        header.add(title, subtitle);
        return header;
    }

    private Div createFilterBar() {
        Div filterBar = new Div();
        filterBar.addClassName("filter-bar");

        allButton.addClassName("filter-tab");
        allButton.getElement().setAttribute("type", "button");
        allButton.getElement().setAttribute("aria-label", "Show all tickets");
        allButton.addClickListener(event -> selectMode(null));
        filterBar.add(allButton);

        for (TransitMode mode : TransitMode.values()) {
            NativeButton button = new NativeButton(mode.getDisplayName());
            button.addClassName("filter-tab");
            button.getElement().setAttribute("type", "button");
            button.getElement().setAttribute("aria-label", "Show " + mode.getDisplayName() + " tickets");
            button.addClickListener(event -> selectMode(mode));
            modeButtons.put(mode, button);
            filterBar.add(button);
        }

        return filterBar;
    }

    private void selectMode(TransitMode mode) {
        selectedMode = mode;
        updateFilterState();
        renderTickets();
    }

    private void updateFilterState() {
        allButton.getElement().getClassList().set("active", selectedMode == null);
        allButton.getElement().setAttribute("aria-pressed", String.valueOf(selectedMode == null));

        for (Map.Entry<TransitMode, NativeButton> entry : modeButtons.entrySet()) {
            boolean active = entry.getKey() == selectedMode;
            NativeButton button = entry.getValue();
            button.getElement().getClassList().set("active", active);
            button.getElement().setAttribute("aria-pressed", String.valueOf(active));
        }
    }

    private void renderTickets() {
        ticketGrid.removeAll();

        List<Ticket> tickets = selectedMode == null
                ? ticketService.findAll()
                : ticketService.findByTransitMode(selectedMode);
        tickets.forEach(ticket -> ticketGrid.add(createTicketCard(ticket)));
    }

    private Anchor createTicketCard(Ticket ticket) {
        Anchor card = new Anchor("/ticket/" + ticket.getId(), "");
        card.addClassNames("ticket-card", ticket.getTransitMode().getCssClass());
        card.getElement().setAttribute("aria-label", "View " + ticket.getName());

        Span emoji = new Span(ticket.getTransitMode().getEmoji());
        emoji.addClassName("ticket-card__emoji");

        Span name = new Span(ticket.getName());
        name.addClassName("ticket-card__name");

        Div badgeRow = new Div();
        badgeRow.addClassName("badge-row");

        Span modeBadge = new Span(ticket.getTransitMode().getDisplayName());
        modeBadge.addClassNames("badge", "mode-badge");

        Span typeBadge = new Span(ticket.getTicketType().getDisplayName());
        typeBadge.addClassNames("badge", "type-badge");

        badgeRow.add(modeBadge, typeBadge);

        Span price = new Span(formatPrice(ticket.getPrice()));
        price.addClassName("ticket-card__price");

        card.add(emoji, name, badgeRow, price);
        return card;
    }

    private String formatPrice(BigDecimal price) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(price);
    }
}
