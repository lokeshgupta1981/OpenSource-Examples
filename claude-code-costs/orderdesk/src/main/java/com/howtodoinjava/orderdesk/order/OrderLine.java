package com.howtodoinjava.orderdesk.order;

import java.math.BigDecimal;

public record OrderLine(String sku, int quantity, BigDecimal unitPrice) {
}
