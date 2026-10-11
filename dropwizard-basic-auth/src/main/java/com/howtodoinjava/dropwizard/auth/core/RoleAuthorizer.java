package com.howtodoinjava.dropwizard.auth.core;

import io.dropwizard.auth.Authorizer;
import jakarta.ws.rs.container.ContainerRequestContext;

public class RoleAuthorizer implements Authorizer<User> {

    @Override
    public boolean authorize(User user, String role, ContainerRequestContext requestContext) {
        return user.roles().contains(role);
    }
}
