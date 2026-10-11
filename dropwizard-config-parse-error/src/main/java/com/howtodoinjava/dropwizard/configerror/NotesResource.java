package com.howtodoinjava.dropwizard.configerror;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/notes")
@Produces(MediaType.APPLICATION_JSON)
public class NotesResource {

    @GET
    public List<String> findAll() {
        return List.of("Renew passport", "Call the plumber");
    }
}
