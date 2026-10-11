package com.howtodoinjava.dropwizard.client.resources;

import com.howtodoinjava.dropwizard.client.api.Availability;
import com.howtodoinjava.dropwizard.client.api.Reservation;
import com.howtodoinjava.dropwizard.client.api.StockLevel;
import com.howtodoinjava.dropwizard.client.client.InventoryClient;
import com.howtodoinjava.dropwizard.client.client.InventoryHttpClient;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    private final InventoryClient inventoryClient;
    private final InventoryHttpClient inventoryHttpClient;

    public OrderResource(InventoryClient inventoryClient, InventoryHttpClient inventoryHttpClient) {
        this.inventoryClient = inventoryClient;
        this.inventoryHttpClient = inventoryHttpClient;
    }

    @GET
    @Path("/availability/{sku}")
    public Availability availability(@PathParam("sku") String sku) {
        StockLevel stock = inventoryClient.findStock(sku)
                .orElseThrow(() -> new NotFoundException("Unknown SKU " + sku));
        return new Availability(sku, stock.quantity(), stock.quantity() > 0, "jersey-client");
    }

    @GET
    @Path("/availability/{sku}/http-client")
    public Availability availabilityWithHttpClient(@PathParam("sku") String sku) {
        StockLevel stock = inventoryHttpClient.findStock(sku)
                .orElseThrow(() -> new NotFoundException("Unknown SKU " + sku));
        return new Availability(sku, stock.quantity(), stock.quantity() > 0, "http-client");
    }

    @POST
    public Response placeOrder(Reservation reservation) {
        StockLevel left = inventoryClient.reserve(reservation);
        return Response.status(Response.Status.CREATED).entity(left).build();
    }
}
