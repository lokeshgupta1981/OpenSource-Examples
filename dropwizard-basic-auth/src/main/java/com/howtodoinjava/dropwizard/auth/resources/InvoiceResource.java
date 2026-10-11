package com.howtodoinjava.dropwizard.auth.resources;

import com.howtodoinjava.dropwizard.auth.api.Invoice;
import com.howtodoinjava.dropwizard.auth.core.User;
import io.dropwizard.auth.Auth;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Path("/invoices")
@Produces(MediaType.APPLICATION_JSON)
public class InvoiceResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceResource.class);

    private final Map<Long, Invoice> invoices = new ConcurrentSkipListMap<>(Map.of(
            1L, new Invoice(1, "Asha", new BigDecimal("120.00")),
            2L, new Invoice(2, "Ben", new BigDecimal("75.50"))));

    @GET
    @PermitAll
    public List<Invoice> listInvoices(@Auth User user) {
        return List.copyOf(invoices.values());
    }

    @GET
    @Path("/{id}")
    @PermitAll
    public Invoice findInvoice(@PathParam("id") long id, @Auth User user) {
        Invoice invoice = invoices.get(id);
        if (invoice == null) {
            throw new NotFoundException("Invoice " + id + " not found");
        }
        return invoice;
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response deleteInvoice(@PathParam("id") long id, @Auth User admin) {
        if (invoices.remove(id) == null) {
            throw new NotFoundException("Invoice " + id + " not found");
        }
        LOG.info("Invoice {} deleted by {}", id, admin.getName());
        return Response.noContent().build();
    }
}
