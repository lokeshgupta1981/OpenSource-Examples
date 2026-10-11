package com.howtodoinjava.dropwizard.health;

import com.codahale.metrics.health.HealthCheck;
import com.howtodoinjava.dropwizard.health.core.InventoryDatabase;
import com.howtodoinjava.dropwizard.health.health.DatabaseHealthCheck;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseHealthCheckTest {

    private final InventoryDatabase database = new InventoryDatabase();
    private final DatabaseHealthCheck healthCheck = new DatabaseHealthCheck(database);

    @Test
    void healthyWhenDatabaseAnswers() {
        HealthCheck.Result result = healthCheck.execute();

        assertTrue(result.isHealthy());
    }

    @Test
    void unhealthyWhenDatabaseIsDown() {
        database.setReachable(false);

        HealthCheck.Result result = healthCheck.execute();

        assertFalse(result.isHealthy());
        assertEquals("Cannot reach the inventory database", result.getMessage());
    }
}
