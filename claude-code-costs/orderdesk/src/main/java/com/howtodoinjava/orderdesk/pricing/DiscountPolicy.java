package com.howtodoinjava.orderdesk.pricing;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class DiscountPolicy {

    private static final Map<String, BigDecimal> COUPONS = Map.of(
            "WELCOME10", new BigDecimal("0.10"),
            "BULK15", new BigDecimal("0.15"));

    public BigDecimal discountRate(String couponCode) {
        if (couponCode == null) {
            return BigDecimal.ZERO;
        }
        return COUPONS.getOrDefault(couponCode.toUpperCase(), BigDecimal.ZERO);
    }
}
