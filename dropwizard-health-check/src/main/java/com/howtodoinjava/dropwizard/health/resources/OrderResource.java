package com.howtodoinjava.dropwizard.health.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Map;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

    @GET
    public List<Map<String, Object>> findAll() {
        return List.of(Map.of("id", 1, "item", "coffee beans", "quantity", 2));
    }
}
