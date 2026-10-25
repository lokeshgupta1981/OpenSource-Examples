package com.howtodoinjava.orderdesk.inventory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final Map<String, Integer> reserved = new ConcurrentHashMap<>();

    public void reserve(String sku, int quantity) {
        reserved.merge(sku, quantity, Integer::sum);
        log.debug("Reserved {} of {}; total reserved {}", quantity, sku, reserved.get(sku));
    }

    public int reservedQuantity(String sku) {
        return reserved.getOrDefault(sku, 0);
    }
}
