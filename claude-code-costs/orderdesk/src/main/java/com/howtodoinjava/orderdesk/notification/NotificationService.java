package com.howtodoinjava.orderdesk.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.howtodoinjava.orderdesk.order.Order;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void orderPlaced(Order order) {
        log.info("Sending confirmation for order {} to customer {}", order.id(), order.customerId());
        order.lines().forEach(line -> log.debug("  line {} x {}", line.sku(), line.quantity()));
    }
}
