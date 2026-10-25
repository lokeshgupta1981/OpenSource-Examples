package com.howtodoinjava.orderdesk.order;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.howtodoinjava.orderdesk.common.ValidationException;

class OrderValidatorTest {

    private final OrderValidator validator = new OrderValidator();

    private CreateOrderRequest requestWith(OrderLine line) {
        return new CreateOrderRequest("c-1", null, List.of(line));
    }

    @Test
    void acceptsValidOrder() {
        assertDoesNotThrow(() -> validator.validate(
                requestWith(new OrderLine("BOOK", 2, new BigDecimal("12.50")))));
    }

    @Test
    void rejectsMissingCustomer() {
        CreateOrderRequest request = new CreateOrderRequest(" ", null,
                List.of(new OrderLine("BOOK", 1, BigDecimal.TEN)));
        assertThrows(ValidationException.class, () -> validator.validate(request));
    }

    @Test
    void rejectsZeroQuantity() {
        assertThrows(ValidationException.class, () -> validator.validate(
                requestWith(new OrderLine("BOOK", 0, BigDecimal.TEN))));
    }

    @Test
    void rejectsNegativeQuantity() {
        assertThrows(ValidationException.class, () -> validator.validate(
                requestWith(new OrderLine("BOOK", -3, BigDecimal.TEN))));
    }

    @Test
    void rejectsNegativePrice() {
        assertThrows(ValidationException.class, () -> validator.validate(
                requestWith(new OrderLine("BOOK", 1, new BigDecimal("-1.00")))));
    }
}
