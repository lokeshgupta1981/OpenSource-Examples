package com.howtodoinjava.dropwizard.client.client;

import io.dropwizard.jersey.errors.ErrorMessage;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryUnavailableExceptionMapper implements ExceptionMapper<InventoryUnavailableException> {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryUnavailableExceptionMapper.class);

    @Override
    public Response toResponse(InventoryUnavailableException e) {
        LOG.warn(e.getMessage());
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ErrorMessage(503, "Inventory service unavailable"))
                .build();
    }
}
