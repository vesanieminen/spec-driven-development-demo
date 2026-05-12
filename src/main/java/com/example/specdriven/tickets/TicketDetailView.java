package com.example.specdriven.tickets;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;

@Route("ticket/:ticketId(\\d+)")
public class TicketDetailView extends VerticalLayout implements BeforeEnterObserver {

    private final TicketService ticketService;

    public TicketDetailView(TicketService ticketService) {
        this.ticketService = ticketService;
        setPadding(false);
        setSpacing(false);
        setSizeFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String ticketIdStr = event.getRouteParameters().get("ticketId").orElse(null);
        if (ticketIdStr == null) {
            event.forwardTo("");
            return;
        }
        Long ticketId;
        try {
            ticketId = Long.parseLong(ticketIdStr);
        } catch (NumberFormatException e) {
            event.forwardTo("");
            return;
        }
        ticketService.findById(ticketId).ifPresentOrElse(
            ticket -> buildView(ticket),
            () -> event.forwardTo("")
        );
    }

    private void buildView(Ticket ticket) {
        removeAll();

        Div page = new Div();
        page.addClassName("page-container");

        // Back link
        Div backLink = new Div();
        backLink.addClassName("back-link");
        backLink.setText("← Back to Browse");
        backLink.addClickListener(e -> UI.getCurrent().navigate(""));
        page.add(backLink);

        // Layout
        Div layout = new Div();
        layout.addClassName("detail-layout");

        // Left: ticket info card
        Div infoCard = new Div();
        infoCard.addClassName("detail-card");

        Span icon = new Span(ViewHelper.modeEmoji(ticket.getTransitMode()));
        icon.getStyle().set("font-size", "32px").set("display", "block").set("margin-bottom", "12px");

        Span name = new Span(ticket.getName());
        name.addClassName("detail-title");

        Div badges = ViewHelper.badgeRow(ticket);

        Span description = new Span(ticket.getDescription());
        description.addClassName("detail-description");

        Div priceRow = new Div();
        Span price = new Span(ViewHelper.formatPrice(ticket.getPrice()));
        price.addClassNames("detail-price", "mode-" + ViewHelper.modeClass(ticket.getTransitMode()));
        Span priceUnit = new Span(" per ticket");
        priceUnit.addClassName("detail-price-unit");
        priceRow.add(price, priceUnit);

        infoCard.add(icon, name, badges, description, priceRow);

        // Right: purchase panel
        Div purchasePanel = new Div();
        purchasePanel.addClassName("purchase-panel");

        Span qtyLabel = new Span("Quantity");
        qtyLabel.addClassName("quantity-label");

        IntegerField qty = new IntegerField();
        qty.setMin(1);
        qty.setMax(5);
        qty.setValue(1);
        qty.setStepButtonsVisible(true);
        qty.addClassName("quantity-stepper");

        Div subtotalBox = new Div();
        subtotalBox.addClassName("subtotal-box");
        Span subtotalLabel = new Span("Subtotal");
        subtotalLabel.addClassName("subtotal-label");
        Span subtotalValue = new Span(ViewHelper.formatPrice(ticket.getPrice()));
        subtotalValue.addClassName("subtotal-value");
        subtotalBox.add(subtotalLabel, subtotalValue);

        qty.addValueChangeListener(e -> {
            int q = e.getValue() != null ? e.getValue() : 1;
            BigDecimal subtotal = ticket.getPrice().multiply(BigDecimal.valueOf(q));
            subtotalValue.setText(ViewHelper.formatPrice(subtotal));
        });

        Button cta = new Button("Continue to Checkout");
        cta.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cta.setWidthFull();
        cta.addClickListener(e -> {
            int q = qty.getValue() != null ? qty.getValue() : 1;
            UI.getCurrent().navigate("checkout/" + ticket.getId() + "/" + q);
        });

        purchasePanel.add(qtyLabel, qty, subtotalBox, cta);
        layout.add(infoCard, purchasePanel);
        page.add(layout);
        add(page);
    }
}
