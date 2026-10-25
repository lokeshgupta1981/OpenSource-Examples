package com.howtodoinjava.orderdesk.order;

import java.util.List;

public record CreateOrderRequest(String customerId, String couponCode, List<OrderLine> lines) {
}
