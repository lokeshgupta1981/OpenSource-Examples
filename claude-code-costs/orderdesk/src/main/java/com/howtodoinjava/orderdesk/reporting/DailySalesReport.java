package com.howtodoinjava.orderdesk.reporting;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.howtodoinjava.orderdesk.order.Order;
import com.howtodoinjava.orderdesk.order.OrderRepository;
import com.howtodoinjava.orderdesk.order.OrderStatus;

@Component
public class DailySalesReport {

    private final OrderRepository repository;

    public DailySalesReport(OrderRepository repository) {
        this.repository = repository;
    }

    public BigDecimal totalSales() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Order order : repository.findAll()) {
            if (order.status() != OrderStatus.CANCELLED) {
                sum = sum.add(order.total());
            }
        }
        return sum;
    }
}
