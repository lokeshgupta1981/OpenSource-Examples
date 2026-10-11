package com.howtodoinjava.dropwizard.health.health;

import com.codahale.metrics.health.HealthCheck;
import com.howtodoinjava.dropwizard.health.core.InventoryDatabase;

public class DatabaseHealthCheck extends HealthCheck {

    private final InventoryDatabase database;

    public DatabaseHealthCheck(InventoryDatabase database) {
        this.database = database;
    }

    @Override
    protected Result check() {
        if (database.ping()) {
            return Result.healthy();
        }
        return Result.unhealthy("Cannot reach the inventory database");
    }
}
