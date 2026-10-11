package com.howtodoinjava.dropwizard.client.inventory;

import com.howtodoinjava.dropwizard.client.api.Reservation;
import com.howtodoinjava.dropwizard.client.api.StockLevel;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stands in for a separate inventory service so that the example runs as one jar.
 * In a real system, this resource runs in another application.
 */
@Path("/inventory")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InventoryResource {

    private final Map<String, Integer> stock = new ConcurrentHashMap<>(Map.of("coffee", 5, "tea", 0));

    @GET
    @Path("/{sku}")
    public StockLevel stockLevel(@PathParam("sku") String sku) {
        Integer quantity = stock.get(sku);
        if (quantity == null) {
            throw new NotFoundException("Unknown SKU " + sku);
        }
        return new StockLevel(sku, quantity);
    }

    @POST
    @Path("/reservations")
    public synchronized Response reserve(Reservation reservation) {
        Integer quantity = stock.get(reservation.sku());
        if (quantity == null) {
            throw new NotFoundException("Unknown SKU " + reservation.sku());
        }
        if (quantity < reservation.quantity()) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        int left = quantity - reservation.quantity();
        stock.put(reservation.sku(), left);
        return Response.status(Response.Status.CREATED)
                .entity(new StockLevel(reservation.sku(), left))
                .build();
    }
}
