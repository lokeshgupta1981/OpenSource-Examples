package com.howtodoinjava.dropwizard.health.resources;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
public class StatusResource {

    private final HealthCheckRegistry registry;

    public StatusResource(HealthCheckRegistry registry) {
        this.registry = registry;
    }

    @GET
    public Response status() {
        SortedMap<String, HealthCheck.Result> results = registry.runHealthChecks();
        Map<String, String> body = new TreeMap<>();
        results.forEach((name, result) -> body.put(name, result.isHealthy() ? "UP" : "DOWN"));

        boolean allHealthy = results.values().stream().allMatch(HealthCheck.Result::isHealthy);
        int status = allHealthy ? 200 : 503;
        return Response.status(status).entity(body).build();
    }
}
