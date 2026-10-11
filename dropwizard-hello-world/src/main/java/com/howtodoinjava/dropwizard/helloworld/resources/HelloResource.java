package com.howtodoinjava.dropwizard.helloworld.resources;

import com.howtodoinjava.dropwizard.helloworld.api.Greeting;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
public class HelloResource {

    private final String template;
    private final String defaultName;
    private final AtomicLong counter = new AtomicLong();

    public HelloResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
    }

    @GET
    public Greeting sayHello(@QueryParam("name") Optional<String> name) {
        String content = template.formatted(name.orElse(defaultName));
        return new Greeting(counter.incrementAndGet(), content);
    }
}
