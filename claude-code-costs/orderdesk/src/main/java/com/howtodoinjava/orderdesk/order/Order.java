package com.howtodoinjava.orderdesk.order;

import java.math.BigDecimal;
import java.util.List;

public record Order(long id, String customerId, List<OrderLine> lines,
                    BigDecimal total, OrderStatus status) {
}
