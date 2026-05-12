package com.example.specdriven.tickets;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;

@Route("checkout/:ticketId(\\d+)/:quantity(\\d+)")
public class TicketCheckoutView extends VerticalLayout implements BeforeEnterObserver {

    private final TicketService ticketService;
    private final PurchaseService purchaseService;

    public TicketCheckoutView(TicketService ticketService, PurchaseService purchaseService) {
        this.ticketService = ticketService;
        this.purchaseService = purchaseService;
        setPadding(false);
        setSpacing(false);
        setSizeFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String ticketIdStr = event.getRouteParameters().get("ticketId").orElse(null);
        String quantityStr = event.getRouteParameters().get("quantity").orElse(null);
        if (ticketIdStr == null || quantityStr == null) {
            event.forwardTo("");
            return;
        }
        try {
            Long ticketId = Long.parseLong(ticketIdStr);
            int quantity = Integer.parseInt(quantityStr);
            ticketService.findById(ticketId).ifPresentOrElse(
                ticket -> buildView(ticket, quantity),
                () -> event.forwardTo("")
            );
        } catch (NumberFormatException e) {
            event.forwardTo("");
        }
    }

    private void buildView(Ticket ticket, int quantity) {
        removeAll();

        BigDecimal total = ticket.getPrice().multiply(BigDecimal.valueOf(quantity));

        Div page = new Div();
        page.addClassName("page-container");

        // Back link
        Div backLink = new Div();
        backLink.addClassName("back-link");
        backLink.setText("← Back to Details");
        backLink.addClickListener(e -> UI.getCurrent().navigate("ticket/" + ticket.getId()));
        page.add(backLink);

        H2 pageTitle = new H2("Checkout");
        pageTitle.getStyle().set("font-size", "22px").set("font-weight", "800")
                .set("color", "#fafafa").set("margin", "0 0 16px");
        page.add(pageTitle);

        Div layout = new Div();
        layout.addClassName("checkout-layout");

        // Payment form card
        Div paymentCard = new Div();
        paymentCard.addClassName("payment-card");

        Span paymentHeader = new Span("Payment Details");
        paymentHeader.addClassName("section-header");
        paymentCard.add(paymentHeader);

        TextField nameField = new TextField("Cardholder Name");
        nameField.setWidthFull();
        nameField.setPlaceholder("Full name");

        TextField cardField = new TextField("Card Number");
        cardField.setWidthFull();
        cardField.setPlaceholder("1234 5678 9012 3456");
        cardField.setMaxLength(19);

        Div expiryAndCvv = new Div();
        expiryAndCvv.addClassName("payment-row-inline");
        TextField expiryField = new TextField("Expiration (MM/YY)");
        expiryField.setPlaceholder("MM/YY");
        expiryField.setMaxLength(5);
        TextField cvvField = new TextField("CVV");
        cvvField.setPlaceholder("123");
        cvvField.setMaxLength(3);
        expiryAndCvv.add(expiryField, cvvField);

        Button purchaseBtn = new Button("Purchase");
        purchaseBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        purchaseBtn.setWidthFull();
        purchaseBtn.setEnabled(false);

        Runnable validate = () -> {
            String name = nameField.getValue().trim();
            String card = cardField.getValue().replaceAll("\\D", "");
            String expiry = expiryField.getValue().trim();
            String cvv = cvvField.getValue().trim();
            boolean valid = !name.isEmpty()
                    && card.length() == 16
                    && expiry.matches("\\d{2}/\\d{2}")
                    && cvv.matches("\\d{3}");
            purchaseBtn.setEnabled(valid);
        };

        nameField.addValueChangeListener(e -> validate.run());
        cardField.addValueChangeListener(e -> validate.run());
        expiryField.addValueChangeListener(e -> validate.run());
        cvvField.addValueChangeListener(e -> validate.run());

        purchaseBtn.addClickListener(e -> {
            PurchaseOrder order = purchaseService.purchase(
                ticket, quantity, cardField.getValue());
            UI.getCurrent().navigate("confirmation/" + order.getConfirmationCode());
        });

        paymentCard.add(nameField, cardField, expiryAndCvv, purchaseBtn);

        // Order summary card
        Div summaryCard = new Div();
        summaryCard.addClassName("summary-card");
        summaryCard.addClassName("checkout-summary-sticky");

        Span summaryHeader = new Span("Order Summary");
        summaryHeader.addClassName("section-header");

        Div summaryRow = new Div();
        summaryRow.getStyle().set("display", "flex").set("justify-content", "space-between")
                .set("align-items", "flex-start");

        Div summaryLeft = new Div();
        Span summaryName = new Span(ticket.getName());
        summaryName.addClassName("summary-ticket-name");
        Span summarySub = new Span(ViewHelper.modeName(ticket.getTransitMode())
                + " · " + ViewHelper.typeName(ticket.getTicketType())
                + " · Qty: " + quantity);
        summarySub.addClassName("summary-ticket-sub");
        summaryLeft.add(summaryName, summarySub);

        Span summaryTotal = new Span(ViewHelper.formatPrice(total));
        summaryTotal.addClassName("summary-total");

        summaryRow.add(summaryLeft, summaryTotal);
        summaryCard.add(summaryHeader, summaryRow);

        layout.add(paymentCard, summaryCard);
        page.add(layout);
        add(page);
    }
}
