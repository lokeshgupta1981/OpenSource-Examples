package com.howtodoinjava.dropwizard.client;

import com.howtodoinjava.dropwizard.client.api.StockLevel;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.concurrent.atomic.AtomicInteger;

/** Fails the first call with 503, answers later calls; one endpoint answers slowly. */
@Path("/inventory")
@Produces(MediaType.APPLICATION_JSON)
public class FlakyInventoryResource {

    final AtomicInteger calls = new AtomicInteger();

    @GET
    @Path("/{sku}")
    public Response stockLevel(@PathParam("sku") String sku) throws InterruptedException {
        if (sku.equals("slow")) {
            Thread.sleep(3000);
        }
        if (calls.incrementAndGet() == 1) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }
        return Response.ok(new StockLevel(sku, 7)).build();
    }
}
