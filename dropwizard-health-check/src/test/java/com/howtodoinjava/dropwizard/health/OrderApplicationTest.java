package com.howtodoinjava.dropwizard.health;

import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DropwizardExtensionsSupport.class)
class OrderApplicationTest {

    static final DropwizardAppExtension<OrderConfiguration> APP = new DropwizardAppExtension<>(
            OrderApplication.class,
            ResourceHelpers.resourceFilePath("test-config.yml"),
            ConfigOverride.config("paymentGatewayUrl", "http://127.0.0.1:1/status"));

    @Test
    void adminHealthCheckFailsWhenGatewayIsDown() {
        Response response = APP.client()
                .target("http://localhost:%d/healthcheck".formatted(APP.getAdminPort()))
                .request()
                .get();

        assertEquals(500, response.getStatus());
        assertTrue(response.readEntity(String.class).contains("Payment gateway unreachable"));
    }

    @Test
    void statusTurns503WhenDatabaseGoesDown() {
        APP.client()
                .target("http://localhost:%d/tasks/database-outage".formatted(APP.getAdminPort()))
                .request()
                .post(Entity.text(""));

        Response response = APP.client()
                .target("http://localhost:%d/status".formatted(APP.getLocalPort()))
                .request()
                .get();

        assertEquals(503, response.getStatus());
        assertTrue(response.readEntity(String.class).contains("\"database\":\"DOWN\""));
    }
}
