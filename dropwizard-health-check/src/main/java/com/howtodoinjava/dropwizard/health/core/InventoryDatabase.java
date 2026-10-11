package com.howtodoinjava.dropwizard.health.core;

import java.util.concurrent.atomic.AtomicBoolean;

/** Stands in for a real database connection pool. */
public class InventoryDatabase {

    private final AtomicBoolean reachable = new AtomicBoolean(true);

    public boolean ping() {
        return reachable.get();
    }

    public void setReachable(boolean value) {
        reachable.set(value);
    }
}
