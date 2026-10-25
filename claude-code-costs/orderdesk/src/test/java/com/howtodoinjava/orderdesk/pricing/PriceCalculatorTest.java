package com.howtodoinjava.orderdesk.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.howtodoinjava.orderdesk.order.OrderLine;

class PriceCalculatorTest {

    private final PriceCalculator calculator =
            new PriceCalculator(new DiscountPolicy(), new TaxCalculator());

    @Test
    void addsTaxToSubtotal() {
        BigDecimal total = calculator.total(
                List.of(new OrderLine("BOOK", 2, new BigDecimal("10.00"))), null);
        assertEquals(new BigDecimal("21.60"), total);
    }

    @Test
    void appliesCouponBeforeTax() {
        BigDecimal total = calculator.total(
                List.of(new OrderLine("BOOK", 1, new BigDecimal("100.00"))), "welcome10");
        assertEquals(new BigDecimal("97.20"), total);
    }
}
