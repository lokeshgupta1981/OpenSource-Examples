package com.howtodoinjava.dropwizard.client;

import com.howtodoinjava.dropwizard.client.api.Availability;
import com.howtodoinjava.dropwizard.client.inventory.InventoryResource;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardClientExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(DropwizardExtensionsSupport.class)
class OrderServiceApplicationTest {

    private static final DropwizardClientExtension INVENTORY =
            new DropwizardClientExtension(new InventoryResource());

    // An instance field starts after the static INVENTORY field, so the URL supplier sees a running server
    private final DropwizardAppExtension<OrderServiceConfiguration> app = new DropwizardAppExtension<>(
            OrderServiceApplication.class,
            ResourceHelpers.resourceFilePath("test-config.yml"),
            ConfigOverride.randomPorts(),
            ConfigOverride.config("inventoryUrl", () -> INVENTORY.baseUri().toString()));

    @Test
    void ordersEndpointCallsInventoryService() {
        Availability coffee = app.client()
                .target("http://localhost:" + app.getLocalPort() + "/orders/availability/coffee")
                .request()
                .get(Availability.class);
        assertEquals(new Availability("coffee", 5, true, "jersey-client"), coffee);
    }

    @Test
    void httpClientVariantReturnsSameStock() {
        Availability coffee = app.client()
                .target("http://localhost:" + app.getLocalPort() + "/orders/availability/coffee/http-client")
                .request()
                .get(Availability.class);
        assertEquals(5, coffee.quantity());
    }

    @Test
    void unknownSkuGives404() {
        Response response = app.client()
                .target("http://localhost:" + app.getLocalPort() + "/orders/availability/milk")
                .request()
                .get();
        assertEquals(404, response.getStatus());
    }
}
