package com.howtodoinjava.dropwizard.auth.resources;

import com.howtodoinjava.dropwizard.auth.core.User;
import io.dropwizard.auth.Auth;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Optional;

@Path("/welcome")
@Produces(MediaType.TEXT_PLAIN)
public class WelcomeResource {

    @GET
    public String welcome(@Auth Optional<User> user) {
        return user.map(u -> "Welcome back, " + u.getName())
                .orElse("Welcome, guest");
    }
}
