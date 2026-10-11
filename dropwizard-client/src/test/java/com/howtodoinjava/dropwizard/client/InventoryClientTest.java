package com.howtodoinjava.dropwizard.client;

import com.howtodoinjava.dropwizard.client.api.Reservation;
import com.howtodoinjava.dropwizard.client.api.StockLevel;
import com.howtodoinjava.dropwizard.client.client.InventoryClient;
import com.howtodoinjava.dropwizard.client.inventory.InventoryResource;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.testing.junit5.DropwizardClientExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DropwizardExtensionsSupport.class)
class InventoryClientTest {

    private static final DropwizardClientExtension INVENTORY =
            new DropwizardClientExtension(new InventoryResource());

    private static Client client;
    private static InventoryClient inventoryClient;

    @BeforeAll
    static void setUp() {
        client = new JerseyClientBuilder(INVENTORY.getEnvironment())
                .using(new JerseyClientConfiguration())
                .build("test-inventory-client");
        inventoryClient = new InventoryClient(client, INVENTORY.baseUri().toString());
    }

    @AfterAll
    static void tearDown() {
        client.close();
    }

    @Test
    void readsStockLevelAsRecord() {
        String inventoryUrl = INVENTORY.baseUri().toString();
        StockLevel coffee = client.target(inventoryUrl)
                .path("inventory/coffee")
                .request(MediaType.APPLICATION_JSON)
                .get(StockLevel.class);
        assertEquals("coffee", coffee.sku());
    }

    @Test
    void returnsEmptyForUnknownSku() {
        Optional<StockLevel> milk = inventoryClient.findStock("milk");
        assertTrue(milk.isEmpty());
    }

    @Test
    void postsReservationAsJson() {
        int before = inventoryClient.findStock("coffee").orElseThrow().quantity();
        StockLevel left = inventoryClient.reserve(new Reservation("coffee", 2));
        assertEquals(before - 2, left.quantity());
    }
}
