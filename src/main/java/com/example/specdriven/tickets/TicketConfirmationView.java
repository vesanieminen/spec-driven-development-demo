package com.example.specdriven.tickets;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import java.util.UUID;

@Route("confirmation/:confirmationCode")
public class TicketConfirmationView extends VerticalLayout implements BeforeEnterObserver {

    private final PurchaseService purchaseService;

    public TicketConfirmationView(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
        setPadding(false);
        setSpacing(false);
        setSizeFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String codeStr = event.getRouteParameters().get("confirmationCode").orElse(null);
        if (codeStr == null) {
            event.forwardTo("");
            return;
        }
        UUID code;
        try {
            code = UUID.fromString(codeStr);
        } catch (IllegalArgumentException e) {
            event.forwardTo("");
            return;
        }
        purchaseService.findByConfirmationCode(code).ifPresentOrElse(
            order -> buildView(order),
            () -> event.forwardTo("")
        );
    }

    private void buildView(PurchaseOrder order) {
        removeAll();

        Div page = new Div();
        page.addClassName("page-container");

        Div container = new Div();
        container.addClassName("confirmation-container");

        // Success header
        Div successHeader = new Div();
        successHeader.addClassName("success-header");

        Div iconCircle = new Div();
        iconCircle.addClassName("success-icon");
        iconCircle.setText("✓");

        Span heading = new Span("Purchase Successful!");
        heading.addClassName("success-heading");

        Span subtitle = new Span("Show this code when boarding");
        subtitle.addClassName("success-subtitle");

        successHeader.add(iconCircle, heading, subtitle);

        // Confirmation code block
        Div codeBlock = ViewHelper.confirmationCodeBlock(order.getConfirmationCode().toString());

        // Details list
        Div details = ViewHelper.detailsList(order);

        // Buy another ticket button
        Div buyBtn = new Div();
        buyBtn.addClassName("btn-secondary");
        buyBtn.setText("Buy Another Ticket");
        buyBtn.addClickListener(e -> UI.getCurrent().navigate(""));

        container.add(successHeader, codeBlock, details, buyBtn);
        page.add(container);
        add(page);
    }
}
