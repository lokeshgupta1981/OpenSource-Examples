package com.howtodoinjava.orderdesk.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

@Component
public class TaxCalculator {

    static final BigDecimal TAX_RATE = new BigDecimal("0.08");

    public BigDecimal tax(BigDecimal amount) {
        return amount.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
    }
}
