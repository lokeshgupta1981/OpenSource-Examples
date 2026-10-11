package com.howtodoinjava.dropwizard.health.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

/** Fake payment gateway status endpoint, so the demo has something to call. */
@Path("/fake-gateway/status")
@Produces(MediaType.APPLICATION_JSON)
public class GatewayStatusResource {

    @GET
    public Map<String, String> status() {
        return Map.of("status", "UP");
    }
}
