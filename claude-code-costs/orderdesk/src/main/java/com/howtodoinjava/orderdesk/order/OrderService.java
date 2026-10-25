package com.howtodoinjava.orderdesk.order;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.howtodoinjava.orderdesk.inventory.InventoryService;
import com.howtodoinjava.orderdesk.notification.NotificationService;
import com.howtodoinjava.orderdesk.pricing.PriceCalculator;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderValidator validator;
    private final PriceCalculator priceCalculator;
    private final InventoryService inventory;
    private final NotificationService notifications;
    private final OrderRepository repository;

    public OrderService(OrderValidator validator, PriceCalculator priceCalculator,
                        InventoryService inventory, NotificationService notifications,
                        OrderRepository repository) {
        this.validator = validator;
        this.priceCalculator = priceCalculator;
        this.inventory = inventory;
        this.notifications = notifications;
        this.repository = repository;
    }

    public Order placeOrder(CreateOrderRequest request) {
        log.debug("Placing order for customer {}", request.customerId());
        validator.validate(request);
        request.lines().forEach(line -> inventory.reserve(line.sku(), line.quantity()));
        BigDecimal total = priceCalculator.total(request.lines(), request.couponCode());
        Order order = new Order(repository.nextId(), request.customerId(), request.lines(),
                total, OrderStatus.NEW);
        repository.save(order);
        notifications.orderPlaced(order);
        log.debug("Order {} placed with total {}", order.id(), total);
        return order;
    }

    public Optional<Order> findOrder(long id) {
        return repository.findById(id);
    }
}
