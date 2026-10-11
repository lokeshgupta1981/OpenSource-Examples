package com.howtodoinjava.dropwizard.client;

import com.howtodoinjava.dropwizard.client.api.StockLevel;
import com.howtodoinjava.dropwizard.client.client.InventoryClient;
import com.howtodoinjava.dropwizard.client.client.InventoryUnavailableException;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.testing.junit5.DropwizardClientExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.util.Duration;
import jakarta.ws.rs.client.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DropwizardExtensionsSupport.class)
class ClientResilienceTest {

    private static final FlakyInventoryResource FLAKY = new FlakyInventoryResource();
    private static final DropwizardClientExtension INVENTORY = new DropwizardClientExtension(FLAKY);

    @BeforeEach
    void resetCalls() {
        FLAKY.calls.set(0);
    }

    private InventoryClient clientWith(int retries, Duration timeout, String name) {
        JerseyClientConfiguration config = new JerseyClientConfiguration();
        config.setRetries(retries);
        config.setTimeout(timeout);
        Client client = new JerseyClientBuilder(INVENTORY.getEnvironment()).using(config).build(name);
        return new InventoryClient(client, INVENTORY.baseUri().toString());
    }

    @Test
    void retryRepeatsGetAfter503() {
        InventoryClient inventoryClient = clientWith(1, Duration.seconds(2), "retry-client");
        StockLevel stock = inventoryClient.findStock("coffee").orElseThrow();
        assertEquals(7, stock.quantity());
        assertEquals(2, FLAKY.calls.get());
    }

    @Test
    void noRetryPassesThe503On() {
        InventoryClient inventoryClient = clientWith(0, Duration.seconds(2), "no-retry-client");
        InventoryUnavailableException e = assertThrows(InventoryUnavailableException.class,
                () -> inventoryClient.findStock("coffee"));
        assertEquals("Inventory returned 503", e.getMessage());
    }

    @Test
    void slowServiceHitsTheTimeout() {
        InventoryClient inventoryClient = clientWith(0, Duration.milliseconds(500), "timeout-client");
        InventoryUnavailableException e = assertThrows(InventoryUnavailableException.class,
                () -> inventoryClient.findStock("slow"));
        System.out.println("timeout message: " + e.getMessage());
        assertTrue(e.getMessage().contains("SocketTimeoutException"));
    }
}
