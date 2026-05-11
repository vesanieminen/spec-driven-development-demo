package com.example.specdriven.tickets;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Route("ticket")
@PageTitle("Ticket Details")
public class TicketDetailView extends Div implements HasUrlParameter<Long> {

    private final TicketService ticketService;

    public TicketDetailView(TicketService ticketService) {
        this.ticketService = ticketService;
        addClassName("ticket-app");
    }

    @Override
    public void setParameter(BeforeEvent event, Long ticketId) {
        removeAll();

        Div content = new Div();
        content.addClassName("page-content");

        Anchor backLink = new Anchor("/", "\u2190 Back to Browse");
        backLink.addClassName("back-link");
        content.add(backLink);

        ticketService.findById(ticketId)
                .ifPresentOrElse(
                        ticket -> content.add(createTicketDetail(ticket)),
                        () -> content.add(createMissingTicket()));

        add(content);
    }

    private Div createTicketDetail(Ticket ticket) {
        Div card = new Div();
        card.addClassNames("detail-card", ticket.getTransitMode().getCssClass());

        Span emoji = new Span(ticket.getTransitMode().getEmoji());
        emoji.addClassName("detail-card__emoji");

        H2 title = new H2(ticket.getName());
        title.addClassName("detail-card__title");

        Div badgeRow = new Div();
        badgeRow.addClassName("badge-row");

        Span modeBadge = new Span(ticket.getTransitMode().getDisplayName());
        modeBadge.addClassNames("badge", "mode-badge");

        Span typeBadge = new Span(ticket.getTicketType().getDisplayName());
        typeBadge.addClassNames("badge", "type-badge");
        badgeRow.add(modeBadge, typeBadge);

        Paragraph description = new Paragraph(ticket.getDescription());
        description.addClassName("detail-card__description");

        Div priceRow = new Div();
        priceRow.addClassName("detail-card__price-row");

        Span price = new Span(formatPrice(ticket.getPrice()));
        price.addClassName("detail-card__price");

        Span unit = new Span("per ticket");
        unit.addClassName("detail-card__price-unit");
        priceRow.add(price, unit);

        card.add(emoji, title, badgeRow, description, priceRow);
        return card;
    }

    private Div createMissingTicket() {
        Div card = new Div();
        card.addClassName("detail-card");

        H2 title = new H2("Ticket not found");
        title.addClassName("detail-card__title");

        Paragraph description = new Paragraph("Choose another ticket from the browse page.");
        description.addClassName("detail-card__description");

        card.add(title, description);
        return card;
    }

    private String formatPrice(BigDecimal price) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(price);
    }
}
