package com.howtodoinjava.orderdesk.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Component;

import com.howtodoinjava.orderdesk.order.OrderLine;

@Component
public class PriceCalculator {

    private final DiscountPolicy discountPolicy;
    private final TaxCalculator taxCalculator;

    public PriceCalculator(DiscountPolicy discountPolicy, TaxCalculator taxCalculator) {
        this.discountPolicy = discountPolicy;
        this.taxCalculator = taxCalculator;
    }

    public BigDecimal total(List<OrderLine> lines, String couponCode) {
        BigDecimal subtotal = lines.stream()
                .map(line -> line.unitPrice().multiply(BigDecimal.valueOf(line.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal discount = subtotal.multiply(discountPolicy.discountRate(couponCode));
        BigDecimal net = subtotal.subtract(discount).setScale(2, RoundingMode.HALF_UP);
        return net.add(taxCalculator.tax(net));
    }
}
