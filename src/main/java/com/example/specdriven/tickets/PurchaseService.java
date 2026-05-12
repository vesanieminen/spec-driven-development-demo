package com.example.specdriven.tickets;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PurchaseService {

    private final PurchaseRepository repository;

    public PurchaseService(PurchaseRepository repository) {
        this.repository = repository;
    }

    public PurchaseOrder purchase(Ticket ticket, int quantity, String cardNumber) {
        String digits = cardNumber.replaceAll("\\D", "");
        String lastFour = digits.substring(Math.max(0, digits.length() - 4));

        PurchaseOrder order = new PurchaseOrder();
        order.setConfirmationCode(UUID.randomUUID());
        order.setQuantity(quantity);
        order.setTotalPrice(ticket.getPrice().multiply(BigDecimal.valueOf(quantity)));
        order.setCardLastFour(lastFour);
        order.setPurchasedAt(LocalDateTime.now());
        order.setTicket(ticket);
        return repository.save(order);
    }

    public Optional<PurchaseOrder> findByConfirmationCode(UUID code) {
        return repository.findByConfirmationCode(code);
    }
}
