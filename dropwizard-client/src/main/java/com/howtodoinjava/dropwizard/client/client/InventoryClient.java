package com.howtodoinjava.dropwizard.client.client;

import com.howtodoinjava.dropwizard.client.api.Reservation;
import com.howtodoinjava.dropwizard.client.api.StockLevel;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

public class InventoryClient {

    private final WebTarget inventory;

    public InventoryClient(Client client, String inventoryUrl) {
        this.inventory = client.target(inventoryUrl).path("inventory");
    }

    public Optional<StockLevel> findStock(String sku) {
        try (Response response = inventory.path(sku)
                .request(MediaType.APPLICATION_JSON)
                .get()) {
            if (response.getStatus() == 404) {
                return Optional.empty();
            }
            if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
                throw new InventoryUnavailableException("Inventory returned " + response.getStatus());
            }
            return Optional.of(response.readEntity(StockLevel.class));
        } catch (ProcessingException e) {
            throw new InventoryUnavailableException("Inventory call failed: " + e.getMessage(), e);
        }
    }

    public StockLevel reserve(Reservation reservation) {
        try (Response response = inventory.path("reservations")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(reservation))) {
            if (response.getStatus() == 409) {
                throw new ClientErrorException("Not enough stock for " + reservation.sku(), 409);
            }
            if (response.getStatus() != 201) {
                throw new InventoryUnavailableException("Inventory returned " + response.getStatus());
            }
            return response.readEntity(StockLevel.class);
        } catch (ProcessingException e) {
            throw new InventoryUnavailableException("Inventory call failed: " + e.getMessage(), e);
        }
    }
}
