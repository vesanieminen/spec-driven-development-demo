package com.example.specdriven.tickets;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.UUID;

@Route("lookup")
public class TicketLookupView extends VerticalLayout {

    public TicketLookupView(PurchaseService purchaseService) {
        setPadding(false);
        setSpacing(false);
        setSizeFull();

        Div page = new Div();
        page.addClassName("page-container");

        Div container = new Div();
        container.addClassName("lookup-container");

        Span title = new Span("Look Up Purchase");
        title.addClassName("lookup-title");

        Span subtitle = new Span("Enter your confirmation code to view ticket details");
        subtitle.addClassName("lookup-subtitle");

        // Input area
        Div inputArea = new Div();
        inputArea.getStyle().set("margin-bottom", "16px");

        TextField codeInput = new TextField();
        codeInput.setWidthFull();
        codeInput.setPlaceholder("e.g. 550e8400-e29b-41d4-a716-446655440000");

        Span errorMsg = new Span();
        errorMsg.addClassName("error-message");
        errorMsg.setVisible(false);

        Div lookupBtn = new Div();
        lookupBtn.addClassName("btn-primary");
        lookupBtn.setText("Look Up");
        lookupBtn.getStyle().set("margin-top", "12px");

        inputArea.add(codeInput, errorMsg, lookupBtn);

        // Disable button when empty
        codeInput.addValueChangeListener(e ->
            lookupBtn.getStyle().set("opacity", e.getValue().trim().isEmpty() ? "0.4" : "1")
        );
        lookupBtn.getStyle().set("opacity", "0.4");

        // Result area (hidden initially)
        Div resultArea = new Div();
        resultArea.setVisible(false);

        lookupBtn.addClickListener(e -> {
            String input = codeInput.getValue().trim();
            if (input.isEmpty()) return;

            errorMsg.setVisible(false);
            resultArea.setVisible(false);

            UUID code;
            try {
                code = UUID.fromString(input);
            } catch (IllegalArgumentException ex) {
                errorMsg.setText("Please enter a valid confirmation code");
                errorMsg.setVisible(true);
                return;
            }

            purchaseService.findByConfirmationCode(code).ifPresentOrElse(
                order -> showResult(resultArea, inputArea, order),
                () -> {
                    errorMsg.setText("No purchase found for this confirmation code");
                    errorMsg.setVisible(true);
                }
            );
        });

        container.add(title, subtitle, inputArea, resultArea);
        page.add(container);
        add(page);
    }

    private void showResult(Div resultArea, Div inputArea, PurchaseOrder order) {
        resultArea.removeAll();

        Div codeBlock = ViewHelper.confirmationCodeBlock(order.getConfirmationCode().toString());
        Div details = ViewHelper.detailsList(order);

        Div buyBtn = new Div();
        buyBtn.addClassName("btn-secondary");
        buyBtn.setText("Buy Another Ticket");
        buyBtn.getStyle().set("margin-bottom", "8px");
        buyBtn.addClickListener(e -> UI.getCurrent().navigate(""));

        Div lookupAnotherBtn = new Div();
        lookupAnotherBtn.addClassName("btn-secondary");
        lookupAnotherBtn.setText("Look Up Another");
        lookupAnotherBtn.addClickListener(e -> {
            resultArea.setVisible(false);
            inputArea.setVisible(true);
        });

        resultArea.add(codeBlock, details, buyBtn, lookupAnotherBtn);
        resultArea.setVisible(true);
        inputArea.setVisible(false);
    }
}
