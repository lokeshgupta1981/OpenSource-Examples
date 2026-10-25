package com.howtodoinjava.orderdesk.order;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.howtodoinjava.orderdesk.common.ValidationException;

@Component
public class OrderValidator {

    static final int MAX_LINES = 50;

    public void validate(CreateOrderRequest request) {
        if (request.customerId() == null || request.customerId().isBlank()) {
            throw new ValidationException("customerId is required");
        }
        if (request.lines() == null || request.lines().isEmpty()) {
            throw new ValidationException("an order needs at least one line");
        }
        if (request.lines().size() > MAX_LINES) {
            throw new ValidationException("an order can have at most " + MAX_LINES + " lines");
        }
        for (OrderLine line : request.lines()) {
            validateLine(line);
        }
    }

    private void validateLine(OrderLine line) {
        if (line.sku() == null || line.sku().isBlank()) {
            throw new ValidationException("sku is required");
        }
        if (line.quantity() == 0) {
            throw new ValidationException("quantity must be positive for " + line.sku());
        }
        if (line.unitPrice() == null || line.unitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("unitPrice must not be negative for " + line.sku());
        }
    }
}
